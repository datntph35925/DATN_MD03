import axios from "./BASE";

const getListOrders = async () => {
  try {
    const response = await axios.get("/order/get-list-order");
    return response.data.data;
  } catch (error) {
    console.error("Error fetching order list:", error);
    throw error;
  }
};
const updateOrderList = async (id, TrangThai) => {
  try {
    const response = await axios.put(`/order/update-order-status/${id}`, {
      TrangThai, // Send the new status in the request body
    });
    return response.data; // Return the updated order
  } catch (error) {
    console.error("Error updating order status:", error.response || error);
    throw error; // Rethrow to handle further
  }
};

export { getListOrders, updateOrderList };
