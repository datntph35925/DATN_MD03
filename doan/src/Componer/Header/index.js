import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Avatar, Badge, Dropdown, Menu, Tooltip, Drawer } from "antd";
import { BellOutlined, UserOutlined } from "@ant-design/icons";
import logo from "../../Image/logo.png";
import NotificationItem from "../itemnotify";
import { getNotify } from "../../Server/notify"; // Adjust the API path
import "./index.scss";

const Header = () => {
  const user = useSelector((state) => state.auth.user);

  const [drawerVisible, setDrawerVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const fetchNotifications = async () => {
      setLoading(true); // Start loading
      try {
        const data = await getNotify(); // Fetch notifications
        if (Array.isArray(data)) {
          setNotifications(data); // Set notifications if response is an array
        } else if (data?.notifications && Array.isArray(data.notifications)) {
          setNotifications(data.notifications); // Handle wrapped notifications
        } else {
          console.warn("Unexpected API response structure:", data);
        }
      } catch (error) {
        console.error("Error fetching notifications:", error);
      } finally {
        setLoading(false); // Stop loading
      }
    };

    fetchNotifications();
  }, []);

  const showDrawer = () => setDrawerVisible(true); // Open drawer
  const closeDrawer = () => setDrawerVisible(false); // Close drawer

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
            count={notifications.length} // Show notification count
            offset={[0, 5]} // Badge position
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
          footer={<button onClick={closeDrawer}>Close</button>}
        >
          {loading ? (
            <p>Loading...</p> // Show loading indicator
          ) : notifications.length === 0 ? (
            <p>No new notifications.</p> // Show if no notifications
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
            <span>{user?.name || "Guest"}</span>
          </div>
        </Dropdown>
      </div>
    </header>
  );
};

export default Header;
