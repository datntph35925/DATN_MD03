import axios from "./BASE";

// Gửi tin nhắn từ Admin
const sendMessage = async (TentaiKhoan, message) => {
  try {
    const response = await axios.post("/chatRouter/admin-gui-tin-nhan", {
      TentaiKhoan,
      message,
    });
    return response.data; // Trả về dữ liệu tin nhắn
  } catch (error) {
    console.error("Error sending message:", error);
    throw error;
  }
};

// Lấy lịch sử tin nhắn
const getChatHistory = async (TentaiKhoan) => {
  try {
    const response = await axios.get(`/chatRouter/lich-su-tin-nhan`, {
      params: { TentaiKhoan },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching chat history:", error);
    throw new Error("Không thể tải lịch sử tin nhắn.");
  }
};

export { sendMessage, getChatHistory };
