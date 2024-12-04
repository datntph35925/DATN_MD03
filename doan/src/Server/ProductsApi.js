import axios from "./BASE";

const getProduct = async () => {
  const response = await axios.get(`/api/get-list-product`);
  return response;
};

const addProduct = async (productData) => {
  try {
    const response = await axios.post("/api/add-product", productData);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi thêm sản phẩm:", error);
    throw error;
  }
};

const deleteProductById = async (id) => {
  try {
    const response = await axios.delete(`/api/delete-product-by-id/${id}`);

    if (response.status === 200) {
      console.log("Xóa sản phẩm thành công");
      return response.data;
    } else {
      console.error("Không thể xóa sản phẩm");
      return null;
    }
  } catch (error) {
    console.error("Lỗi khi xóa sản phẩm:", error);
    throw error;
  }
};

const updateProductById = async (id, updatedData) => {
  try {
    const response = await axios.put(
      `/api/update-product-by-id/${id}`,
      updatedData
    );

    if (response.status === 200) {
      console.log("Cập nhật sản phẩm thành công");
      return response.data; // Trả về dữ liệu sản phẩm đã được cập nhật
    } else {
      console.error("Không thể cập nhật sản phẩm");
      return null;
    }
  } catch (error) {
    console.error("Lỗi khi cập nhật sản phẩm:", error);
    throw error; // Ném lỗi để xử lý tốt hơn trong mã gọi hàm
  }
};

const totalListProducts = async () => {
  try {
    const response = await axios.get(`/api/total-products`);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi lấy tổng danh sách sản phẩm:", error);
    throw error;
  }
};

export {
  getProduct,
  addProduct,
  deleteProductById,
  updateProductById,
  totalListProducts,
};
