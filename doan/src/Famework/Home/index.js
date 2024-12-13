import React, { useEffect, useState } from "react";
import { Row, Col, Card, message, Spin } from "antd";
import {
  ShoppingOutlined,
  UserOutlined,
  BarChartOutlined,
  ProductOutlined,
  ShoppingCartOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

import { getTotalAccounts } from "../../Server/Auth";
import { totalListProducts } from "../../Server/ProductsApi";
import {
  totalOrders,
  totalOrdersQuantity,
  totalOrdersRevenue,
} from "../../Server/Order";
import "./index.scss";
import ApexChart from "../../Componer/Bieudo";

const Dashboard = () => {
  const [accountCount, setAccountCount] = useState(null);
  const [productCount, setProductCount] = useState(null);
  const [orderQuantity, setOrderQuantity] = useState(null);
  const [orderCount, setOrderCount] = useState(null);
  const [revenueData, setRevenueData] = useState(null);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const [
          accountData,
          productData,
          orderData,
          orderQuantityData,
          revenueDataResult,
        ] = await Promise.all([
          getTotalAccounts(),
          totalListProducts(),
          totalOrders(),
          totalOrdersQuantity(),
          totalOrdersRevenue(),
        ]);

        setAccountCount(accountData.totalAccounts || 0);
        setProductCount(productData.totalProducts || 0);
        setOrderCount(orderData.unprocessedOrders || 0);
        setOrderQuantity(orderQuantityData || 0);
        setRevenueData(revenueDataResult || 0);
      } catch (error) {
        message.error("Lỗi khi tải dữ liệu. Vui lòng thử lại.");
        console.error("Error fetching dashboard data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const data = [
    {
      count: revenueData !== null ? revenueData : "Đang tải...",
      label: "Doanh thu",
      icon: <BarChartOutlined />,
    },
    {
      count: orderQuantity !== null ? orderQuantity : "Đang tải...",
      label: "Sản phẩm đã bán",
      icon: <ShoppingCartOutlined />,
      onClick: () => navigate("/quanlydonhang/dagiao"),
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
        {revenueData ? (
          <ApexChart revenueData={revenueData} />
        ) : (
          <Spin size="small" />
        )}
      </div>
    </div>
  );
};

export default Dashboard;
