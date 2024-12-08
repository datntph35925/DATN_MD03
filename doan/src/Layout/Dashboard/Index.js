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
  HistoryOutlined,
} from "@ant-design/icons";
import { Link, useNavigate, useLocation } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // Xác định mục menu đang hoạt động
  const getSelectedKey = () => {
    if (location.pathname.startsWith("/home")) return "1";
    if (location.pathname.startsWith("/profile")) return "2";
    if (location.pathname.startsWith("/products")) return "3";
    if (location.pathname.startsWith("/chat")) return "4";
    if (location.pathname.startsWith("/quanlydonhang")) return "5";
    if (location.pathname.startsWith("/lichsugiaodich")) return "6";
    return "";
  };

  // Xử lý đăng xuất
  const handleLogout = () => {
    Modal.confirm({
      title: "Xác nhận đăng xuất",
      content: "Bạn có chắc chắn muốn đăng xuất không?",
      okText: "Đăng xuất",
      cancelText: "Hủy",
      onOk: () => {
        localStorage.removeItem("isLoggedIn");
        navigate("/login");
      },
    });
  };

  return (
    <Menu
      mode="inline"
      selectedKeys={[getSelectedKey()]}
      className="custom-menu"
    >
      {/* Trang chủ */}
      <Menu.Item key="1" icon={<HomeOutlined />}>
        <Link to="/home">Trang chủ</Link>
      </Menu.Item>

      {/* Quản lý tài khoản */}
      <Menu.Item key="2" icon={<UserOutlined />}>
        <Link to="/profile">Quản lý tài khoản</Link>
      </Menu.Item>

      {/* Sản phẩm */}
      <Menu.Item key="3" icon={<ShoppingOutlined />}>
        <Link to="/products">Sản phẩm</Link>
      </Menu.Item>

      {/* Hỗ trợ khách hàng */}
      <Menu.Item key="4" icon={<BarChartOutlined />}>
        <Link to="/chat">Hỗ trợ khách hàng</Link>
      </Menu.Item>

      {/* Quản lý đơn hàng */}
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

      {/* Lịch sử giao dịch */}
      <Menu.SubMenu
        key="6"
        icon={<HistoryOutlined />}
        title="Lịch sử giao dịch"
      >
        <Menu.Item key="6-1">
          <Link to="/lichsugiaodich/dangxuly">Đang xử lý</Link>
        </Menu.Item>
        <Menu.Item key="6-2">
          <Link to="/lichsugiaodich/dathanhtoan">Đã thanh toán</Link>
        </Menu.Item>
      </Menu.SubMenu>

      {/* Đăng xuất */}
      <Menu.Item key="7" icon={<LogoutOutlined />} onClick={handleLogout}>
        Đăng xuất
      </Menu.Item>
    </Menu>
  );
};

export default Dashboard;
