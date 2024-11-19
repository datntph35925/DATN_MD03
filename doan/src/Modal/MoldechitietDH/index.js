// OrderDetailModal.js
import React from "react";
import { Modal, Button } from "antd";

const OrderDetailModal = ({ visible, onClose, order }) => {
  return (
    <Modal
      title="Chi tiết Đơn hàng"
      visible={visible}
      onCancel={onClose}
      footer={[
        <Button key="close" onClick={onClose}>
          Đóng
        </Button>,
      ]}
    >
      {order && (
        <div>
          <p>
            <strong>Mã Đơn hàng:</strong> {order.orderId}
          </p>
          <p>
            <strong>Tên Khách hàng:</strong> {order.customerName}
          </p>
          <p>
            <strong>Ngày Đặt hàng:</strong> {order.orderDate}
          </p>
          <p>
            <strong>Trạng thái:</strong> {order.status}
          </p>
          <p>
            <strong>Tổng tiền:</strong> {order.totalAmount}
          </p>
        </div>
      )}
    </Modal>
  );
};

export default OrderDetailModal;
