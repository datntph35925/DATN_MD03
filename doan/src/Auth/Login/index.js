import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import backgroundImage from "../../Image/backgroup_nike.jpg";
import { LoginAPI, VerifyCodeAPI } from "../../Server/Auth";
import LoginModal from "../../Modal/LoginModal";
import "./index.scss";

const Login = () => {
  const [showModal, setShowModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [verificationEmailSent, setVerificationEmailSent] = useState(false); // State to control verification flow
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [inputCode, setInputCode] = useState(""); // Code for verification
  const navigate = useNavigate();

  // Handle login logic
  const handleLogin = async () => {
    try {
      // Call the login API
      const loginResponse = await LoginAPI({ username, password });
      console.log(loginResponse);

      if (loginResponse.status) {
        // If login successful, move to verification phase
        setVerificationEmailSent(true);
        setErrorMessage("");
      } else {
        // Handle login failure
        setErrorMessage("Tên đăng nhập hoặc mật khẩu không đúng.");
      }
    } catch (error) {
      setErrorMessage(error.message || "Đăng nhập thất bại. Vui lòng thử lại.");
    }
  };

  // Handle code verification logic
  const handleVerify = async () => {
    try {
      // Call the VerifyCodeAPI and pass username and inputCode
      const verifyResponse = await VerifyCodeAPI({
        username,
        verificationCode: inputCode, // Pass the code entered in the modal
      });

      console.log("Verification response:", verifyResponse);

      if (verifyResponse.success) {
        // Navigate to home if verification is successful
        navigate("/Home");
      } else {
        setErrorMessage("Mã xác minh không hợp lệ."); // Display error if verification fails
      }
    } catch (error) {
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
            setVerificationEmailSent(false); // Reset state when closing modal
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
