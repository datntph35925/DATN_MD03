import React from "react";
import { Avatar, Badge, Dropdown, Menu } from "antd";
import { BellOutlined, UserOutlined, LogoutOutlined } from "@ant-design/icons";

const Header = () => {
  const user = {
    name: "John Doe",
    avatar: "",
  };

  const handleLogout = () => {
    console.log("Logging out...");
  };

  const userMenu = (
    <Menu>
      <Menu.Item key="1" icon={<UserOutlined />}>
        Profile
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
    <div
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0 20px",
        height: "64px",
        background: "#001529",
        color: "#fff",
      }}
    >
      <div
        style={{ fontSize: "20px", color: "#fff", fontWeight: "bold" }}
      ></div>
      <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
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
            <BellOutlined
              style={{ fontSize: "20px", color: "#fff", cursor: "pointer" }}
            />
          </Badge>
        </Dropdown>
        <Dropdown overlay={userMenu} placement="bottomRight" arrow>
          <div
            style={{
              display: "flex",
              alignItems: "center",
              cursor: "pointer",
              color: "#fff",
            }}
          >
            <Avatar
              src={user.avatar || null}
              icon={!user.avatar && <UserOutlined />}
              style={{
                backgroundColor: user.avatar ? "transparent" : "#87d068",
                marginRight: "8px",
              }}
            />
            <span>{user.name}</span>
          </div>
        </Dropdown>
      </div>
    </div>
  );
};

export default Header;
