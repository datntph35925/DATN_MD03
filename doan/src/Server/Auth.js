import axios from "../Server/BASE";

const Login = async (payload) => {
  try {
    const response = await axios.post("/auth/dang-nhap-admin", payload);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Login failed");
  }
};
export default Login;
