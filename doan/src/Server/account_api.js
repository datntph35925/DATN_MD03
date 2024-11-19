import axios from "./BASE";

const getListAccount = async () => {
  try {
    const response = await axios.get(`/auth/list-accounts`);
    return response.data; // Assuming you want the data from the response
  } catch (error) {
    console.error("Error fetching account list:", error);
    throw error; // Re-throw error for handling in calling functions
  }
};

export { getListAccount };
