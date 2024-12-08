import React, { useState, useEffect } from "react";
import { Table, Tag, Button, Modal, Spin, message } from "antd";
import {
  gethistory,
  updateTransactionStatus,
} from "../../Server/Transaction_historyAPI";

const TransactionHistory = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedTransaction, setSelectedTransaction] = useState(null);

  // Fetch transaction history data from API
  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        setLoading(true);
        const data = await gethistory();
        const filteredData = data.filter(
          (transaction) => transaction.TrangThai === "Chưa thanh toán"
        );

        const sanitizedData = filteredData.map((transaction) => ({
          ...transaction,
          SoTien: transaction.SoTien || 0,
          TrangThai: transaction.TrangThai || "Không xác định",
        }));
        setTransactions(sanitizedData || []);
      } catch (err) {
        console.error("Error fetching transaction history:", err);
        setError("Không thể tải lịch sử giao dịch. Vui lòng thử lại sau.");
      } finally {
        setLoading(false);
      }
    };

    fetchTransactions();
  }, []);

  // Update transaction status
  const handleUpdateStatus = async (_id, newStatus) => {
    try {
      setLoading(true);
      const response = await updateTransactionStatus(_id, newStatus);
      console.log("Updated transaction: ", response);

      if (response) {
        message.success(`Giao dịch ${_id} đã được cập nhật thành công!`);
        // Update transaction list after successful status update
        setTransactions((prevTransactions) =>
          prevTransactions.map((transaction) =>
            transaction._id === _id
              ? { ...transaction, TrangThai: newStatus }
              : transaction
          )
        );
      }
    } catch (error) {
      console.error("Error updating transaction status:", error);
      message.error("Không thể cập nhật trạng thái giao dịch.");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateClick = (record) => {
    setSelectedTransaction(record);
  };

  const handleConfirmUpdate = async () => {
    if (selectedTransaction) {
      await handleUpdateStatus(selectedTransaction._id, "Đã thanh toán");
      setSelectedTransaction(null);
    }
  };

  // Define columns for the transaction table
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
      render: (date) =>
        date ? new Date(date).toLocaleString("vi-VN") : "Không xác định",
    },
    {
      title: "Số tiền",
      dataIndex: "SoTien",
      key: "SoTien",
      render: (amount) =>
        amount ? `${amount.toLocaleString()} VND` : "Không xác định",
    },
    {
      title: "Tên tài khoản",
      dataIndex: "Tentaikhoan",
      key: "Tentaikhoan",
      render: (name) => name || "Không xác định",
    },
    {
      title: "Trạng thái",
      dataIndex: "TrangThai",
      key: "TrangThai",
      render: (Status) => (
        <Tag color={Status === "Đã thanh toán" ? "green" : "orange"}>
          {Status}
        </Tag>
      ),
    },
    {
      title: "Hành động",
      key: "action",
      render: (_, record) => (
        <Button
          type="primary"
          onClick={() => handleUpdateClick(record)}
          disabled={record.TrangThai === "Đã thanh toán"}
        >
          Xác nhận thanh toán
        </Button>
      ),
    },
  ];

  // Display loading spinner while fetching data
  if (loading) {
    return (
      <Spin size="large" style={{ display: "block", margin: "50px auto" }} />
    );
  }

  // Display error message if data fetching fails
  if (error) {
    return <p style={{ textAlign: "center", color: "red" }}>{error}</p>;
  }

  // Display transaction table and modal for transaction details
  return (
    <>
      <Table
        dataSource={transactions}
        columns={columns}
        rowKey={(record) => record._id || record.MaDonHang}
        locale={{ emptyText: "Không có dữ liệu" }}
      />

      <Modal
        title="Chi tiết giao dịch"
        visible={!!selectedTransaction}
        onOk={handleConfirmUpdate}
        onCancel={() => setSelectedTransaction(null)}
        okText="Xác nhận"
        cancelText="Hủy"
      >
        {selectedTransaction && (
          <>
            <p>
              Mã giao dịch: {selectedTransaction.MaDonHang || "Không xác định"}
            </p>
            <p>
              Số tiền:{" "}
              {selectedTransaction.SoTien
                ? `${selectedTransaction.SoTien.toLocaleString()} VND`
                : "Không xác định"}
            </p>
            <p>
              Trạng thái: {selectedTransaction.TrangThai || "Không xác định"}
            </p>
          </>
        )}
      </Modal>
    </>
  );
};

export default TransactionHistory;
