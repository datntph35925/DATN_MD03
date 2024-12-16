import axios from "axios";

const BASE_URL = "http://160.191.50.148:3000"; // Update this URL as per your environment

const instance = axios.create({
  baseURL: BASE_URL,
  timeout: 5000, // Set a reasonable timeout
  headers: {
    "Content-Type": "application/json",
  },
});

// Add an interceptor to include authentication token (if needed)
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("authToken"); // Replace with your token management logic
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Handle global response errors
instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.error("Unauthorized! Redirecting to login...");
      // Optional: Implement logout or redirect logic
    }
    return Promise.reject(error);
  }
);

export default instance;
