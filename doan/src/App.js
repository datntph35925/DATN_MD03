import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Provider } from "react-redux";

import Home from "./Layout/Home";
import Login from "./Auth/Login";
import Homes from "./Famework/Home";
import Products from "./Famework/Product";
import QlDonHang from "./Famework/Order_Management/index";
import OrderDeingDelivered from "./Famework/Order_DeingDelivered/index";
import OrderBeenDelivered from "./Famework/Order_BeenDelivered/index";
import OrderBeenCancalled from "./Famework/Order_BeenCancelled/index";
import HistoryOutlined from "./Famework/Transaction_history";
import History from "./Famework/Transaction/index";
import Vochers from "./Famework/Vocher";
import Profile from "./Famework/Account_Management/Index";
import Chat from "./Famework/chat";
import store from "./Router/store";

const App = () => {
  return (
    <Provider store={store}>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Home />}>
            <Route path="products" element={<Products />} />
            <Route path="/home" element={<Homes />} />
            <Route path="/quanlydonhang/dangxuly" element={<QlDonHang />} />
            <Route
              path="/lichsugiaodich/dangxuly"
              element={<HistoryOutlined />}
            />
            <Route path="/lichsugiaodich/dathanhtoan" element={<History />} />
            <Route
              path="/quanlydonhang/danggiao"
              element={<OrderDeingDelivered />}
            />
            <Route
              path="/quanlydonhang/dagiao"
              element={<OrderBeenDelivered />}
            />
            <Route path="/quanlydonhang/huy" element={<OrderBeenCancalled />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/vocher" element={<Vochers />} />
            <Route path="/chat" element={<Chat />} />
          </Route>
        </Routes>
      </Router>
    </Provider>
  );
};

export default App;
