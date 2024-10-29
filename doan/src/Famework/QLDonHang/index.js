import React, { useState } from "react";
import { Table, Tag, Button, Space, Popconfirm } from "antd";

const QuanLyDonHang = () => {
  const [orders, setOrders] = useState([
    {
      key: "1",
      orderId: "1001",
      customerName: "Nguyễn Văn A",
      orderDate: "2023-10-01",
      status: "Chờ xử lý",
      totalAmount: "4,500,000 VND",
    },
    {
      key: "2",
      orderId: "1002",
      customerName: "Lê Thị B",
      orderDate: "2023-10-05",
      status: "Đã gửi",
      totalAmount: "3,000,000 VND",
    },
  ]);

  const columns = [
    {
      title: "Mã Đơn hàng",
      dataIndex: "orderId",
      key: "orderId",
    },
    {
      title: "Tên Khách hàng",
      dataIndex: "customerName",
      key: "customerName",
    },
    {
      title: "Ngày Đặt hàng",
      dataIndex: "orderDate",
      key: "orderDate",
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      render: (status) => (
        <Tag color={status === "Đã gửi" ? "green" : "orange"}>{status}</Tag>
      ),
    },
    {
      title: "Tổng tiền",
      dataIndex: "totalAmount",
      key: "totalAmount",
    },
    {
      title: "Hành động",
      key: "actions",
      render: (text, record) => (
        <Space size="middle">
          <Button type="link">Xem Chi tiết</Button>
          <Button type="link">Cập nhật Trạng thái</Button>
          <Popconfirm
            title="Bạn có chắc muốn hủy đơn hàng này không?"
            onConfirm={() => handleCancel(record.key)}
            okText="Có"
            cancelText="Không"
          >
            <Button type="link" danger>
              Hủy
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const handleCancel = (key) => {
    setOrders(orders.filter((order) => order.key !== key));
  };

  return (
    <div>
      <h2>Quản lý Đơn hàng</h2>
      <Table columns={columns} dataSource={orders} rowKey="key" />
    </div>
  );
};

export default QuanLyDonHang;
