import React, { useEffect, useState } from "react";
import { Modal, Button, Form, Input, Space, InputNumber, message } from "antd";
import { CloseOutlined } from "@ant-design/icons";

const AddProductModal = ({ visible, onAdd, onCancel, initialValues }) => {
  const [form] = Form.useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Set giá trị ban đầu khi mở modal
  useEffect(() => {
    if (initialValues) {
      form.setFieldsValue(initialValues);
    } else {
      form.resetFields();
    }
  }, [initialValues, form]);

  const handleAddProduct = async () => {
    try {
      const values = await form.validateFields();
      setIsSubmitting(true);

      // Chuẩn hóa dữ liệu trước khi gửi
      const formattedValues = {
        ...values,
        KichThuoc: values.KichThuoc.map((item) => ({
          size: item.size,
          soLuongTon: parseInt(item.soLuongTon, 10),
        })),
      };

      onAdd(formattedValues);
      message.success(
        initialValues
          ? "Sản phẩm đã được cập nhật!"
          : "Sản phẩm đã được thêm thành công!"
      );
      form.resetFields();
    } catch (error) {
      message.error("Vui lòng kiểm tra lại thông tin!");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal
      visible={visible}
      title={initialValues ? "Sửa Sản phẩm" : "Thêm Sản phẩm"}
      onOk={handleAddProduct}
      onCancel={onCancel}
      confirmLoading={isSubmitting}
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
          <InputNumber min={0} style={{ width: "100%" }} />
        </Form.Item>
        <Form.Item
          name="ThuongHieu"
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
                      name={[name, "size"]}
                      rules={[
                        { required: true, message: "Vui lòng nhập size!" },
                      ]}
                    >
                      <InputNumber placeholder="Kích thước" min={1} />
                    </Form.Item>
                    <Form.Item
                      name={[name, "soLuongTon"]}
                      rules={[
                        { required: true, message: "Vui lòng nhập số lượng!" },
                      ]}
                    >
                      <InputNumber placeholder="Số lượng" min={1} />
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
