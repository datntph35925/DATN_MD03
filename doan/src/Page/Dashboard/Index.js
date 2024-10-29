import React from "react";
import { Menu } from "antd";
import {
  UserOutlined,
  HomeOutlined,
  SettingOutlined,
  ShoppingOutlined,
  BarChartOutlined,
  OrderedListOutlined,
} from "@ant-design/icons";
import { Link } from "react-router-dom";

const Dashboard = () => {
  return (
    <Menu mode="inline" defaultSelectedKeys={["1"]} className="custom-menu">
      <Menu.Item key="1" icon={<HomeOutlined />}>
        <Link to="/Home">Home</Link>
      </Menu.Item>
      <Menu.Item key="2" icon={<UserOutlined />}>
        <Link to="/profile">profile</Link>
      </Menu.Item>
      <Menu.Item key="3" icon={<ShoppingOutlined />}>
        <Link to="/products">Sản phẩm</Link>
      </Menu.Item>
      <Menu.Item key="4" icon={<BarChartOutlined />}>
        Thống kê
      </Menu.Item>
      <Menu.Item key="5" icon={<OrderedListOutlined />}>
        <Link to="/Quanlydonhang"> Quản lý đơn hàng</Link>
      </Menu.Item>
      <Menu.Item key="6" icon={<SettingOutlined />}>
        Settings
      </Menu.Item>
    </Menu>
  );
};

export default Dashboard;
