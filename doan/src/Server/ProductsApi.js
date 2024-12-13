import axios from "./BASE";

// Hàm lấy danh sách sản phẩm
const getProduct = async () => {
  const response = await axios.get(`/api/get-list-product`);
  return response;
};

// Hàm thêm sản phẩm mới (hỗ trợ tải ảnh lên)
const addProduct = async (productData, imageFiles) => {
  try {
    // Tạo FormData để gửi dữ liệu sản phẩm và ảnh
    const formData = new FormData();

    // Thêm các chi tiết sản phẩm vào FormData (trừ trường ảnh)
    Object.keys(productData).forEach((key) => {
      if (key !== "HinhAnh") {
        // Tránh thêm URL ảnh vào đây vì chúng ta sẽ xử lý ảnh riêng
        formData.append(key, productData[key]);
      }
    });

    // Thêm các tệp ảnh vào FormData nếu có
    if (imageFiles && imageFiles.length > 0) {
      imageFiles.forEach((file) => {
        formData.append("images[]", file); // "images[]" là tên khóa, có thể thay đổi theo yêu cầu backend
      });
    }

    // Gửi yêu cầu POST với FormData
    const response = await axios.post("/api/add-product", formData, {
      headers: {
        "Content-Type": "multipart/form-data", // Đảm bảo Content-Type là multipart/form-data
      },
    });

    return response.data; // Trả về dữ liệu sản phẩm đã được thêm
  } catch (error) {
    console.error("Lỗi khi thêm sản phẩm:", error);
    throw error; // Ném lỗi để xử lý trong component
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
