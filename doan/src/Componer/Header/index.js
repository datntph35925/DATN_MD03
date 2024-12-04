import React from "react";
import { useSelector } from "react-redux";
import { Avatar, Badge, Dropdown, Menu, Tooltip } from "antd";
import { BellOutlined, UserOutlined } from "@ant-design/icons";
import logo from "../../Image/logo.png";
import "./index.scss"; // Import file SCSS

// Thay đổi URL của logo

const Header = () => {
  // Access user data from Redux
  const user = useSelector((state) => state.auth.user);

  const userMenu = (
    <Menu>
      <Menu.Item key="1" icon={<UserOutlined />}>
        {user?.email || "No email available"}
      </Menu.Item>
    </Menu>
  );

  const notificationsMenu = (
    <Menu>
      <Menu.Item key="1">You have a new message</Menu.Item>
      <Menu.Item key="2">Your order has been shipped</Menu.Item>
      <Menu.Item key="3">Reminder: Meeting at 3 PM</Menu.Item>
      <Menu.Divider />
      <Menu.Item key="4">View all notifications</Menu.Item>
    </Menu>
  );

  return (
    <header className="custom-header">
      <div className="custom-header-logo">
        <img src={logo} alt="Logo" />
        <div className="custom-header-title">SneakZone</div>
      </div>

      <div className="custom-header-actions">
        <Tooltip title="Notifications">
          <Dropdown
            overlay={notificationsMenu}
            trigger={["click"]}
            placement="bottomRight"
            arrow
          >
            <Badge
              count={3}
              offset={[0, 5]}
              style={{ backgroundColor: "#f5222d" }}
            >
              <BellOutlined className="custom-header-bell" />
            </Badge>
          </Dropdown>
        </Tooltip>
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
