import axios from "./BASE.js";

const getlistVouchers = async () => {
  try {
    const response = await axios.get("/voucher/get-list-vouchers");
    return response.data.data;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách voucher:", error);
    throw new Error("Không thể lấy danh sách voucher, vui lòng thử lại sau.");
  }
};

const addVoucher = async (voucher) => {
  try {
    const response = await axios.post("/voucher/add-vouchers", voucher);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi thêm voucher:", error);
    throw new Error("Không thể thêm voucher, vui lòng thử lại sau.");
  }
};

const deleteVoucher = async (id) => {
  try {
    const response = await axios.delete(`/voucher/delete-vouchers/${id}`);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi xóa voucher:", error);
    throw new Error("Không thể xóa voucher, vui lòng thử lại sau.");
  }
};

const updateVoucher = async (id, voucher) => {
  try {
    const response = await axios.put(`/voucher/update-vouchers/${id}`, voucher);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi cập nhật voucher:", error);
    if (error.response) {
      throw new Error(
        `Mã lỗi: ${error.response.status}, Thông báo: ${
          error.response.data.message || error.response.statusText
        }`
      );
    } else if (error.request) {
      throw new Error("Không nhận được phản hồi từ server. Vui lòng thử lại.");
    } else {
      throw new Error("Lỗi trong quá trình gửi yêu cầu. Vui lòng thử lại.");
    }
  }
};

export { getlistVouchers, addVoucher, deleteVoucher, updateVoucher };
