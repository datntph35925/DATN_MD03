import React, { useEffect, useState } from "react";
import { Modal, Button, Form, Input, Space, InputNumber, message } from "antd";
import { CloseOutlined } from "@ant-design/icons";

const AddProductModal = ({ visible, onAdd, onCancel, initialValues }) => {
  const [form] = Form.useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Set giá trị ban đầu hoặc reset form khi `initialValues` thay đổi
  useEffect(() => {
    if (initialValues) {
      const initialData = {
        ...initialValues,
        HinhAnh: initialValues.HinhAnh?.join(", ") || "",
      };
      form.setFieldsValue(initialData);
    } else {
      form.resetFields();
    }
  }, [initialValues, form]);

  // Xử lý sự kiện khi người dùng nhấn "OK" để thêm/sửa sản phẩm
  const handleAddProduct = async () => {
    try {
      const values = await form.validateFields();
      setIsSubmitting(true);

      // Chuẩn hóa dữ liệu trước khi gửi
      const formattedValues = {
        ...values,
        HinhAnh: values.HinhAnh
          ? values.HinhAnh.split(",").map((url) => url.trim())
          : [],
        KichThuoc:
          values.KichThuoc?.map((item) => ({
            size: item?.size || 0,
            soLuongTon: parseInt(item?.soLuongTon || "0", 10),
          })) || [],
      };

      onAdd(formattedValues); // Gửi dữ liệu đến callback
      message.success(
        initialValues
          ? "Sản phẩm đã được cập nhật thành công!"
          : "Sản phẩm đã được thêm thành công!"
      );
      form.resetFields();
    } catch (error) {
      message.error("Vui lòng kiểm tra lại thông tin nhập!");
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
          rules={[{ required: true, message: "Vui lòng nhập Tên sản phẩm!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="GiaBan"
          label="Giá bán"
          rules={[{ required: true, message: "Vui lòng nhập Giá bán!" }]}
        >
          <InputNumber min={0} style={{ width: "100%" }} />
        </Form.Item>
        <Form.Item
          name="ThuongHieu"
          label="Thương hiệu"
          rules={[{ required: true, message: "Vui lòng nhập Thương hiệu!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item label="Kích thước">
          <Form.List name="KichThuoc">
            {(fields, { add, remove }) => (
              <>
                {fields.map(({ key, name, ...restField }) => (
                  <Space
                    key={key}
                    style={{ display: "flex", marginBottom: 8 }}
                    align="baseline"
                  >
                    <Form.Item
                      {...restField}
                      name={[name, "size"]}
                      rules={[
                        {
                          required: true,
                          message: "Vui lòng nhập kích thước!",
                        },
                      ]}
                    >
                      <InputNumber placeholder="Kích thước" min={1} />
                    </Form.Item>
                    <Form.Item
                      {...restField}
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
          label="Ảnh sản phẩm (URL cách nhau bằng dấu phẩy)"
          rules={[{ required: true, message: "Vui lòng nhập URL ảnh!" }]}
        >
          <Input placeholder="Nhập URL ảnh" />
        </Form.Item>
        <Form.Item
          name="MoTa"
          label="Mô tả sản phẩm"
          rules={[{ required: true, message: "Vui lòng nhập Mô tả sản phẩm!" }]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddProductModal;
