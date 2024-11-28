import React, { useState, useEffect } from "react";
import { fetchMessages, fetchChatHistory } from "../../Server/chat";
import { getListAccount } from "../../Server/account_api";
import "./index.scss";

const Chat = () => {
  const [users, setUsers] = useState([]);
  const [activeUser, setActiveUser] = useState(null);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  // Lấy danh sách người dùng khi component được tải
  useEffect(() => {
    const loadUsers = async () => {
      setLoading(true);
      try {
        const response = await getListAccount();
        setUsers(response);
        console.log("Users:", response);
      } catch (error) {
        console.error("Lỗi khi tải danh sách người dùng:", error);
      } finally {
        setLoading(false);
      }
    };

    loadUsers();
  }, []);

  // Hàm để chọn người dùng và tải lịch sử chat
  const selectUser = async (user) => {
    setActiveUser(user);
    setLoading(true);
    try {
      const response = await fetchChatHistory(user.id); // Lấy lịch sử tin nhắn
      setMessages(response.data || []);
      console.log("Messages for user:", response.data);
    } catch (error) {
      console.error("Lỗi khi tải lịch sử chat:", error);
    } finally {
      setLoading(false);
    }
  };

  // Hàm để gửi tin nhắn
  const sendMessage = async () => {
    if (!input.trim() || !activeUser) return;

    const newMessage = {
      TentaiKhoan: activeUser.email, // Gửi email người dùng
      message: input.trim(),
    };

    // Cập nhật UI cục bộ
    setMessages((prevMessages) => [
      ...prevMessages,
      { ...newMessage, sender: "me", userId: activeUser.id },
    ]);
    setInput("");

    // Gửi tin nhắn lên server
    try {
      await fetchMessages(newMessage);
      console.log("Tin nhắn đã được gửi:", newMessage);
    } catch (error) {
      console.error("Lỗi khi gửi tin nhắn:", error);
    }
  };

  return (
    <div className="chat-container">
      <div className="user-list">
        <h3>Người dùng</h3>
        {loading ? (
          <p>Đang tải danh sách người dùng...</p>
        ) : users.length > 0 ? (
          users.map((user) => (
            <div
              key={user.id}
              className={`user-item ${
                activeUser?.id === user.id ? "active" : ""
              }`}
              onClick={() => selectUser(user)}
            >
              {user.Hoten} {/* Changed from user.name to user.hoten */}
            </div>
          ))
        ) : (
          <p>Không có người dùng.</p>
        )}
      </div>
      <div className="chat-content">
        <div className="chat-header">
          <h2>Chat với {activeUser?.hoten || "..."}</h2>{" "}
          {/* Changed from user.name to user.hoten */}
        </div>
        <div className="chat-body">
          {loading ? (
            <p>Đang tải tin nhắn...</p>
          ) : activeUser ? (
            messages.length > 0 ? (
              messages.map((message, index) => (
                <div
                  key={index}
                  className={`chat-message ${
                    message.sender === "me"
                      ? "chat-message-sent"
                      : "chat-message-received"
                  }`}
                >
                  {message.text || message.message}
                </div>
              ))
            ) : (
              <p>Không có tin nhắn nào.</p>
            )
          ) : (
            <p>Chọn một người dùng để bắt đầu chat.</p>
          )}
        </div>
        <div className="chat-footer">
          <input
            type="text"
            placeholder="Nhập tin nhắn..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          />
          <button onClick={sendMessage}>Gửi</button>
        </div>
      </div>
    </div>
  );
};

export default Chat;
