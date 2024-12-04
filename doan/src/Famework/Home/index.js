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
import { totalOrders } from "../../Server/Order";
import "./index.scss";
import Bieudo from "../../Componer/Bieudo";

const Dashboard = () => {
  const [accountCount, setAccountCount] = useState(null);
  const [productCount, setProductCount] = useState(null);
  const [orderCount, setOrderCount] = useState(null); // State for unprocessed orders
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [accountResponse, productResponse, orderResponse] =
          await Promise.all([
            getTotalAccounts(),
            totalListProducts(),
            totalOrders(), // Fetch total orders
          ]);
        console.log("abc", orderResponse);
        setAccountCount(accountResponse.totalAccounts || 0);
        setProductCount(productResponse.totalProducts || 0);
        setOrderCount(orderResponse.totalOrders || 0); // Assume API returns `unprocessedOrders`
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
      count: 363,
      label: "Doanh thu",
      icon: <BarChartOutlined />,
    },
    {
      count: 12,
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
      onClick: () => navigate("/quanlydonhang/dangxuly"), // Navigate to orders page
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
        <Bieudo />
      </div>
    </div>
  );
};

export default Dashboard;
