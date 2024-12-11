import axios from "./BASE";

// Pass the productId as a parameter
const getComment = async (MaSanPham) => {
  try {
    // Gửi yêu cầu GET đến endpoint với MaSanPham
    const response = await axios.get(
      `/reviews/get-reviews-product/${MaSanPham}`
    );

    // Kiểm tra phản hồi và trả về dữ liệu nếu thành công
    if (response.data.success) {
      return response.data.data;
    } else {
      console.error("Lỗi từ API:", response.data.message);
      return [];
    }
  } catch (error) {
    console.error("Lỗi khi lấy danh sách bình luận:", error);
    throw error;
  }
};

export { getComment };
