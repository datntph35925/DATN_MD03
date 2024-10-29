import React, { useState } from "react";
import { Modal, Button, Form, Input } from "antd";

const AddProductModal = ({ visible, onAdd, onCancel }) => {
  const [form] = Form.useForm();

  // Handle form submission
  const handleAddProduct = () => {
    form
      .validateFields()
      .then((values) => {
        onAdd(values); // Pass the form values to the parent component
        form.resetFields(); // Reset form fields after submission
      })
      .catch((info) => {
        console.log("Validate Failed:", info);
      });
  };

  return (
    <Modal
      visible={visible}
      title="Thêm Sản phẩm"
      onOk={handleAddProduct}
      onCancel={onCancel}
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="name"
          label="Product Name"
          rules={[
            { required: true, message: "Please input the product name!" },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="price"
          label="Price"
          rules={[{ required: true, message: "Please input the price!" }]}
        >
          <Input type="number" />
        </Form.Item>
        <Form.Item
          name="quantity"
          label="Quantity"
          rules={[{ required: true, message: "Please input the quantity!" }]}
        >
          <Input type="number" />
        </Form.Item>
        <Form.Item
          name="image"
          label="Image URL"
          rules={[{ required: true, message: "Please input the image URL!" }]}
        >
          <Input />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddProductModal;
