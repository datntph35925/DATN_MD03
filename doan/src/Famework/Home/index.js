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
import "./index.scss"; // Import the external SCSS file
import Bieudo from "../../Componer/Bieudo";

const Dashboard = () => {
  const [accountCount, setAccountCount] = useState();
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate(); // Initialize useNavigate

  useEffect(() => {
    const fetchAccountCount = async () => {
      try {
        setLoading(true);
        const response = await getTotalAccounts();
        setAccountCount(response.totalAccounts || 0);
      } catch (error) {
        message.error(error.message || "Lấy số tài khoản thất bại");
      } finally {
        setLoading(false);
      }
    };

    fetchAccountCount();
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
      onClick: () => navigate("/profile"), // Navigate to /users when clicked
    },
    {
      count: 5,
      label: "Sản phẩm",
      icon: <ShoppingOutlined />,
    },
    {
      count: 5,
      label: "ĐH chưa xử lý",
      icon: <ProductOutlined />,
    },
  ];

  if (loading && accountCount === null) {
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
                style={{ backgroundColor: item.color }}
                hoverable
                onClick={item.onClick} // Handle click event
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
