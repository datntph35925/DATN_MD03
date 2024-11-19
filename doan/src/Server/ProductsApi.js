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
    console.error("Error adding product:", error);
    throw error;
  }
};
const deleteProductById = async (productId) => {
  try {
    const response = await axios.delete(`/api/delete-product-by-id`, {
      data: { productId },
    });

    if (response.status === 200) {
      console.log("Product deleted successfully");
      return response.data; // Return the response data (optional)
    } else {
      console.error("Failed to delete product");
      return null;
    }
  } catch (error) {
    console.error("Error deleting product:", error);
    return null;
  }
};

export { getProduct, addProduct, deleteProductById };