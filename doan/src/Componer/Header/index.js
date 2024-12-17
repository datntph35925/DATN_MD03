import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import {
  Avatar,
  Badge,
  Dropdown,
  Menu,
  Tooltip,
  Drawer,
  Modal,
  Button,
  notification,
} from "antd";
import { BellOutlined, UserOutlined } from "@ant-design/icons";
import logo from "../../Image/logo.png";
import NotificationItem from "../itemnotify";
import { getNotify, addNotification, getUsers } from "../../Server/notify"; // Adjust API path
import ModalAddNotification from "../../Modal/ModalAddnotifi"; // Import modal component
import "./index.scss";

const Header = () => {
  const user = useSelector((state) => state.auth.user);

  const [drawerVisible, setDrawerVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [modalVisible, setModalVisible] = useState(false); // Control modal visibility
  const [accounts, setAccounts] = useState([]); // State for available accounts

  useEffect(() => {
    const fetchNotifications = async () => {
      setLoading(true);
      try {
        const data = await getNotify();
        if (Array.isArray(data)) {
          setNotifications(data);
        } else if (data?.notifications && Array.isArray(data.notifications)) {
          setNotifications(data.notifications);
        } else {
          console.warn("Unexpected API response structure:", data);
        }
      } catch (error) {
        console.error("Error fetching notifications:", error);
      } finally {
        setLoading(false);
      }
    };



    fetchNotifications();

  }, []);

  const showDrawer = () => setDrawerVisible(true);
  const closeDrawer = () => setDrawerVisible(false);

  const showModal = () => setModalVisible(true); // Show modal
  const closeModal = () => setModalVisible(false); // Close modal

  const userMenu = (
    <Menu>
      <Menu.Item key="1" icon={<UserOutlined />}>
        {user?.email || "No Email"}
      </Menu.Item>
    </Menu>
  );

  return (
    <header className="custom-header">
      <div className="custom-header-logo">
        <img src={logo} alt="Logo" />
        <div className="custom-header-title">SneakZone</div>
      </div>

      <div className="custom-header-actions">
        {/* Notifications Bell */}
        <Tooltip title="Notifications">
          <Badge
            count={notifications.length}
            offset={[0, 5]}
            style={{ backgroundColor: "#f5222d" }}
          >
            <BellOutlined className="custom-header-bell" onClick={showDrawer} />
          </Badge>
        </Tooltip>

        {/* Notifications Drawer */}
        <Drawer
          title="Thông báo"
          placement="right"
          closable
          visible={drawerVisible}
          onClose={closeDrawer}
          destroyOnClose
          width={400}
          footer={<Button onClick={showModal}>Gửi</Button>} // Show modal when clicked
        >
          {loading ? (
            <p>Loading...</p>
          ) : notifications.length === 0 ? (
            <p>No new notifications.</p>
          ) : (
            notifications.map((notification, index) => (
              <NotificationItem
                key={index}
                tenTaiKhoan={notification.tentaikhoan}
                title={notification.title}
                message={notification.message}
                timestamp={notification.timestamp}
              />
            ))
          )}
        </Drawer>

        {/* User Menu */}
        <Dropdown overlay={userMenu} placement="bottomRight" arrow>
          <div className="custom-header-avatar">
            <Avatar
              src={user?.avatar || null}
              icon={!user?.avatar && <UserOutlined />}
              style={{
                backgroundColor: user?.avatar ? "transparent" : "#87d068",
              }}
            />

          </div>
        </Dropdown>
      </div>

      {/* Modal for adding new notification */}
      <ModalAddNotification
        visible={modalVisible}
        onClose={closeModal}
        onAddNotification={(values) => {
          setNotifications((prevNotifications) => [
            ...prevNotifications,
            values,
          ]);
          closeModal();
        }}
        accounts={accounts} // Pass available accounts (users) to modal
      />
    </header>
  );
};

export default Header;
