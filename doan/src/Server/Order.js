import axios from "./BASE";

const getListOrders = async () => {
  try {
    const response = await axios.get("/order/get-list-order");
    return response.data.data;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách đơn hàng:", error);
    throw error;
  }
};

const updateOrderList = async (id, TrangThai) => {
  try {
    const response = await axios.put(`/order/update-order-status/${id}`, {
      TrangThai, // Gửi trạng thái mới trong phần body của yêu cầu
    });
    return response.data; // Trả về đơn hàng đã được cập nhật
  } catch (error) {
    console.error(
      "Lỗi khi cập nhật trạng thái đơn hàng:",
      error.response || error
    );
    throw error; // Ném lỗi để xử lý tiếp
  }
};

const totalOrders = async () => {
  try {
    const response = await axios.get("/order/total-orders");
    return response.data;
  } catch (error) {
    console.error("Lỗi khi lấy tổng số đơn hàng:", error);
    throw error;
  }
};

export { getListOrders, updateOrderList, totalOrders };
