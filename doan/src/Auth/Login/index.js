import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate from react-router-dom
import backgroundImage from "../../Image/backgroup_nike.jpg"; // Adjust the path based on your project structure
import "./index.scss";

const Imgae = [backgroundImage];

const Login = () => {
  const [showModal, setShowModal] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLoginClick = () => {
    setShowModal(true);
  };
  const handleCloseModal = () => {
    setShowModal(false);
  };
  const handleSubmit = (e) => {
    e.preventDefault();

    if (username === "admin" && password === "password") {
      navigate("/Home");
    } else {
      alert("Invalid username or password");
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
                <button type="submit" className="btn_submit">
                  Submit
                </button>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Login;
