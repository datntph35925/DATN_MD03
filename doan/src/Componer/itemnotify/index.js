import React from "react";
import "./index.scss";

const NotificationItem = ({ tenTaiKhoan, title, message, timestamp }) => (
  <div className="notification-item">
    <p>
      <strong>Tài khoản:</strong> {tenTaiKhoan}
    </p>
    <p>
      <strong>Tiêu đề</strong> {title}
    </p>
    <p>
      <strong>Thông báo:</strong> {message}
    </p>
    <p>
      <strong>Ngày gửi:</strong> {timestamp}
    </p>
  </div>
);

export default NotificationItem;
