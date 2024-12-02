import axios from "./BASE";

const getListOrders = async () => {
  try {
    const response = await axios.get("/order/get-list-order");
    return response.data;
  } catch (error) {
    console.error("Error fetching order list:", error);
    throw error;
  }
};
export { getListOrders };
