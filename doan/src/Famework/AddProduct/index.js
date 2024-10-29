import React, { useState } from "react";
import { Table, Button, Image } from "antd";
import AddProductModal from "../../MoldeItem/MoldeAddProduct"; // Import the modal component

const Products = () => {
  // State for product data
  const [products, setProducts] = useState([
    {
      key: "1",
      name: "Product 1",
      price: 100,
      quantity: 10,
      image: "https://via.placeholder.com/100", // Placeholder image
    },
    {
      key: "2",
      name: "Product 2",
      price: 200,
      quantity: 20,
      image: "https://via.placeholder.com/100",
    },
    {
      key: "3",
      name: "Product 3",
      price: 300,
      quantity: 15,
      image: "https://via.placeholder.com/100",
    },
  ]);

  // State for controlling the Add Product modal
  const [isAddModalVisible, setIsAddModalVisible] = useState(false);

  // Function to handle adding a product
  const handleAddProduct = (product) => {
    const newProduct = {
      ...product,
      key: products.length + 1, // Assign a unique key for new product
    };
    setProducts([...products, newProduct]); // Add the new product to the list
    setIsAddModalVisible(false); // Close the modal after adding the product
  };

  // Function to delete a product
  const handleDelete = (key) => {
    setProducts(products.filter((item) => item.key !== key));
  };

  // Function to show the Add Product modal
  const showAddProductModal = () => {
    setIsAddModalVisible(true);
  };

  // Function to handle cancel in modal
  const handleCancel = () => {
    setIsAddModalVisible(false);
  };

  // Table columns
  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Image",
      dataIndex: "image",
      key: "image",
      render: (image) => <Image width={50} src={image} />, // Display image
    },
    {
      title: "Price",
      dataIndex: "price",
      key: "price",
      render: (price) => `$${price}`, // Format price
    },
    {
      title: "Quantity",
      dataIndex: "quantity",
      key: "quantity",
    },
    {
      title: "Actions",
      key: "action",
      render: (text, record) => (
        <>
          <Button type="link" onClick={() => handleDelete(record.key)} danger>
            Delete
          </Button>
        </>
      ),
    },
  ];

  return (
    <div className="container_addProduct">
      {/* Button to open the modal */}
      <Button
        type="primary"
        style={{ marginBottom: "16px" }}
        onClick={showAddProductModal}
      >
        Thêm Sản phẩm
      </Button>

      {/* Table showing products */}
      <Table columns={columns} dataSource={products} />

      {/* Add Product Modal */}
      <AddProductModal
        visible={isAddModalVisible}
        onAdd={handleAddProduct}
        onCancel={handleCancel}
      />
    </div>
  );
};

export default Products;
