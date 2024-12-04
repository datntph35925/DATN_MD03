import React, { useState, useEffect } from "react";
import { sendMessage, getChatHistory } from "../../Server/chat";
import { getListAccount } from "../../Server/account_api";
import "./index.scss";

const Chat = () => {
  const [users, setUsers] = useState([]);
  const [activeUser, setActiveUser] = useState(null);
  const [chatHistory, setChatHistory] = useState([]);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState({ users: false, messages: false });
  const [error, setError] = useState("");

  // Lấy danh sách người dùng khi component được mount
  useEffect(() => {
    const fetchUsers = async () => {
      setLoading((prev) => ({ ...prev, users: true }));
      try {
        const userList = await getListAccount();
        setUsers(userList);
      } catch (err) {
        console.error("Lỗi khi lấy danh sách người dùng:", err);
        setError("Không thể tải danh sách người dùng.");
      } finally {
        setLoading((prev) => ({ ...prev, users: false }));
      }
    };
    fetchUsers();
  }, []);

  // Lấy lịch sử tin nhắn của người dùng đang hoạt động
  useEffect(() => {
    if (!activeUser) return;

    const fetchHistory = async () => {
      setLoading((prev) => ({ ...prev, messages: true }));
      try {
        const history = await getChatHistory(activeUser.Tentaikhoan); // Lấy lịch sử tin nhắn từ backend
        setChatHistory(history);
      } catch (err) {
        console.error("Lỗi khi lấy lịch sử tin nhắn:", err);
        setError("Không thể tải lịch sử tin nhắn.");
      } finally {
        setLoading((prev) => ({ ...prev, messages: false }));
      }
    };
    fetchHistory();
  }, [activeUser]);

  // Gửi tin nhắn tới người dùng đang hoạt động
  const handleSendMessage = async () => {
    if (!message.trim() || !activeUser) return;

    // Đưa tin nhắn "Đang gửi..." vào lịch sử trước khi gửi thật sự
    const tempMessage = {
      senderId: "admin",
      message: "Đang gửi...",
      temp: true, // Gắn cờ để phân biệt tin nhắn tạm thời
    };

    setChatHistory((prev) => [...prev, tempMessage]);

    try {
      // Gửi tin nhắn tới backend
      const newMessage = await sendMessage(activeUser.Tentaikhoan, message);

      setMessage(""); // Xóa nội dung trong ô nhập sau khi gửi

      // Sau 10 giây, cập nhật tin nhắn từ "Đang gửi..." thành tin nhắn thực
      setTimeout(() => {
        setChatHistory((prev) =>
          prev.map((msg) => (msg.temp ? { ...newMessage, temp: false } : msg))
        );
      }, 10000); // 10 giây
    } catch (err) {
      console.error("Lỗi khi gửi tin nhắn:", err);
      setError("Không thể gửi tin nhắn.");
    }
  };

  // Trợ giúp lấy tên người dùng theo ID
  const getUserNameById = (id) => {
    const user = users.find((u) => u.Tentaikhoan === id);
    return user ? user.Hoten : id;
  };

  return (
    <div className="chat-container">
      {/* Danh sách người dùng */}
      <div className="user-list">
        <h3>Người dùng</h3>
        {loading.users ? (
          <p>Đang tải...</p>
        ) : users.length ? (
          users.map((user) => (
            <div
              key={user.Hoten}
              className={`user-item ${
                activeUser?.Hoten === user.Hoten ? "active" : ""
              }`}
              onClick={() => {
                setActiveUser(user);
                setChatHistory([]);
                setError("");
              }}
            >
              {user.Hoten}
            </div>
          ))
        ) : (
          <p>Không có người dùng.</p>
        )}
      </div>

      {/* Nội dung chat */}
      <div className="chat-content">
        <div className="chat-header">
          <h2>Chat với {activeUser?.Hoten || "..."}</h2>
        </div>

        {/* Tin nhắn chat */}
        <div className="chat-body">
          {loading.messages ? (
            <p>Đang tải tin nhắn...</p>
          ) : chatHistory.length ? (
            chatHistory.map((msg, index) => (
              <div
                key={index}
                className={`chat-message ${
                  msg.senderId === "admin" ? "sent" : "received"
                }`}
              >
                <span className="sender-id">
                  {msg.senderId === "admin"
                    ? "Admin"
                    : getUserNameById(msg.senderId)}
                </span>
                <p>{msg.message}</p>
              </div>
            ))
          ) : (
            <p>Không có tin nhắn nào.</p>
          )}
        </div>

        {/* Nhập tin nhắn */}
        <div className="chat-footer">
          <input
            type="text"
            placeholder="Nhập tin nhắn..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSendMessage()}
            disabled={!activeUser}
          />
          <button onClick={handleSendMessage} disabled={!activeUser}>
            Gửi
          </button>
        </div>
      </div>

      {/* Hiển thị lỗi */}
      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default Chat;
