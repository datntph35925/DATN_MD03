import React from "react";
import { Modal, Form, Input, Select, DatePicker, message } from "antd";
import { addVoucher } from "../../Server/Vouchers";

function AddVoucherModal({ isVisible, onOk, onCancel }) {
  const [form] = Form.useForm();

  const handleOk = async () => {
    try {
      const values = await form.validateFields();

      const response = await addVoucher(values);
      if (response) {
        message.success("Voucher đã được thêm thành công!");
        form.resetFields();
        // Pass the new voucher data to the parent component
        onOk(response); // Assuming `response` is the new voucher data returned by the server
      }
    } catch (error) {
      if (error.response) {
        console.error("API Error:", error.response.data);
      } else {
        console.error("Error:", error.message);
      }
      message.error("Lỗi khi thêm voucher. Vui lòng thử lại!");
    }
  };

  return (
    <Modal
      title="Thêm Voucher Mới"
      open={isVisible}
      onOk={handleOk}
      onCancel={onCancel}
      okText="Lưu"
      cancelText="Hủy"
    >
      <Form
        form={form}
        layout="vertical"
        name="add_voucher_form"
        initialValues={{
          MaVoucher: "",
          GiaTri: "",
          LoaiVoucher: "",
          NgayBatDau: "",
          NgayKetThuc: "",
        }}
      >
        <Form.Item
          name="MaVoucher"
          label="Mã Voucher"
          rules={[{ required: true, message: "Vui lòng nhập mã voucher!" }]}
        >
          <Input placeholder="Nhập mã voucher" />
        </Form.Item>

        <Form.Item
          name="GiaTri"
          label="Giá Trị"
          rules={[
            { required: true, message: "Vui lòng nhập giá trị voucher!" },
            { pattern: /^\d+$/, message: "Giá trị phải là số hợp lệ!" },
          ]}
        >
          <Input placeholder="Nhập giá trị (số tiền hoặc % giảm giá)" />
        </Form.Item>

        <Form.Item
          name="LoaiVoucher"
          label="Loại Voucher"
          rules={[{ required: true, message: "Vui lòng chọn loại voucher!" }]}
        >
          <Select placeholder="Chọn loại voucher">
            <Select.Option value="Giảm giá theo %">
              Giảm giá theo %
            </Select.Option>
            <Select.Option value="Giảm giá cố định">
              Giảm giá cố định
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="NgayBatDau"
          label="Ngày Bắt Đầu"
          rules={[{ required: true, message: "Vui lòng chọn ngày bắt đầu!" }]}
        >
          <DatePicker
            style={{ width: "100%" }}
            format="YYYY-MM-DD"
            placeholder="Chọn ngày bắt đầu"
          />
        </Form.Item>

        <Form.Item
          name="NgayKetThuc"
          label="Ngày Kết Thúc"
          rules={[{ required: true, message: "Vui lòng chọn ngày kết thúc!" }]}
        >
          <DatePicker
            style={{ width: "100%" }}
            format="YYYY-MM-DD"
            placeholder="Chọn ngày kết thúc"
          />
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default AddVoucherModal;
