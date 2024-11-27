import React, { useState } from "react";
import "./index.scss";

const Chat = () => {
  const [users, setUsers] = useState([
    { id: 1, name: "Alice" },
    { id: 2, name: "Bob" },
    { id: 3, name: "Charlie" },
  ]); // Danh sách người dùng
  const [activeUser, setActiveUser] = useState(users[0]); // Người dùng đang được chọn
  const [messages, setMessages] = useState([]); // Tin nhắn với người dùng đang chọn
  const [input, setInput] = useState("");

  const sendMessage = () => {
    if (input.trim() !== "") {
      setMessages([
        ...messages,
        { text: input, sender: "me", userId: activeUser.id },
      ]);
      setInput("");
    }
  };

  const selectUser = (user) => {
    setActiveUser(user);
    setMessages([]); // Reset tin nhắn khi chọn người dùng khác
  };

  return (
    <div className="chat-container">
      <div className="user-list">
        <h3>Users</h3>
        {users.map((user) => (
          <div
            key={user.id}
            className={`user-item ${activeUser.id === user.id ? "active" : ""}`}
            onClick={() => selectUser(user)}
          >
            {user.name}
          </div>
        ))}
      </div>
      <div className="chat-content">
        <div className="chat-header">
          <h2>Chat with {activeUser.name}</h2>
        </div>
        <div className="chat-body">
          {messages
            .filter((msg) => msg.userId === activeUser.id)
            .map((message, index) => (
              <div
                key={index}
                className={`chat-message ${
                  message.sender === "me"
                    ? "chat-message-sent"
                    : "chat-message-received"
                }`}
              >
                {message.text}
              </div>
            ))}
        </div>
        <div className="chat-footer">
          <input
            type="text"
            placeholder="Type a message..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          />
          <button onClick={sendMessage}>Send</button>
        </div>
      </div>
    </div>
  );
};

export default Chat;
