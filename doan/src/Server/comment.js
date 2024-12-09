import axios from "./BASE";
const getComment = async () => {
  try {
    const response = await axios.get(`/comment/get-list-comment`);
    return response.data.data;
  } catch (error) {
    console.error("L��i khi lấy danh sách bình luận:", error);
    throw error;
  }
};
export { getComment };
