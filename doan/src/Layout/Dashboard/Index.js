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
  const location = useLocation(); // Lấy thông tin đường dẫn hiện tại

  // Xác định mục menu nào đang hoạt động dựa trên pathname
  const selectedKey = () => {
    switch (location.pathname) {
      case "/Home":
        return "1";
      case "/profile":
        return "2";
      case "/products":
        return "3";
      case "/Quanlydonhang":
        return "5";
      case "/settings":
        return "6";
      default:
        return ""; // Không chọn mục nào nếu không khớp
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
        <Link to="/Home">Home</Link>
      </Menu.Item>
      <Menu.Item key="2" icon={<UserOutlined />}>
        <Link to="/profile">Quản lí tài khoản</Link>
      </Menu.Item>
      <Menu.Item key="3" icon={<ShoppingOutlined />}>
        <Link to="/products">Sản phẩm</Link>
      </Menu.Item>
      <Menu.Item key="4" icon={<BarChartOutlined />}>
        <Link to="/thongke">Thống kê</Link>
      </Menu.Item>
      <Menu.Item key="5" icon={<OrderedListOutlined />}>
        <Link to="/Quanlydonhang">Quản lý đơn hàng</Link>
      </Menu.Item>
      <Menu.Item key="6" icon={<SettingOutlined />}>
        <Link to="/settings">Settings</Link>
      </Menu.Item>
      <Menu.Item key="7" icon={<LogoutOutlined />} onClick={handleLogout}>
        Logout
      </Menu.Item>
    </Menu>
  );
};

export default Dashboard;
