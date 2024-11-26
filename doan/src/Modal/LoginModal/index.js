import React from "react";
import "./index.scss";

const LoginModal = ({
  showModal,
  onClose,
  username,
  setUsername,
  password,
  setPassword,
  inputCode,
  setInputCode,
  onLogin,
  onVerify,
  errorMessage,
  verificationEmailSent,
}) => {
  if (!showModal) return null;

  return (
    <div className="modal_overlay">
      <div className="modal_content">
        <button className="close_button" onClick={onClose}>
          ×
        </button>
        <h2>{verificationEmailSent ? "Xác minh mã" : "Đăng nhập"}</h2>
        {errorMessage && <p className="error_message">{errorMessage}</p>}

        {!verificationEmailSent ? (
          // Login form
          <>
            <input
              type="text"
              placeholder="Tên đăng nhập"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <input
              type="password"
              placeholder="Mật khẩu"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={onLogin}>Đăng nhập</button>
          </>
        ) : (
          // Verification code form
          <>
            <p>Mã xác minh đã được gửi tới email của bạn.</p>
            <input
              type="text"
              placeholder="Nhập mã xác minh"
              value={inputCode}
              onChange={(e) => setInputCode(e.target.value)}
            />
            <button onClick={onVerify}>Xác minh</button>
          </>
        )}
      </div>
    </div>
  );
};

export default LoginModal;
