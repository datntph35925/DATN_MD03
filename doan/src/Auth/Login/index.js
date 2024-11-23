import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import backgroundImage from "../../Image/backgroup_nike.jpg";
import LoginAPI from "../../Server/Auth"; // Adjust the path based on your project structure
import "./index.scss";

const Imgae = [backgroundImage];

const Login = () => {
  const [showModal, setShowModal] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [verificationCode, setVerificationCode] = useState("");
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [isVerified, setIsVerified] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  // Generate a random verification code (can be replaced with actual backend logic if required)
  const generateCode = () => Math.floor(1000 + Math.random() * 9000).toString();
  const [expectedCode] = useState(generateCode);

  const handleLoginClick = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setIsSubmitted(false);
    setIsVerified(false);
    setVerificationCode("");
    setErrorMessage("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // Call the login API
      const response = await LoginAPI({ username, password });
      if (response.success) {
        setIsSubmitted(true); // Show the verification step
        // Optionally, fetch the verification code from the backend
      } else {
        setErrorMessage("Invalid username or password.");
      }
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  const handleVerify = (e) => {
    e.preventDefault();
    if (verificationCode === expectedCode) {
      setIsVerified(true);
      alert("Verification successful!");
      navigate("/Home");
    } else {
      setErrorMessage("Invalid verification code.");
    }
  };

  return (
    <div className="container_login">
      <div className="img_background">
        <img src={Imgae[0]} alt="background" />
        <button className="btn_login" onClick={handleLoginClick}>
          Login
        </button>
        {showModal && (
          <div className="modal">
            <div className="modal_content">
              <span className="close" onClick={handleCloseModal}>
                &times;
              </span>
              <h2>Login</h2>
              {!isSubmitted ? (
                <form onSubmit={handleSubmit}>
                  <div className="form_group">
                    <label htmlFor="username">Username</label>
                    <input
                      type="text"
                      id="username"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      placeholder="Enter your username"
                    />
                  </div>
                  <div className="form_group">
                    <label htmlFor="password">Password</label>
                    <input
                      type="password"
                      id="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      placeholder="Enter your password"
                    />
                  </div>
                  {errorMessage && (
                    <p className="error_message">{errorMessage}</p>
                  )}
                  <button type="submit" className="btn_submit">
                    Submit
                  </button>
                </form>
              ) : (
                <form onSubmit={handleVerify}>
                  <div className="verification_section">
                    <h3>Verification Required</h3>
                    <p className="verification_instruction">
                      Please enter the 4-digit verification code sent to you:{" "}
                      <strong>{expectedCode}</strong>
                    </p>
                    <div className="verification_input_group">
                      <input
                        type="text"
                        className="verification_input"
                        value={verificationCode}
                        onChange={(e) => setVerificationCode(e.target.value)}
                        placeholder="Enter verification code"
                      />
                      <button type="submit" className="btn_verify">
                        Verify
                      </button>
                    </div>
                  </div>
                  {errorMessage && (
                    <p className="error_message">{errorMessage}</p>
                  )}
                </form>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Login;
