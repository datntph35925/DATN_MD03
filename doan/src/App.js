import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./Page/Home";
import Login from "./Auth/Login";
import Homes from "./Famework/Home";
import Products from "./Famework/AddProduct";
import QlDonHang from "./Famework/QLDonHang";

import Profile from "./Famework/QuanlyTK/Index";

const App = () => {
  return (
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
  );
};

export default App;
