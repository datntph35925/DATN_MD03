import React, { useState, useEffect } from "react";
import { Table, Button, message, Modal } from "antd";
import AddVoucherModal from "../../Modal/ModalAddVoucher";
import {
  getlistVouchers,
  deleteVoucher,
  updateVoucher,
  addVoucher,
} from "../../Server/Vouchers";
import "./index.scss";

const { confirm } = Modal;

function VoucherList() {
  const [vouchers, setVouchers] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingVoucher, setEditingVoucher] = useState(null); // Voucher đang chỉnh sửa

  // Fetch danh sách vouchers
  useEffect(() => {
    const fetchVouchers = async () => {
      try {
        const data = await getlistVouchers();
        setVouchers(data.map((voucher) => ({ ...voucher, key: voucher._id })));
      } catch (error) {
        console.error("Error fetching vouchers:", error);
        message.error(
          "Lỗi: Không thể tải danh sách voucher. Vui lòng thử lại sau!"
        );
      }
    };

    fetchVouchers();
  }, []);

  // Hiển thị modal thêm voucher
  const handleAddVoucherClick = () => {
    setEditingVoucher(null); // Reset voucher đang chỉnh sửa
    setIsModalVisible(true);
  };

  // Hiển thị modal chỉnh sửa voucher
  const handleEditVoucherClick = (voucher) => {
    setEditingVoucher(voucher);
    setIsModalVisible(true);
  };

  // Xử lý thêm hoặc cập nhật voucher
  const handleModalOk = async (updatedVoucher) => {
    if (!updatedVoucher || Object.keys(updatedVoucher).length === 0) {
      message.error("Lỗi: Dữ liệu không hợp lệ. Vui lòng kiểm tra và thử lại!");
      return;
    }

    try {
      if (editingVoucher) {
        // Cập nhật voucher
        const updated = await updateVoucher(editingVoucher._id, updatedVoucher);
        setVouchers((prevVouchers) =>
          prevVouchers.map((voucher) =>
            voucher._id === editingVoucher._id
              ? { ...updated, key: updated._id }
              : voucher
          )
        );
        message.success(`Cập nhật voucher "${updated.MaVoucher}" thành công!`);
      } else {
        // Thêm mới voucher
        const newVoucher = await addVoucher(updatedVoucher);
        setVouchers((prevVouchers) => [
          ...prevVouchers,
          { ...newVoucher, key: newVoucher._id },
        ]);
        message.success(
          `Thêm mới voucher "${newVoucher.MaVoucher}" thành công!`
        );
      }
    } catch (error) {
      console.error("Error adding/updating voucher:", error);
      message.error(
        `Lỗi: ${
          error.response?.data?.message ||
          "Không thể hoàn tất yêu cầu. Vui lòng thử lại!"
        }`
      );
    } finally {
      setIsModalVisible(false);
    }
  };

  // Đóng modal
  const handleModalCancel = () => setIsModalVisible(false);

  // Xóa voucher
  const handleDeleteVoucher = async (id) => {
    try {
      await deleteVoucher(id);
      setVouchers((prevVouchers) =>
        prevVouchers.filter((voucher) => voucher._id !== id)
      );
      message.success("Xóa voucher thành công!");
    } catch (error) {
      console.error("Error deleting voucher:", error);
      message.error("Lỗi: Không thể xóa voucher. Vui lòng thử lại!");
    }
  };

  // Hiển thị xác nhận xóa
  const showDeleteConfirm = (id) => {
    confirm({
      title: "Bạn có chắc chắn muốn xóa voucher này?",
      content: "Thao tác này sẽ xóa vĩnh viễn voucher và không thể hoàn tác.",
      okText: "Xác nhận",
      okType: "danger",
      cancelText: "Hủy",
      onOk() {
        handleDeleteVoucher(id);
      },
    });
  };

  // Cấu hình cột cho bảng
  const columns = [
    {
      title: "Mã Voucher",
      dataIndex: "MaVoucher",
      key: "MaVoucher",
    },
    {
      title: "Giảm Giá",
      dataIndex: "GiaTri",
      key: "GiaTri",
      render: (GiaTri, record) =>
        record.LoaiVoucher === "Giảm giá cố định"
          ? `${GiaTri.toLocaleString("vi-VN")} VND`
          : `${GiaTri}%`,
    },
    {
      title: "Hạn Sử Dụng",
      dataIndex: "NgayKetThuc",
      key: "NgayKetThuc",
      render: (date) => new Date(date).toLocaleDateString("vi-VN"),
    },
    {
      title: "Trạng thái",
      dataIndex: "TrangThai",
      key: "TrangThai",
    },
    {
      title: "Hành động",
      key: "action",
      render: (_, record) => (
        <>
          <Button
            type="primary"
            style={{ marginRight: 8 }}
            onClick={() => handleEditVoucherClick(record)}
          >
            Cập nhật
          </Button>
          <Button
            type="danger"
            style={{ background: "#FF0000" }}
            onClick={() => showDeleteConfirm(record._id)}
          >
            Xóa
          </Button>
        </>
      ),
    },
  ];

  return (
    <div className="voucher-container">
      <header className="voucher-header">
        <h2>Danh Sách Voucher</h2>
      </header>
      <Button
        type="primary"
        onClick={handleAddVoucherClick}
        style={{ marginBottom: 16 }}
      >
        Thêm Voucher
      </Button>
      <Table
        dataSource={vouchers}
        columns={columns}
        pagination={{ pageSize: 5 }}
      />
      <AddVoucherModal
        isVisible={isModalVisible}
        onOk={handleModalOk}
        onCancel={handleModalCancel}
        voucher={editingVoucher}
      />
    </div>
  );
}

export default VoucherList;
