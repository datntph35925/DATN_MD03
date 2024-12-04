import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { loginSuccess } from "../../Router/authSlice";
import backgroundImage from "../../Image/backgroup_nike.jpg";
import { LoginAPI, VerifyCodeAPI } from "../../Server/Auth";
import LoginModal from "../../Modal/LoginModal";
import "./index.scss";

const Login = () => {
  const [showModal, setShowModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [verificationEmailSent, setVerificationEmailSent] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [inputCode, setInputCode] = useState("");
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleLogin = async () => {
    try {
      const loginResponse = await LoginAPI({ username, password });

      if (loginResponse.status) {
        dispatch(loginSuccess(loginResponse.user)); // Lưu thông tin vào Redux
        setVerificationEmailSent(true); // Hiển thị modal xác minh
        setErrorMessage("");
      } else {
        setErrorMessage("Tên đăng nhập hoặc mật khẩu không đúng.");
      }
    } catch (error) {
      setErrorMessage(error.message || "Đăng nhập thất bại. Vui lòng thử lại.");
    }
  };

  const handleVerify = async () => {
    try {
      const verifyResponse = await VerifyCodeAPI({
        username,
        verificationCode: inputCode,
      });

      console.log("Verify Response:", verifyResponse);

      if (verifyResponse?.message) {
        setErrorMessage("");
        navigate("/home");
      } else {
        setErrorMessage(verifyResponse?.message || "Mã xác minh không hợp lệ.");
      }
    } catch (error) {
      console.error("handleVerify Error:", error);
      setErrorMessage(error.message || "Xác minh thất bại. Vui lòng thử lại.");
    }
  };

  return (
    <div className="container_login">
      <div className="img_background">
        <img src={backgroundImage} alt="background" />
        <button
          type="button"
          className="btn_login"
          onClick={() => setShowModal(true)}
        >
          Đăng nhập
        </button>
        <LoginModal
          showModal={showModal}
          onClose={() => {
            setShowModal(false);
            setVerificationEmailSent(false);
          }}
          username={username}
          setUsername={setUsername}
          password={password}
          setPassword={setPassword}
          inputCode={inputCode}
          setInputCode={setInputCode}
          onLogin={handleLogin}
          onVerify={handleVerify}
          errorMessage={errorMessage}
          verificationEmailSent={verificationEmailSent}
        />
      </div>
    </div>
  );
};

export default Login;
