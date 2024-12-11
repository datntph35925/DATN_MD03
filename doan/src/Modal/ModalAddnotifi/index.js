import React, { useState } from "react";
import { Modal, Form, Input, Button, Select, notification } from "antd";
import { addNotification } from "../../Server/notify"; // Adjust API path

const ModalAddNotification = ({
  visible,
  onClose,
  onAddNotification,
  accounts = [],
}) => {
  const [loading, setLoading] = useState(false);
  const [sendToAll, setSendToAll] = useState(false); // State to track "Send to All"

  const handleAddNotification = async (values) => {
    setLoading(true);
    try {
      // Include all accounts if "Send to All" is checked
      const payload = sendToAll ? { ...values, account: "all" } : values;

      await addNotification(payload); // Call the API to add notification
      notification.success({
        message: "Thông báo",
        description: "Thông báo đã được thêm thành công!",
      });
      onAddNotification(payload); // Add notification to the list in the parent component
      onClose(); // Close the modal
    } catch (error) {
      notification.error({
        message: "Lỗi",
        description: "Đã xảy ra lỗi khi thêm thông báo!",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSelectChange = (value) => {
    // Toggle "Send to All" based on selection
    setSendToAll(value.includes("all"));
  };

  return (
    <Modal
      title="Thêm Thông Báo Mới"
      visible={visible}
      onCancel={onClose}
      footer={null}
    >
      <Form onFinish={handleAddNotification} layout="vertical">
        <Form.Item
          name="title"
          label="Tiêu đề"
          rules={[
            { required: true, message: "Vui lòng nhập tiêu đề thông báo!" },
          ]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          name="message"
          label="Nội dung Thông Báo"
          rules={[
            { required: true, message: "Vui lòng nhập nội dung thông báo!" },
          ]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>

        <Form.Item
          name="account"
          label="Chọn Tài Khoản"
          rules={[{ required: true, message: "Vui lòng chọn tài khoản!" }]}
        >
          <Select
            mode="multiple" // Allow selecting multiple accounts
            placeholder="Chọn tài khoản để gửi thông báo"
            onChange={handleSelectChange}
            optionLabelProp="label"
          >
            <Select.Option key="all" value="all" label="Gửi tất cả">
              Gửi tất cả
            </Select.Option>
            {accounts.map((account) => (
              <Select.Option
                key={account.id}
                value={account.id}
                label={account.name}
              >
                {account.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading} block>
            Thêm Thông Báo
          </Button>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default ModalAddNotification;
