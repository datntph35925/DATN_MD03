import axios from "./BASE";

const getNotify = async () => {
  try {
    const response = await axios.get(`/routes/notifications/`);
    return response.data;
  } catch (error) {
    console.error("L��i khi lấy thông báo:", error);
  }
};
const addNotification = async ({ title, message, account = "all" }) => {
  try {
    const response = await axios.post(
        `/routes/notifications/guithongbaotatca`,
        {
          title,
          message,
          account, // "all" hoặc ID tài khoản
        }
    );

    // Trích xuất dữ liệu trả về từ server
    const { message: successMessage, notificationsCount } = response.data;

    console.log(`Thông báo thành công: ${successMessage}`);
    console.log(`Số lượng thông báo đã gửi: ${notificationsCount}`);

    return response.data;
  } catch (error) {
    console.error("Lỗi khi gửi thông báo:", error);

    if (error.response && error.response.data) {
      console.error("Chi tiết lỗi từ server:", error.response.data.error);
    }
    throw error; // Ném lỗi để xử lý phía giao diện
  }
};
export { getNotify, addNotification };
