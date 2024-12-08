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
export { getlistVouchers, addVoucher };
