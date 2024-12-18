import axios from "./BASE";

// Hàm lấy danh sách sản phẩm
const getProduct = async () => {
  const response = await axios.get(`/api/get-list-product`);
  return response;
};

// Hàm thêm sản phẩm mới (hỗ trợ tải ảnh lên)
const addProduct = async (formData) => {
  try {
    const response = await axios.post("/api/add-product", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Lỗi khi thêm sản phẩm:", error);
    throw error;
  }
};

// Hàm xóa sản phẩm theo ID
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

// Hàm cập nhật sản phẩm theo ID
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
    throw error; // Ném lỗi để xử lý trong mã gọi hàm
  }
};

// Hàm lấy tổng số sản phẩm
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
