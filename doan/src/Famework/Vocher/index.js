import React, { useState, useEffect } from "react";
import { Table, Button, message, Modal } from "antd";
import AddVoucherModal from "../../Modal/ModalAddVoucher";
import {
  getlistVouchers,
  deleteVoucher,
  addVoucher,
  updateVoucher,
} from "../../Server/Vouchers";
import "./index.scss";

const { confirm } = Modal;

function VoucherList() {
  const [vouchers, setVouchers] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingVoucher, setEditingVoucher] = useState(null);

  useEffect(() => {
    const fetchVouchers = async () => {
      try {
        const data = await getlistVouchers();
        setVouchers(data.map((voucher) => ({ ...voucher, key: voucher._id })));
      } catch (error) {
        message.error("Không thể tải danh sách voucher. Vui lòng thử lại!");
      }
    };

    fetchVouchers();
  }, []);

  const handleAddVoucherClick = () => {
    setEditingVoucher(null);
    setIsModalVisible(true);
  };

  const handleEditVoucherClick = (voucher) => {
    setEditingVoucher(voucher);
    setIsModalVisible(true);
  };

  const handleModalOk = async (updatedVoucher) => {
    if (!updatedVoucher || Object.keys(updatedVoucher).length === 0) {
      message.error("Dữ liệu không hợp lệ. Vui lòng kiểm tra và thử lại!");
      return;
    }

    try {
      if (editingVoucher) {
        const updated = await updateVoucher(editingVoucher._id, updatedVoucher);
        if (updated) {
          setVouchers((prevVouchers) =>
            prevVouchers.map((voucher) =>
              voucher._id === editingVoucher._id
                ? { ...updated, key: updated._id }
                : voucher
            )
          );
          message.success("Cập nhật voucher thành công!");
        } else {
          message.error("Không có thay đổi nào để cập nhật!");
        }
      } else {
        const newVoucher = await addVoucher(updatedVoucher);
        setVouchers((prevVouchers) => [
          ...prevVouchers,
          { ...newVoucher, key: newVoucher._id },
        ]);
        message.success("Thêm voucher mới thành công!");
      }
    } catch (error) {
      console.error("Error processing voucher:", error);
      message.error("Có lỗi xảy ra. Vui lòng thử lại sau.");
    } finally {

      setIsModalVisible(false);
      setEditingVoucher(null);
    }
  };

  const handleDeleteVoucher = async (id) => {
    try {
      await deleteVoucher(id);
      setVouchers((prevVouchers) =>
        prevVouchers.filter((voucher) => voucher._id !== id)
      );
      message.success("Xóa voucher thành công!");
    } catch (error) {
      console.error("Error deleting voucher:", error);
      message.error("Lỗi khi xóa voucher. Vui lòng thử lại!");
    }
  };

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
          {/*<Button*/}
          {/*  type="danger"*/}
          {/*  style={{ background: "#FF0000" }}*/}
          {/*  onClick={() => showDeleteConfirm(record._id)}*/}
          {/*>*/}
          {/*  Xóa*/}
          {/*</Button>*/}
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
        pagination={{ pageSize: 10 }}
      />
      <AddVoucherModal
        isVisible={isModalVisible}
        onOk={handleModalOk}
        onCancel={() => setIsModalVisible(false)}
        voucher={editingVoucher}
      />
    </div>
  );
}

export default VoucherList;
