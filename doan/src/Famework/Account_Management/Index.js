import React, { useState, useEffect } from "react";
import {
  Table,
  Button,
  Space,
  Popconfirm,
  Modal,
  Descriptions,
  message,
} from "antd";
import { getListAccount, deleteAccounts } from "../../Server/account_api"; // Adjust the path to your API functions

const Account_Management = () => {
  const [customers, setCustomers] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [loading, setLoading] = useState(false);

  // Fetch customers from the API
  useEffect(() => {
    const fetchAccounts = async () => {
      setLoading(true);
      try {
        const data = await getListAccount();

        setCustomers(
          data.map((customer) => ({
            key: customer._id,
            Matk: customer.Matk,
            Tentaikhoan: customer.Tentaikhoan,
            Hoten: customer.Hoten,
            // Append .jpg to image filenames
            Anhtk: customer.Anhtk
              ? `/uploads/${customer.Anhtk}.jpg`
              : "/uploads/default-avatar.jpg", // Default fallback image
          }))
        );
      } catch (error) {
        message.error("Failed to fetch customer accounts");
        console.error("Error fetching customer accounts:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAccounts();
  }, []);

  const columns = [
    {
      title: "Account ID",
      dataIndex: "Matk",
      key: "Matk",
    },
    {
      title: "Email",
      dataIndex: "Tentaikhoan",
      key: "Tentaikhoan",
    },
    {
      title: "Name",
      dataIndex: "Hoten",
      key: "Hoten",
    },
    {
      title: "Avatar",
      dataIndex: "Anhtk",
      key: "Anhtk",
      render: (text) => (
        <img
          src={text}
          alt="avatar"
          style={{ width: 50, height: 50, borderRadius: "50%" }}
        />
      ),
    },
    {
      title: "Actions",
      key: "actions",
      render: (text, record) => (
        <Space size="middle">
          <Button type="link" onClick={() => handleViewDetails(record)}>
            View Details
          </Button>
          <Popconfirm
            title="Are you sure to delete this customer?"
            onConfirm={() => handleDelete(record.key)} // Call delete with the account ID
            okText="Yes"
            cancelText="No"
          >
            <Button type="link" danger>
              Delete
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  // Delete customer handler
  const handleDelete = async (accountId) => {
    setLoading(true); // Set loading state to true during API call
    try {
      await deleteAccounts(accountId);
      setCustomers(customers.filter((customer) => customer.key !== accountId));
      message.success("Customer deleted successfully");
    } catch (error) {
      message.error("Failed to delete customer");
      console.error("Error deleting customer:", error);
    } finally {
      setLoading(false); // Reset loading state
    }
  };

  // Open modal and set selected customer
  const handleViewDetails = (customer) => {
    setSelectedCustomer(customer);
    setIsModalVisible(true);
  };

  // Close modal
  const handleCancel = () => {
    setIsModalVisible(false);
    setSelectedCustomer(null);
  };

  return (
    <div>
      <h2>Customer Management</h2>
      <Table
        columns={columns}
        dataSource={customers}
        rowKey="key"
        loading={loading}
      />
      <Modal
        visible={isModalVisible}
        title="Customer Details"
        onCancel={handleCancel}
        footer={[
          <Button key="close" onClick={handleCancel}>
            Close
          </Button>,
        ]}
      >
        {selectedCustomer && (
          <Descriptions column={1}>
            <Descriptions.Item label="Account ID">
              {selectedCustomer.Matk}
            </Descriptions.Item>
            <Descriptions.Item label="Email">
              {selectedCustomer.Tentaikhoan}
            </Descriptions.Item>
            <Descriptions.Item label="Name">
              {selectedCustomer.Hoten}
            </Descriptions.Item>
            <Descriptions.Item label="Avatar">
              <img
                src={selectedCustomer.Anhtk}
                alt="avatar"
                style={{ width: 100, borderRadius: "10px" }}
              />
            </Descriptions.Item>
          </Descriptions>
        )}
      </Modal>
    </div>
  );
};

export default Account_Management;
