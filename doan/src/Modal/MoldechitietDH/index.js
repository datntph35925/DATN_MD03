import React from "react";
import { Modal, Descriptions, Table } from "antd";

const OrderDetailModal = ({ visible, onClose, order }) => {
  // Cột cho bảng sản phẩm trong đơn hàng
  const productColumns = [
    {
      title: "Tên Sản phẩm",
      dataIndex: "TenSP",
      key: "TenSP",
    },
    {
      title: "Số lượng",
      dataIndex: "SoLuongGioHang",
      key: "SoLuongGioHang",
    },
    {
      title: "Đơn giá",
      dataIndex: "Gia",
      key: "Gia",
      render: (Gia) => (Gia ? `${Gia.toLocaleString()} VND` : "N/A"),
    },
    {
      title: "Thành tiền",
      key: "TongTien",
      render: (_, record) =>
        `${(record.SoLuongGioHang * record.Gia).toLocaleString()} VND`,
    },
  ];

  return (
    <Modal
      title="Chi tiết Đơn hàng"
      visible={visible}
      onCancel={onClose}
      footer={null}
    >
      {order ? (
        <>
          {/* Thông tin chung của đơn hàng */}
          <Descriptions bordered column={1}>
            <Descriptions.Item label="Mã Đơn hàng">
              {order.orderId || "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Tên Người nhận">
              {order.TenNguoiNhan || "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Số điện thoại">
              {order.SoDienThoai || "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Địa chỉ giao hàng">
              {order.DiaChiGiaoHang || "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Ngày đặt hàng">
              {order.NgayDatHang
                ? new Date(order.NgayDatHang).toLocaleString()
                : "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Phương thức thanh toán">
              {order.PhuongThucThanhToan || "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Trạng thái">
              {order.TrangThai || "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Tổng số lượng">
              {order.TongSoLuong || "N/A"}
            </Descriptions.Item>
            <Descriptions.Item label="Tổng tiền">
              {order.TongTien
                ? `${order.TongTien.toLocaleString()} VND`
                : "N/A"}
            </Descriptions.Item>
          </Descriptions>

          {/* Danh sách sản phẩm */}
          <h3 style={{ marginTop: "16px" }}>Danh sách sản phẩm</h3>
          <Table
            columns={productColumns}
            dataSource={Array.isArray(order?.SanPham) ? order.SanPham : []}
            rowKey={(record) => record.productId || record.id || Math.random()}
            pagination={false}
          />
        </>
      ) : (
        <p>Không có thông tin chi tiết để hiển thị.</p>
      )}
    </Modal>
  );
};

export default OrderDetailModal;
