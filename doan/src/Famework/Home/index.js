import React from "react";
import { Row, Col, Card } from "antd";
import {
  ShoppingOutlined,
  UserOutlined,
  BarChartOutlined,
  EuroCircleOutlined,
  ProductOutlined,
} from "@ant-design/icons";
import "./index.scss"; // Import the external SCSS file
import Bieudo from "../../Componer/Bieudo";
const Dashboard = () => {
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
      count: 5,
      label: "Số người dùng",
      icon: <UserOutlined />,
    },
    {
      count: 5,
      label: "Sản phẩm",
      icon: <ShoppingOutlined />,
    },
    {
      count: 5,
      label: "Đơn hàng chưa xử lý",
      icon: <ProductOutlined />,
    },
  ];

  return (
    <div className="dashboard-container">
      <Row gutter={[16, 16]} justify="space-between">
        {data.map((item, index) => (
          <Col xs={24} sm={12} md={8} lg={4} key={index}>
            <Card
              className="dashboard-card"
              style={{ backgroundColor: item.color }}
              hoverable
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
      <Bieudo />
    </div>
  );
};

export default Dashboard;
