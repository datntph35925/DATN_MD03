import React, { useEffect, useState } from "react";
import {
  Modal,
  Button,
  Form,
  Input,
  Space,
  InputNumber,
  Upload,
  message,
} from "antd";
import { CloseOutlined, PlusOutlined } from "@ant-design/icons";

const AddProductModal = ({ visible, onAdd, onCancel, initialValues }) => {
  const [form] = Form.useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [fileList, setFileList] = useState([]);

  // Handle initial values and form reset
  useEffect(() => {
    if (initialValues) {
      const initialData = {
        ...initialValues,
        HinhAnh: initialValues.HinhAnh?.join(", ") || "",
      };
      form.setFieldsValue(initialData);
      setFileList(
          initialValues.HinhAnh?.map((url, index) => ({
            uid: `${index}`,
            name: `Image ${index + 1}`,
            status: "done",
            url,
          })) || []
      );
    } else {
      form.resetFields();
      setFileList([]);
    }
  }, [initialValues, form]);

  // Handle file upload changes
  const handleUploadChange = ({ fileList: newFileList }) => {
    setFileList(newFileList);
  };

  // Handle form submission
  const handleAddProduct = async () => {
    try {
      const values = await form.validateFields();
      setIsSubmitting(true);

      const formData = new FormData();

      Object.keys(values).forEach((key) => {
        if (key !== "HinhAnh") {
          formData.append(key, Array.isArray(values[key]) ? JSON.stringify(values[key]) : values[key]);
        }
      });

      fileList.forEach((file) => {
        if (file.originFileObj) {
          formData.append("HinhAnh", file.originFileObj);
        }
      });

      const manualUrls = values.HinhAnh?.split(",").map((url) => url.trim());
      if (manualUrls) {
        manualUrls.forEach((url) => formData.append("HinhAnh", url));
      }

      onAdd(formData);
      message.success(initialValues ? "Sản phẩm đã được cập nhật!" : "Sản phẩm đã được thêm mới!");
      form.resetFields();
      setFileList([]);
    } catch (error) {
      message.error("Vui lòng kiểm tra lại thông tin nhập!");
    } finally {
      setIsSubmitting(false);
    }
  };

  // Upload button for new file uploads
  const uploadButton = (
      <div>
        <PlusOutlined />
        <div style={{ marginTop: 8 }}>Upload</div>
      </div>
  );

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
                                {
                                  required: true,
                                  message: "Vui lòng nhập số lượng!",
                                },
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
              label="Upload Ảnh"
              extra="Bạn có thể tải lên ảnh hoặc nhập URL bên dưới."
          >
            <Upload
                listType="picture-card"
                fileList={fileList}
                onChange={handleUploadChange}
                beforeUpload={(file) => {
                  const isImage = file.type.startsWith("image/");
                  if (!isImage) {
                    message.error("Bạn chỉ có thể tải lên file ảnh!");
                    return Upload.LIST_IGNORE;
                  }
                  return false; // Ngăn không cho upload tự động
                }}
            >
              {fileList.length >= 8 ? null : uploadButton}
            </Upload>

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
