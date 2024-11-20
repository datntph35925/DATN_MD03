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
const deleteAccounts = async (accountId) => {
  try {
    const response = await axios.delete(`/auth/delete-account/${accountId}`);
    return response.data; // Return the response data (e.g., success message)
  } catch (error) {
    console.error("Error deleting account:", error);
    throw error; // Re-throw error for handling in calling functions
  }
};
export { getListAccount, deleteAccounts };
