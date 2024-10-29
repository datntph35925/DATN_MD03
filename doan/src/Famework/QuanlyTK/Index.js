import React, { useState } from "react";
import { Table, Button, Space, Popconfirm } from "antd";

const QLTaikhoan = () => {
  const [customers, setCustomers] = useState([
    {
      key: "1",
      name: "John Doe",
      email: "john.doe@example.com",
      phone: "123-456-7890",
      address: "123 Main St, City, Country",
    },
    {
      key: "2",
      name: "Jane Smith",
      email: "jane.smith@example.com",
      phone: "098-765-4321",
      address: "456 Maple Ave, City, Country",
    },
  ]);
  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Phone",
      dataIndex: "phone",
      key: "phone",
    },
    {
      title: "Address",
      dataIndex: "address",
      key: "address",
    },
    {
      title: "Actions",
      key: "actions",
      render: (text, record) => (
        <Space size="middle">
          <Button type="link">Edit</Button>
          <Popconfirm
            title="Are you sure to delete this customer?"
            onConfirm={() => handleDelete(record.key)}
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
  const handleDelete = (key) => {
    setCustomers(customers.filter((customer) => customer.key !== key));
  };

  return (
    <div>
      <h2>Customer Management</h2>
      <Table columns={columns} dataSource={customers} rowKey="key" />
    </div>
  );
};

export default QLTaikhoan;
