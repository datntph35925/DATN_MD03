import { message } from "antd";
import axios from "./BASE.js";

// API lấy lịch sử giao dịch
const gethistory = async () => {
  try {
    const response = await axios.get(
      "/paymentAuthentication/get-list-paymentauthentication"
    );
    return response.data.data; // Lấy danh sách giao dịch từ response
  } catch (error) {
    console.error("Lỗi khi lấy lịch sử giao dịch:", error);
    throw error;
  }
};

const updateTransactionStatus = async (_id, newStatus) => {
  try {
    const response = await axios.put(
      `/paymentAuthentication/update-paymentauthentication-status/${_id}`,
      { TrangThai: newStatus }
    );
    return response.data;
  } catch (error) {
    if (error.response) {
      console.error("Response Error:", error.response.data);
    } else {
      console.error("Lỗi khi gửi yêu cầu:", error);
    }
    throw error;
  }
};

export { gethistory, updateTransactionStatus };
