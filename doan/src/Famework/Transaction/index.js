import React, { useState, useEffect } from "react";
import { Table, Tag, Button, Spin } from "antd";
import { gethistory } from "../../Server/Transaction_historyAPI";

const Transaction = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Lấy dữ liệu giao dịch
  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const data = await gethistory();
        setTransactions(data || []);
        setLoading(false);
      } catch (err) {
        console.error("Lỗi khi lấy lịch sử giao dịch:", err);
        setError("Không thể tải lịch sử giao dịch. Vui lòng thử lại sau.");
        setLoading(false);
      }
    };

    fetchTransactions();
  }, []);

  const filteredTransactions = transactions.filter(
    (tx) => tx.TrangThai === "Đã thanh toán"
  );

  const columns = [
    {
      title: "Mã giao dịch",
      dataIndex: "MaDonHang",
      key: "MaDonHang",
    },
    {
      title: "Ngày tạo",
      dataIndex: "NgayTao",
      key: "NgayTao",
      render: (date) => new Date(date).toLocaleString("vi-VN"),
    },
    {
      title: "Số tiền",
      dataIndex: "SoTien",
      key: "SoTien",
      render: (amount) => `${amount.toLocaleString()} VND`,
    },
    {
      title: "Tên tài khoản",
      dataIndex: "Tentaikhoan",
      key: "Tentaikhoan",
    },
    {
      title: "Trạng thái",
      dataIndex: "TrangThai",
      key: "TrangThai",
      render: (status) => <Tag color="green">{status}</Tag>, // Giao dịch đã thanh toán
    },
  ];
  if (loading) {
    return (
      <Spin size="large" style={{ display: "block", margin: "50px auto" }} />
    );
  }
  if (error) {
    return <p style={{ textAlign: "center", color: "red" }}>{error}</p>;
  }
  return (
    <Table
      dataSource={filteredTransactions}
      columns={columns}
      rowKey="MaDonHang"
    />
  );
};

export default Transaction;
