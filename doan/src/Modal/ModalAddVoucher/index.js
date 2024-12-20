import React, { useEffect } from "react";
import { Modal, Form, Input, Select, DatePicker, message } from "antd";
import moment from "moment";
import { addVoucher, updateVoucher } from "../../Server/Vouchers";
import { CheckCircleOutlined } from "@ant-design/icons";

function AddVoucherModal({ isVisible, onOk, onCancel, voucher }) {
  const [form] = Form.useForm();

  useEffect(() => {
    if (voucher) {
      form.setFieldsValue({
        ...voucher,
        NgayBatDau: voucher.NgayBatDau ? moment(voucher.NgayBatDau) : null,
        NgayKetThuc: voucher.NgayKetThuc ? moment(voucher.NgayKetThuc) : null,
        TrangThai: voucher.TrangThai || null,
      });
    } else {
      form.resetFields();
    }
  }, [voucher, form]);

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      const formattedValues = {
        ...values,
        NgayBatDau: values.NgayBatDau ? values.NgayBatDau.toISOString() : null,
        NgayKetThuc: values.NgayKetThuc
          ? values.NgayKetThuc.toISOString()
          : null,
      };

      let response;
      if (voucher) {
        response = await updateVoucher(voucher._id, formattedValues);
      } else {
        response = await addVoucher(formattedValues);
      }

      if (response?.success) {
        console.log("Voucher added successfully");
        message.success(
          voucher ? "Cập nhật voucher thành công!" : "Thêm voucher thành công!"
        );
        form.resetFields();
        onOk(response);
      } else {
        console.log("Error response", response);
        message.success(
          response?.message || "Lỗi không xác định! Vui lòng thử lại."
        );
      }
    } catch (error) {
      console.error("Lỗi xử lý voucher:", error.message || error);
      message.error(
        `Lỗi: ${error.message || "Không thể xử lý yêu cầu. Vui lòng thử lại!"}`
      );
    } finally {
      onCancel(); // Đóng modal trong mọi trường hợp
    }
  };

  const handleCancel = () => {
    form.resetFields();
    onCancel();
  };

  return (
    <Modal
      title={voucher ? "Chỉnh sửa Voucher" : "Thêm Voucher Mới"}
      open={isVisible}
      onOk={handleOk}
      onCancel={handleCancel}
      okText={voucher ? "Cập nhật" : "Lưu"}
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
          TrangThai: "Có thể sử dụng",
          NgayBatDau: null,
          NgayKetThuc: null,
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
          name="TrangThai"
          label="Trạng Thái"
          rules={[{ required: true, message: "Vui lòng chọn trạng thái!" }]}
        >
          <Select placeholder="Chọn trạng thái">
            <Select.Option value="Có thể sử dụng">Có thể sử dụng</Select.Option>
            <Select.Option value="Không thể sử dụng">
              Không thể sử dụng
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
            format="DD-MM-YYYY"
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
            format="DD-MM-YYYY"
            placeholder="Chọn ngày kết thúc"
          />
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default AddVoucherModal;
