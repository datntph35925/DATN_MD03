import axios from "./BASE";

const getNotify = async () => {
  try {
    const response = await axios.get(`/routes/notifications/`);
    return response.data;
  } catch (error) {
    console.error("L��i khi lấy thông báo:", error);
  }
};
const addNotification = async (title, message) => {
  try {
    const response = await axios.post(
      `/routes/notifications/guithongbaotatca`,
      {
        title,
        message,
      }
    );

    // Xử lý dữ liệu trả về từ backend
    const { message: successMessage, notificationsCount } = response.data;

    console.log(`Thông báo: ${successMessage}`);
    console.log(`Số lượng thông báo đã gửi: ${notificationsCount}`);

    return response.data; // Trả về dữ liệu nếu cần sử dụng
  } catch (error) {
    console.error("Lỗi khi thêm thông báo:", error);

    // Kiểm tra lỗi từ phía server hoặc lỗi kết nối
    if (error.response && error.response.data) {
      console.error("Chi tiết lỗi từ server:", error.response.data.error);
    }
  }
};

const getUsers = async () => {
  try {
    const response = await axios.get(`/routes/gui_thong_bao`);
    return response.data;
  } catch (error) {
    console.error("L��i khi lấy danh sách người dùng:", error);
  }
};
export { getNotify, addNotification, getUsers };
