import React, { useState } from "react";
import { Table, Tag, Button, Popconfirm, message } from "antd";

const TransactionHistory = () => {
  const [transactions, setTransactions] = useState([
    { id: "1", date: "2024-12-01", amount: 100, status: "Chờ xác minh" },
    { id: "2", date: "2024-12-02", amount: 200, status: "Đã xác minh" },
    { id: "3", date: "2024-12-03", amount: 150, status: "Thất bại" },
  ]);

  const updateStatus = (id, newStatus) => {
    setTransactions((prev) =>
      prev.map((tx) => (tx.id === id ? { ...tx, status: newStatus } : tx))
    );
    message.success("Trạng thái giao dịch đã được cập nhật!");
  };

  const columns = [
    {
      title: "Mã giao dịch",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "Ngày",
      dataIndex: "date",
      key: "date",
    },
    {
      title: "Số tiền",
      dataIndex: "amount",
      key: "amount",
      render: (amount) => `${amount.toLocaleString()} VND`,
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      render: (status) => {
        let color =
          status === "Chờ xác minh"
            ? "orange"
            : status === "Đã xác minh"
            ? "green"
            : "red";
        return <Tag color={color}>{status}</Tag>;
      },
    },
    {
      title: "Hành động",
      key: "action",
      render: (_, record) =>
        record.status === "Chờ xác minh" ? (
          <Popconfirm
            title="Bạn có chắc chắn muốn xác minh giao dịch này?"
            onConfirm={() => updateStatus(record.id, "Đã xác minh")}
            okText="Có"
            cancelText="Không"
          >
            <Button type="primary">Xác minh</Button>
          </Popconfirm>
        ) : (
          <Button type="default" disabled>
            Đã xác minh
          </Button>
        ),
    },
  ];

  return <Table dataSource={transactions} columns={columns} rowKey="id" />;
};

export default TransactionHistory;
