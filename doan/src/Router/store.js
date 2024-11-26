import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice"; // Một slice giả định

const store = configureStore({
  reducer: {
    auth: authReducer,
  },
});

export default store;
