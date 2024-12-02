import React, { useState, useEffect } from "react";
import { Table, Tag, Button, Space, Popconfirm } from "antd";
import OrderDetailModal from "../../Modal/MoldechitietDH";
import { getListOrders } from "../../Server/Order"; // Import API

// Quản lý đơn hàng
const Order_Management = () => {
  const [orders, setOrders] = useState([]); // Khởi tạo mảng đơn hàng trống
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);

  // Fetch danh sách đơn hàng khi component mount
  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const fetchedOrders = await getListOrders(); // Lấy danh sách đơn hàng từ API

        // Kiểm tra nếu fetchedOrders là một mảng hợp lệ
        if (Array.isArray(fetchedOrders)) {
          setOrders(fetchedOrders); // Cập nhật đơn hàng từ API
        } else {
          console.error("Dữ liệu trả về không phải là mảng:", fetchedOrders);
        }
      } catch (error) {
        console.error("Lỗi khi lấy danh sách đơn hàng:", error);
      }
    };

    fetchOrders();
  }, []); // Chạy chỉ khi component mount

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
          <Button type="link" onClick={() => showModal(record)}>
            Xem Chi tiết
          </Button>
          <Button type="link">Cập nhật Trạng thái</Button>
          <Popconfirm
            title="Bạn có chắc muốn hủy đơn hàng này không?"
            onConfirm={() => handleCancelOrder(record.key)}
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

  const showModal = (order) => {
    setSelectedOrder(order);
    setIsModalVisible(true);
  };

  const handleCancelOrder = (key) => {
    setOrders(orders.filter((order) => order.key !== key));
  };

  const handleCloseModal = () => {
    setIsModalVisible(false);
    setSelectedOrder(null);
  };

  return (
    <div>
      <h2>Quản lý Đơn hàng</h2>
      <Table columns={columns} dataSource={orders} rowKey="key" />

      <OrderDetailModal
        visible={isModalVisible}
        onClose={handleCloseModal}
        order={selectedOrder}
      />
    </div>
  );
};

export default Order_Management;
