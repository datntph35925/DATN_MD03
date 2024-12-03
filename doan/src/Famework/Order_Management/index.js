import React, { useState, useEffect } from "react";
import { Table, Tag, Button, Space, message, Select } from "antd";
import OrderDetailModal from "../../Modal/MoldechitietDH";
import { getListOrders, updateOrderList } from "../../Server/Order"; // Import API

const Order_Management = () => {
  const [orders, setOrders] = useState([]); // Initialize empty orders array
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [loading, setLoading] = useState(false); // Loading state
  const { Option } = Select;

  // Fetch order list on component mount
  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const fetchedOrders = await getListOrders();
      if (Array.isArray(fetchedOrders)) {
        setOrders(fetchedOrders);
      } else {
        console.error("The response data is not an array:", fetchedOrders);
      }
    } catch (error) {
      console.error("Error fetching order list:", error);
      message.error("Failed to fetch the order list.");
    } finally {
      setLoading(false);
    }
  };

  const updateOrderStatus = async (_id, newStatus) => {
    try {
      setLoading(true);
      await updateOrderList(_id, newStatus);
      message.success("Order status updated successfully!");

      // Refresh the order list to reflect the updated status
      setOrders((prevOrders) =>
        prevOrders.map((order) =>
          order._id === _id ? { ...order, TrangThai: newStatus } : order
        )
      );
    } catch (error) {
      console.error("Error updating order status:", error);
      message.error("Failed to update the order status.");
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: "Tên Người nhận",
      dataIndex: "TenNguoiNhan",
      key: "TenNguoiNhan",
    },
    {
      title: "Ngày Đặt hàng",
      dataIndex: "NgayDatHang",
      key: "NgayDatHang",
      render: (date) => new Date(date).toLocaleString(),
    },
    {
      title: "Phương thức thanh toán",
      dataIndex: "PhuongThucThanhToan",
      key: "PhuongThucThanhToan",
    },
    {
      title: "Trạng thái",
      dataIndex: "TrangThai",
      key: "TrangThai",
      render: (status) => {
        let color = "default";
        if (status === "Chờ xử lý") color = "orange";
        else if (status === "Đang giao") color = "blue";
        else if (status === "Đã giao") color = "green";
        else if (status === "Hủy") color = "red";

        return <Tag color={color}>{status}</Tag>;
      },
    },
    {
      title: "Tổng số lượng",
      dataIndex: "TongSoLuong",
      key: "TongSoLuong",
    },
    {
      title: "Tổng tiền",
      dataIndex: "TongTien",
      key: "TongTien",
      render: (amount) => `${amount.toLocaleString()} VND`,
    },
    {
      title: "Hành động",
      key: "actions",
      render: (text, record) => (
        <Space size="middle">
          <Button type="link" onClick={() => showModal(record)}>
            Xem Chi tiết
          </Button>
          <Select
            value={record.TrangThai} // Reflect the current status
            onChange={(newStatus) => updateOrderStatus(record._id, newStatus)}
            style={{ width: 120 }}
          >
            <Option value="Chờ xử lý">Chờ xử lý</Option>
            <Option value="Đang giao">Đang giao</Option>
            <Option value="Đã giao">Đã giao</Option>
            <Option value="Hủy">Hủy</Option>
          </Select>
        </Space>
      ),
    },
  ];

  const showModal = (order) => {
    setSelectedOrder(order);
    setIsModalVisible(true);
  };

  const handleCloseModal = () => {
    setIsModalVisible(false);
    setSelectedOrder(null);
  };

  return (
    <div>
      <h2>Quản lý Đơn hàng</h2>
      <Table
        columns={columns}
        dataSource={orders}
        rowKey="orderId"
        loading={loading}
      />
      <OrderDetailModal
        visible={isModalVisible}
        onClose={handleCloseModal}
        order={selectedOrder}
      />
    </div>
  );
};

export default Order_Management;
