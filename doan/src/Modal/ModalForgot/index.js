import React, { useState } from "react";
import { Form, Input, Button, Card, message } from "antd";
import PropTypes from "prop-types";
import { ForgotPasswordAPI, resetPasswordAPI } from "../../Server/Auth.js";
import "./index.scss";

const ForgotPassword = ({ onClose }) => {
  const [loading, setLoading] = useState(false);
  const [step, setStep] = useState(1); // Bước xử lý (1: nhập email và tài khoản, 2: đặt lại mật khẩu)

  // Xử lý bước 1: Gửi tài khoản và email để nhận mã xác nhận
  const onFinishStep1 = async (values) => {
    try {
      setLoading(true);
      const response = await ForgotPasswordAPI({
        username: values.username,
        account: values.account, // Gửi thêm tài khoản
      });
      console.log("ForgotPasswordAPI Response:", response); // Debug phản hồi
      message.success("Mã xác nhận đã được gửi đến email của bạn!");
      setStep(2);
    } catch (error) {
      console.error("onFinishStep1 Error:", error.message); // Log lỗi chi tiết
      message.error(error.message || "Gửi yêu cầu thất bại!");
    } finally {
      setLoading(false);
    }
  };

  // Xử lý bước 2: Nhập mã xác nhận và mật khẩu mới
  const onFinishStep2 = async (values) => {
    if (values.newPassword !== values.confirmPassword) {
      message.error("Mật khẩu xác nhận không khớp!");
      return;
    }
    try {
      setLoading(true);
      const response = await resetPasswordAPI({
        verificationCode: values.verificationCode,
        newPassword: values.newPassword,
      });
      console.log("resetPasswordAPI Response:", response); // Debug phản hồi
      message.success("Mật khẩu đã được đặt lại thành công!");
      if (onClose) onClose(); // Đóng modal sau khi thành công
    } catch (error) {
      console.error("onFinishStep2 Error:", error.message); // Log lỗi chi tiết
      message.error(error.message || "Đặt lại mật khẩu thất bại!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="forgot_password_modal">
      <Card
        title={step === 1 ? "Quên Mật Khẩu" : "Đặt Lại Mật Khẩu"}
        bordered={false}
        className="forgot_password_card"
      >
        <Form
          layout="vertical"
          name={step === 1 ? "forgotPasswordStep1" : "resetPasswordStep2"}
          onFinish={step === 1 ? onFinishStep1 : onFinishStep2}
          autoComplete="off"
        >
          {step === 1 ? (
            <>
              <Form.Item
                label="Email"
                name="username"
                rules={[
                  { required: true, message: "Vui lòng nhập email!" },
                  { type: "email", message: "Địa chỉ email không hợp lệ!" },
                ]}
              >
                <Input
                  placeholder="Nhập email của bạn"
                  aria-label="Email Input"
                />
              </Form.Item>
              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={loading}
                  block
                  className="form-button"
                >
                  Gửi Yêu Cầu
                </Button>
                <Button type="default" onClick={onClose} block>
                  Hủy
                </Button>
              </Form.Item>
            </>
          ) : (
            <>
              <Form.Item
                label="Mã Xác Nhận"
                name="verificationCode"
                rules={[
                  { required: true, message: "Vui lòng nhập mã xác nhận!" },
                  { len: 6, message: "Mã xác nhận phải có 6 chữ số!" },
                ]}
              >
                <Input
                  placeholder="Nhập mã xác nhận"
                  aria-label="Verification Code Input"
                />
              </Form.Item>
              <Form.Item
                label="Tài Khoản"
                name="username"
                rules={[
                  { required: true, message: "Vui lòng nhập tài khoản!" },
                ]}
              >
                <Input
                  placeholder="Nhập tài khoản của bạn"
                  aria-label="Account Input"
                />
              </Form.Item>
              <Form.Item
                label="Mật Khẩu Mới"
                name="newPassword"
                rules={[
                  { required: true, message: "Vui lòng nhập mật khẩu mới!" },
                  { min: 6, message: "Mật khẩu phải có ít nhất 6 ký tự!" },
                ]}
              >
                <Input.Password
                  placeholder="Nhập mật khẩu mới"
                  aria-label="New Password Input"
                />
              </Form.Item>
              <Form.Item
                label="Xác Nhận Mật Khẩu"
                name="confirmPassword"
                rules={[
                  { required: true, message: "Vui lòng xác nhận mật khẩu!" },
                ]}
              >
                <Input.Password
                  placeholder="Xác nhận mật khẩu mới"
                  aria-label="Confirm Password Input"
                />
              </Form.Item>
              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={loading}
                  block
                  className="form-button"
                >
                  Đặt Lại Mật Khẩu
                </Button>
                <Button type="default" onClick={onClose} block>
                  Hủy
                </Button>
              </Form.Item>
            </>
          )}
        </Form>
      </Card>
    </div>
  );
};

ForgotPassword.propTypes = {
  onClose: PropTypes.func.isRequired,
};

export default ForgotPassword;
