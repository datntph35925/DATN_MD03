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

  // Fetch list of users on mount
  useEffect(() => {
    const fetchUsers = async () => {
      setLoading((prev) => ({ ...prev, users: true }));
      try {
        const userList = await getListAccount();
        setUsers(userList);
      } catch (err) {
        console.error("Error fetching users:", err);
        setError("Không thể tải danh sách người dùng.");
      } finally {
        setLoading((prev) => ({ ...prev, users: false }));
      }
    };
    fetchUsers();
  }, []);

  // Fetch chat history for active user
  useEffect(() => {
    if (!activeUser) return;

    const fetchHistory = async () => {
      setLoading((prev) => ({ ...prev, messages: true }));
      try {
        const history = await getChatHistory(activeUser.Tentaikhoan); // Lấy lịch sử tin nhắn của người dùng từ backend
        setChatHistory(history);
      } catch (err) {
        console.error("Error fetching chat history:", err);
        setError("Không thể tải lịch sử tin nhắn.");
      } finally {
        setLoading((prev) => ({ ...prev, messages: false }));
      }
    };
    fetchHistory();
  }, [activeUser]);

  // Send a message to active user
  const handleSendMessage = async () => {
    if (!message.trim() || !activeUser) return;

    // Đưa tin nhắn "Đang gửi..." vào lịch sử trước khi gửi thật sự
    const tempMessage = {
      senderId: "admin",
      message: "Đang gửi...",
      temp: true, // Flag để phân biệt tin nhắn tạm thời
    };

    setChatHistory((prev) => [...prev, tempMessage]);

    try {
      // Gửi tin nhắn đến backend
      const newMessage = await sendMessage(activeUser.Tentaikhoan, message);

      setMessage(""); // Xóa nội dung trong input sau khi gửi

      // Sau 10 giây, cập nhật tin nhắn từ "Đang gửi..." thành tin nhắn thật
      setTimeout(() => {
        setChatHistory((prev) =>
          prev.map((msg) => (msg.temp ? { ...newMessage, temp: false } : msg))
        );
      }, 10000); // 10 giây
    } catch (err) {
      console.error("Error sending message:", err);
      setError("Không thể gửi tin nhắn.");
    }
  };

  // Helper to get user name by ID
  const getUserNameById = (id) => {
    const user = users.find((u) => u.Tentaikhoan === id);
    return user ? user.Hoten : id;
  };

  return (
    <div className="chat-container">
      {/* User List */}
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

      {/* Chat Content */}
      <div className="chat-content">
        <div className="chat-header">
          <h2>Chat với {activeUser?.Hoten || "..."}</h2>
        </div>

        {/* Chat Messages */}
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

        {/* Chat Input */}
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

      {/* Error Message */}
      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default Chat;
