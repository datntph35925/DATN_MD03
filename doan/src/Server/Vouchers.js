import axios from "./BASE.js";

const getlistVouchers = async () => {
  try {
    const response = await axios.get("/voucher/get-list-vouchers");
    return response.data.data;
  } catch (error) {
    console.error("Lỗii khi lấy danh sách vouchers:", error);
  }
};

const addVoucher = async (voucher) => {
  try {
    const response = await axios.post("/voucher/add-vouchers", voucher);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi thêm voucher:", error);
  }
};
const deleteVoucher = async (id) => {
  try {
    const response = await axios.delete(`/voucher/delete-vouchers/${id}`);
    return response.data;
  } catch (error) {
    console.error("Lỗii khi xóa voucher:", error);
  }
};
const updateVoucher = async (id, voucher) => {
  try {
    const response = await axios.put(`/voucher/update-vouchers/${id}`, voucher);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi cập nhật voucher:", error);
  }
};
export { getlistVouchers, addVoucher, deleteVoucher, updateVoucher };
