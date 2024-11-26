import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Provider } from "react-redux";

import Home from "./Layout/Home";
import Login from "./Auth/Login";
import Homes from "./Famework/Home";
import Products from "./Famework/Product";
import QlDonHang from "./Famework/Order_Management/index";
import Profile from "./Famework/Account_Management/Index";

import store from "./Router/store";

const App = () => {
  return (
    <Provider store={store}>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Home />}>
            <Route path="products" element={<Products />} />
            <Route path="/Home" element={<Homes />} />
            <Route path="/Quanlydonhang" element={<QlDonHang />} />
            <Route path="/profile" element={<Profile />} />
          </Route>
        </Routes>
      </Router>
    </Provider>
  );
};

export default App;
