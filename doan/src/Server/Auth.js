import axios from "../Server/BASE";

const LoginAPI = async (payload) => {
  try {
    const response = await axios.post("/auth/dang-nhap-admin", payload);

    if (response.status === 200) {
      return {
        status: true,
        verificationCode: response.data.verificationCode, // Mã xác minh trả về từ API
        message: response.data.status || "Đăng nhập thành công.",
      };
    } else {
      throw new Error("Tên đăng nhập hoặc mật khẩu không đúng.");
    }
  } catch (error) {
    throw new Error(
      error.response?.data?.message || "Đăng nhập thất bại. Vui lòng thử lại."
    );
  }
};

const VerifyCodeAPI = async (payload) => {
  try {
    const response = await axios.post("/auth/xac-thuc-ma-dang-nhap-admin", {
      username: payload.username,
      verificationCode: payload.verificationCode,
    });

    return response.data;
  } catch (error) {
    return {
      success: false,
      message:
        error.response?.data?.message ||
        "Không thể xác minh mã. Vui lòng thử lại.",
    };
  }
};

export default VerifyCodeAPI;

const ForgotPasswordAPI = async (payload) => {
  try {
    const response = await axios.post(
      "/auth/gui-ma-dat-lai-mat-khau-admin",
      payload
    );
    return response.data;
  } catch (error) {
    throw new Error(
      error.response?.data?.message || "Gửi mã đặt lại mật khẩu thất bại."
    );
  }
};

const resetPasswordAPI = async (payload) => {
  try {
    const response = await axios.post(
      "/auth/xac-thuc-ma-doi-mat-khau-admin",
      payload
    );
    return response.data;
  } catch (error) {
    throw new Error(
      error.response?.data?.message || "Cài lại mật khẩu thất bại."
    );
  }
};

const getTotalAccounts = async (payload) => {
  try {
    const response = await axios.get("/auth/tong-tai-khoan", payload);
    return response.data;
  } catch (error) {
    throw new Error(
      error.response?.data?.message || "Lấy tổng số tài khoản thất bại."
    );
  }
};

export {
  LoginAPI,
  ForgotPasswordAPI,
  resetPasswordAPI,
  VerifyCodeAPI,
  getTotalAccounts,
};
