import React, { useState, useEffect } from "react";
import { Table, Button, message } from "antd";
import AddVoucherModal from "../../Modal/ModalAddVoucher";
import { getlistVouchers } from "../../Server/Vouchers"; // Import the delete API function
import "./index.scss";

function VoucherList() {
  const [vouchers, setVouchers] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);

  // Fetch vouchers from API
  useEffect(() => {
    const fetchVouchers = async () => {
      try {
        const data = await getlistVouchers();
        console.log(data); // Log the API response
        setVouchers(data); // Set vouchers state with API data
      } catch (error) {
        console.error("Error fetching vouchers:", error);
      }
    };

    fetchVouchers();
  }, []); // Empty dependency array ensures it runs once on mount

  const handleAddVoucherClick = () => {
    setIsModalVisible(true);
  };

  const handleModalOk = (newVoucher) => {
    // Add the new voucher to the list of vouchers
    setVouchers((prevVouchers) => [...prevVouchers, newVoucher]);
    setIsModalVisible(false);
    alert("Voucher đã được thêm!");
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
  };

  // Handle the delete functionality
  // const handleDeleteVoucher = async (id) => {
  //   try {
  //     const response = await deleteVoucher(id);
  //     if (response.success) {
  //       // Remove the deleted voucher from the state
  //       setVouchers((prevVouchers) =>
  //         prevVouchers.filter((voucher) => voucher.id !== id)
  //       );
  //       message.success("Voucher đã được xóa thành công!");
  //     } else {
  //       message.error("Lỗi khi xóa voucher!");
  //     }
  //   } catch (error) {
  //     console.error("Error deleting voucher:", error);
  //     message.error("Lỗi khi xóa voucher. Vui lòng thử lại!");
  //   }
  // };

  // Define table columns
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
      render: (GiaTri, record) => {
        if (record.LoaiVoucher === "Giảm giá cố định") {
          return `${GiaTri.toLocaleString("vi-VN")} VND`;
        } else if (record.LoaiVoucher === "Giảm giá theo %") {
          return `${GiaTri}%`;
        }
        return "N/A";
      },
    },
    {
      title: "Hạn Sử Dụng",
      dataIndex: "NgayKetThuc",
      key: "NgayKetThuc",
      render: (date) => new Date(date).toLocaleDateString("vi-VN"), // Định dạng ngày tháng năm
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
            style={{
              backgroundColor: "#28a745",
              borderColor: "#28a745",
              marginRight: 8,
            }}
          >
            Áp dụng
          </Button>
          <Button type="danger">Xóa</Button>
        </>
      ),
    },
  ];

  return (
    <div className="voucher-container">
      <header className="voucher-header">
        <h2>Danh Sách Voucher</h2>
      </header>
      <Button type="primary" onClick={handleAddVoucherClick}>
        Thêm Voucher
      </Button>
      {/* Ant Design Table */}
      <Table
        dataSource={vouchers}
        columns={columns}
        rowKey="id" // Ensure that the rowKey corresponds to the unique identifier
        pagination={{ pageSize: 5 }}
      />

      {/* Modal Component */}
      <AddVoucherModal
        isVisible={isModalVisible}
        onOk={handleModalOk}
        onCancel={handleModalCancel}
      />
    </div>
  );
}

export default VoucherList;
