import axios from "./BASE";

// Pass the productId as a parameter
const getComment = async (productId) => {
  try {
    // Correctly insert productId into the API URL
    const response = await axios.get(
      `/reviews/get-reviews-product/${productId}`
    );
    return response.data.data;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách bình luận:", error);
    throw error;
  }
};

export { getComment };
