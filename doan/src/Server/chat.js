import axios from "axios";

const fetchMessages = async (newMessage) => {
  try {
    const response = await axios.get(
      "/chatRouter/admin-gui-tin-nhan",
      newMessage
    );
    return response.data; // Trả về dữ liệu tin nhắn
  } catch (error) {
    console.error("Error fetching messages:", error);
    throw error;
  }
};

const fetchChatHistory = async (userId) => {
  try {
    const response = await axios.get(`/chatRouter/lich-su-tin-nhan`, {
      params: { userId },
    });
    return response.data; // Trả về danh sách tin nhắn
  } catch (error) {
    console.error("Lỗi khi lấy lịch sử tin nhắn:", error);
    throw error;
  }
};

export { fetchMessages, fetchChatHistory };
