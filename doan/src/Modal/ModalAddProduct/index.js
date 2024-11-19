import React, { useState } from "react";
import { Modal, Button, Form, Input, Space, message, InputNumber } from "antd";
import { CloseOutlined } from "@ant-design/icons";
import { addProduct } from "../../Server/ProductsApi"; // Điều chỉnh đường dẫn nếu cần thiết

const AddProductModal = ({ visible, onAdd, onCancel }) => {
  const [form] = Form.useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Xử lý khi gửi form
  const handleAddProduct = async () => {
    try {
      // Xác minh các trường dữ liệu
      const values = await form.validateFields();
      console.log(values);

      // Kiểm tra và xử lý giá trị GiaBan
      const giaBan = parseFloat(values.GiaBan);
      if (isNaN(giaBan)) {
        throw new Error("Giá bán không hợp lệ");
      }

      const productData = {
        MaSanPham: values.MaSanPham, // Đảm bảo key đúng với backend
        TenSP: values.TenSP,
        GiaBan: giaBan, // Chuyển sang kiểu số
        ThuongHieu: values.Thuonghieu,
        KichThuoc: values.KichThuoc.map((item) => ({
          size: item.Size, // Chuyển từ Size sang size
          soLuongTon: parseInt(item.Quantity, 10), // Chuyển số lượng sang số nguyên, từ Quantity sang soLuongTon
        })),
        HinhAnh: Array.isArray(values.HinhAnh)
          ? values.HinhAnh
          : [values.HinhAnh], // Đảm bảo là mảng
        MoTa: values.MoTa,
      };

      // Đặt trạng thái đang gửi
      setIsSubmitting(true);

      // Gọi API để thêm sản phẩm
      const response = await addProduct(productData);

      if (response.status === 200) {
        message.success("Sản phẩm đã được thêm thành công!");
        onAdd(response.data.data); // Thông báo cho thành phần cha về dữ liệu sản phẩm mới
        form.resetFields(); // Xóa các trường sau khi thêm thành công
      } else {
        message.error("Không thể thêm sản phẩm, vui lòng thử lại!");
      }
    } catch (error) {
      message.error(
        "Đã xảy ra lỗi: " + (error.message || "Kiểm tra lại thông tin!")
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal
      visible={visible}
      title="Thêm Sản phẩm"
      onOk={handleAddProduct}
      onCancel={onCancel}
      confirmLoading={isSubmitting} // Hiển thị trạng thái tải khi gửi
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="MaSanPham"
          label="Mã sản phẩm"
          rules={[{ required: true, message: "Vui lòng nhập Mã sản phẩm!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="TenSP"
          label="Tên sản phẩm"
          rules={[{ required: true, message: "Vui lòng nhập tên sản phẩm!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="GiaBan"
          label="Giá bán"
          rules={[{ required: true, message: "Vui lòng nhập giá bán!" }]}
        >
          <Input type="number" />
        </Form.Item>
        <Form.Item
          name="Thuonghieu"
          label="Thương hiệu"
          rules={[{ required: true, message: "Vui lòng nhập thương hiệu!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item label="Kích thước">
          <Form.List name="KichThuoc">
            {(fields, { add, remove }) => (
              <>
                {fields.map(({ key, name }) => (
                  <Space key={key} style={{ display: "flex", marginBottom: 8 }}>
                    <Form.Item
                      name={[name, "Size"]}
                      rules={[
                        { required: true, message: "Vui lòng nhập size!" },
                      ]}
                    >
                      <InputNumber
                        placeholder="Kích thước"
                        min={1} // Giới hạn giá trị tối thiểu
                        style={{ width: "100%" }} // Đảm bảo kích thước đẹp
                      />
                    </Form.Item>
                    <Form.Item
                      name={[name, "Quantity"]}
                      rules={[
                        { required: true, message: "Vui lòng nhập số lượng!" },
                      ]}
                    >
                      <InputNumber
                        placeholder="Số lượng"
                        min={1} // Giới hạn giá trị tối thiểu
                        style={{ width: "100%" }}
                      />
                    </Form.Item>
                    <CloseOutlined onClick={() => remove(name)} />
                  </Space>
                ))}
                <Button type="dashed" onClick={() => add()} block>
                  + Thêm kích thước
                </Button>
              </>
            )}
          </Form.List>
        </Form.Item>

        <Form.Item
          name="HinhAnh"
          label="Ảnh sản phẩm"
          rules={[{ required: true, message: "Vui lòng nhập URL ảnh!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="MoTa"
          label="Mô tả sản phẩm"
          rules={[{ required: true, message: "Vui lòng nhập mô tả sản phẩm!" }]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddProductModal;
