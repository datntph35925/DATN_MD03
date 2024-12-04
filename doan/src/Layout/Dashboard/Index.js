import React from "react";
import { Menu, Modal } from "antd";
import {
  UserOutlined,
  HomeOutlined,
  SettingOutlined,
  ShoppingOutlined,
  BarChartOutlined,
  OrderedListOutlined,
  LogoutOutlined,
} from "@ant-design/icons";
import { Link, useNavigate, useLocation } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // Xác định mục menu đang hoạt động dựa trên pathname
  const getSelectedKey = () => {
    if (location.pathname.startsWith("/home")) return "1";
    if (location.pathname.startsWith("/profile")) return "2";
    if (location.pathname.startsWith("/products")) return "3";
    if (location.pathname.startsWith("/chat")) return "4";
    if (location.pathname.startsWith("/quanlydonhang")) return "5";
    return "";
  };

  const handleLogout = () => {
    Modal.confirm({
      title: "Xác nhận đăng xuất",
      content: "Bạn có chắc chắn muốn đăng xuất không?",
      okText: "Đăng xuất",
      cancelText: "Hủy",
      onOk: () => {
        navigate("/login");
      },
    });
  };

  return (
    <Menu
      mode="inline"
      selectedKeys={[getSelectedKey()]} // Đặt trạng thái mục đang hoạt động
      className="custom-menu"
    >
      <Menu.Item key="1" icon={<HomeOutlined />}>
        <Link to="/home">Trang chủ</Link>
      </Menu.Item>
      <Menu.Item key="2" icon={<UserOutlined />}>
        <Link to="/profile">Quản lí tài khoản</Link>
      </Menu.Item>
      <Menu.Item key="3" icon={<ShoppingOutlined />}>
        <Link to="/products">Sản phẩm</Link>
      </Menu.Item>
      <Menu.Item key="4" icon={<BarChartOutlined />}>
        <Link to="/chat">Hỗ trợ khách hàng</Link>
      </Menu.Item>
      <Menu.SubMenu
        key="5"
        icon={<OrderedListOutlined />}
        title="Quản lý đơn hàng"
      >
        <Menu.Item key="5-1">
          <Link to="/quanlydonhang/dangxuly">Đang xử lý</Link>
        </Menu.Item>
        <Menu.Item key="5-2">
          <Link to="/quanlydonhang/danggiao">Đang giao</Link>
        </Menu.Item>
        <Menu.Item key="5-3">
          <Link to="/quanlydonhang/dagiao">Đã giao</Link>
        </Menu.Item>
        <Menu.Item key="5-4">
          <Link to="/quanlydonhang/huy">Đã hủy</Link>
        </Menu.Item>
      </Menu.SubMenu>
      <Menu.Item key="7" icon={<LogoutOutlined />} onClick={handleLogout}>
        Đăng xuất
      </Menu.Item>
    </Menu>
  );
};

export default Dashboard;
