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
        ] = await Promise.all([
          getTotalAccounts(),
          totalListProducts(),
          totalOrders(),
          totalOrdersQuantity(),
          totalOrdersRevenue(
            lastWeek.format("YYYY-MM-DD"),
            today.format("YYYY-MM-DD")
          ),
        ]);

        setAccountCount(accountResponse.totalAccounts || 0);
        setProductCount(productResponse.totalProducts || 0);
        setOrderCount(unprocessedOrdersCount.unprocessedOrders || 0);
        setSoldQuantity(soldQuantityResponse.totalSoldQuantity || 0);
        setRevenue(revenueResponse.totalRevenue || 0);

        // Lưu dữ liệu doanh thu để truyền xuống biểu đồ
        setRevenueData({
          dates: revenueResponse.dates || [],
          prices: revenueResponse.prices || [],
        });
      } catch (error) {
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
    },
    {
      count: soldQuantity !== null ? soldQuantity : "Đang tải...",
      label: "Đã Bán",
      icon: <EuroCircleOutlined />,
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
      onClick: () => navigate("/quanlydonhang/dangxuly"),
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
