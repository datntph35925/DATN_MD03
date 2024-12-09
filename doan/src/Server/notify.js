import axios from "./BASE";

const getNotify = async () => {
  try {
    const response = await axios.get(`/routes/notifications/`);
    return response.data;
  } catch (error) {
    console.error("L��i khi lấy thông báo:", error);
  }
};
export { getNotify };
