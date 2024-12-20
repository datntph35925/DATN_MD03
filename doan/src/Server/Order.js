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
const totalOrdersQuantity = async () => {
  try {
    const response = await axios.get("/order/total-sold-quantity");
    return response.data.totalSoldQuantity;
  } catch (error) {
    console.error("Lỗi khi lấy tổng số đơn hàng theo số lượng:", error);
    throw error;
  }
};

const getRevenueStatistics = async (start, end) => {
  try {
    const response = await axios.get("/order/revenue-statistics", {
      params: { start, end }, // Truyền tham số start và end
    });
    return response.data.data;
  } catch (error) {
    console.error("Lỗi khi lấy tổng doanh thu đơn hàng:", error);
    throw error;
  }
};
const RevenueTotal = async () => {
  try {
    const response = await axios.get("/order/total-delivered-revenue");
    return response.data.totalRevenue;
  } catch (error) {
    console.error("Lỗi khi lấy tổng doanh thu:", error);
    throw error;
  }
};
const getTopSellingProducts = async (startDate, endDate) => {
  try {
    // Gửi request đến endpoint backend với tham số thời gian (nếu có)
    const response = await axios.get("/order/top-10-best-selling-products", {
      params: { startDate, endDate }, // Truyền tham số startDate và endDate nếu có
    });

    // Kiểm tra phản hồi từ backend
    if (response.status === 200 && response.data.status === 200) {
      return response.data.data; // Trả về danh sách top sản phẩm bán chạy
    } else {
      console.error(
          "Lỗi khi lấy top sản phẩm bán chạy:",
          response.data.message
      );
      return []; // Trả về mảng rỗng nếu có lỗi
    }
  } catch (error) {
    console.error("Lỗi khi gọi API lấy top 10 sản phẩm bán chạy:", error);
    return []; // Trả về mảng rỗng nếu có lỗi
  }
};

export {
  getListOrders,
  updateOrderList,
  totalOrders,
  totalOrdersQuantity,
  getRevenueStatistics,
  RevenueTotal,
  getTopSellingProducts,
};