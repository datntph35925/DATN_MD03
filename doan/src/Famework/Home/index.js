import React, { useEffect, useState } from "react";
import { Row, Col, Card, message, Spin } from "antd";
import {
  ShoppingOutlined,
  UserOutlined,
  BarChartOutlined,
  EuroCircleOutlined,
  ProductOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { getTotalAccounts } from "../../Server/Auth";
import { totalListProducts } from "../../Server/ProductsApi";
import {
  totalOrders,
  totalOrdersQuantity,
  totalOrdersRevenue,
} from "../../Server/Order";
import dayjs from "dayjs";
import "./index.scss";
import ApexChart from "../../Componer/Bieudo";

const Dashboard = () => {
  const [accountCount, setAccountCount] = useState(null);
  const [productCount, setProductCount] = useState(null);
  const [orderCount, setOrderCount] = useState(null);
  const [soldQuantity, setSoldQuantity] = useState(null);
  const [revenue, setRevenue] = useState(null);
  const [revenueData, setRevenueData] = useState({ dates: [], prices: [] });
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const today = dayjs();
        const lastWeek = today.add(-7, "d");

        const [
          accountResponse,
          productResponse,
          unprocessedOrdersCount,
          soldQuantityResponse,
          revenueResponse,
        ] = await Promise.allSettled([
          getTotalAccounts(),
          totalListProducts(),
          totalOrders(),
          totalOrdersQuantity(),
          totalOrdersRevenue(
            lastWeek.format("YYYY-MM-DD"),
            today.format("YYYY-MM-DD")
          ),
        ]);

        // Xử lý từng API
        if (accountResponse.status === "fulfilled") {
          setAccountCount(accountResponse.value.totalAccounts || 0);
        } else {
          console.error("Error fetching accounts:", accountResponse.reason);
        }

        if (productResponse.status === "fulfilled") {
          setProductCount(productResponse.value.totalProducts || 0);
        } else {
          console.error("Error fetching products:", productResponse.reason);
        }

        if (unprocessedOrdersCount.status === "fulfilled") {
          setOrderCount(unprocessedOrdersCount.value.unprocessedOrders || 0);
        } else {
          console.error(
            "Error fetching orders:",
            unprocessedOrdersCount.reason
          );
        }

        if (soldQuantityResponse.status === "fulfilled") {
          if (
            soldQuantityResponse.value.status === 404 &&
            soldQuantityResponse.value.message === "Không có đơn hàng đã giao."
          ) {
            setSoldQuantity(0); // Không có sản phẩm đã bán
          } else {
            setSoldQuantity(soldQuantityResponse.value.totalSoldQuantity || 0);
          }
        } else {
          console.error(
            "Error fetching sold quantity:",
            soldQuantityResponse.reason
          );
          setSoldQuantity(0); // Đặt mặc định là 0 khi lỗi
        }

        if (revenueResponse.status === "fulfilled") {
          const revenueData = revenueResponse.value;
          setRevenue(revenueData.totalRevenue || 0);
          setRevenueData({
            dates: revenueData.dates || [],
            prices: revenueData.prices || [],
          });
        } else {
          console.error("Error fetching revenue:", revenueResponse.reason);
        }
      } catch (error) {
        console.error("Error in fetchData:", error);
        message.error(error.message || "Lấy dữ liệu thất bại");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const data = [
    {
      count: revenue !== null ? `$${revenue.toLocaleString()}` : "Đang tải...",
      label: "Doanh thu",
      icon: <BarChartOutlined />,
      onClick: () =>
        revenue === null && message.warning("Chưa có dữ liệu doanh thu."),
    },
    {
      count: soldQuantity !== null ? soldQuantity : "Đang tải...",
      label: "Đã Bán",
      icon: <EuroCircleOutlined />,
      onClick: () => {
        if (soldQuantity === 0) {
          message.info("Không có sản phẩm nào đã bán.");
        }
      },
    },
    {
      count: accountCount !== null ? accountCount : "Đang tải...",
      label: "Số người dùng",
      icon: <UserOutlined />,
      onClick: () => navigate("/profile"),
    },
    {
      count: productCount !== null ? productCount : "Đang tải...",
      label: "Sản phẩm",
      icon: <ShoppingOutlined />,
      onClick: () => navigate("/products"),
    },
    {
      count: orderCount !== null ? orderCount : "Đang tải...",
      label: "ĐH chưa xử lý",
      icon: <ProductOutlined />,
      onClick: () =>
        orderCount === 0 && message.warning("Không có đơn hàng chưa xử lý."),
    },
  ];

  if (loading) {
    return (
      <div className="dashboard-container">
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div className="dashboard-container">
      <div className="menu">
        <Row gutter={[16, 16]} justify="space-between">
          {data.map((item, index) => (
            <Col xs={24} sm={12} md={8} lg={4} key={index}>
              <Card
                className="dashboard-card"
                hoverable
                onClick={item.onClick}
                onMouseEnter={(e) => {
                  e.currentTarget.style.transform = "scale(1.05)";
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.transform = "scale(1)";
                }}
              >
                <div className="card-content">
                  <div className="card-count">{item.count}</div>
                  <div className="card-label">
                    {item.icon} <span>{item.label}</span>
                  </div>
                  <div className="card-detail">Chi tiết ➡️</div>
                </div>
              </Card>
            </Col>
          ))}
        </Row>
      </div>
      <div className="bieudo">
        <ApexChart revenueData={revenueData} />
      </div>
    </div>
  );
};

export default Dashboard;
