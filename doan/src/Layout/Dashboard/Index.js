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
  MailOutlined,
} from "@ant-design/icons";
import { Link, useNavigate, useLocation } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // Xác định mục menu nào đang hoạt động dựa trên pathname
  const selectedKey = () => {
    switch (location.pathname) {
      case "/home":
        return "1";
      case "/profile":
        return "2";
      case "/products":
        return "3";
      case "/chat":
        return "4";
      case "/quanlydonhang":
      case "/quanlydonhang/dangxuly":
      case "/quanlydonhang/dagiao":
        return "5";
      case "/settings":
        return "6";
      default:
        return "";
    }
  };

  const handleLogout = () => {
    Modal.confirm({
      title: "Xác nhận đăng xuất",
      content: "Bạn có chắc chắn muốn đăng xuất không?",
      okText: "Đăng xuất",
      cancelText: "Hủy",
      onOk: () => {
        navigate("/login"); // Điều hướng đến trang đăng nhập
      },
    });
  };

  return (
    <Menu
      mode="inline"
      selectedKeys={[selectedKey()]} // Đặt trạng thái mục đang hoạt động
      className="custom-menu"
    >
      <Menu.Item key="1" icon={<HomeOutlined />}>
        <Link to="/home">Home</Link>
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
      {/* <Menu.Item key="6" icon={<SettingOutlined />}>
        <Link to="/settings">Settings</Link>
      </Menu.Item> */}
      <Menu.Item key="7" icon={<LogoutOutlined />} onClick={handleLogout}>
        Logout
      </Menu.Item>
    </Menu>
  );
};

export default Dashboard;
