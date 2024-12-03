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
const deleteProductById = async (id) => {
  try {
    const response = await axios.delete(`/api/delete-product-by-id/${id}`);

    if (response.status === 200) {
      console.log("Product deleted successfully");
      return response.data;
    } else {
      console.error("Failed to delete product");
      return null;
    }
  } catch (error) {
    console.error("Error deleting product:", error);
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
      console.log("Product updated successfully");
      return response.data; // Return the updated product data
    } else {
      console.error("Failed to update product");
      return null;
    }
  } catch (error) {
    console.error("Error updating product:", error);
    throw error; // Throw the error for better error handling in the calling code
  }
};

export { getProduct, addProduct, deleteProductById, updateProductById };
