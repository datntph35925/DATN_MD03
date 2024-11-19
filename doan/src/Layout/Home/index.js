import React from "react";
import "./index.scss";
import { Layout } from "antd";
import Dashboard from "../Dashboard/Index"; // Import Dashboard
import { Outlet } from "react-router-dom"; // Import Outlet

const { Header, Footer, Sider, Content } = Layout;

const Home = () => {
  return (
    <Layout className="custom-layout">
      <Header className="custom-header">SneakZone</Header>
      <Layout>
        <Sider width="20%" className="custom-sider">
          <Dashboard />
        </Sider>
        <Content className="custom-content">
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

export default Home;
