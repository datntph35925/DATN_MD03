import React, { useState, useEffect } from "react";
import {
  Table,
  Button,
  Image,
  message,
  Modal,
  Input,
  Spin,
  Tooltip,
  Rate,
} from "antd";
import { PlusOutlined } from "@ant-design/icons";
import AddProductModal from "../../Modal/ModalAddProduct"; // The modal for adding/editing products
import {
  addProduct,
  getProduct,
  deleteProductById,
  updateProductById,
} from "../../Server/ProductsApi"; // The API functions for products
import { getComment } from "../../Server/comment"; // API to get comments

const Products = () => {
  const [products, setProducts] = useState([]); // State to hold the product list
  const [isLoading, setIsLoading] = useState(false); // State to track loading status
  const [isAddModalVisible, setIsAddModalVisible] = useState(false); // State for showing modal
  const [editingProduct, setEditingProduct] = useState(null); // State to hold the product being edited
  const [searchTerm, setSearchTerm] = useState(""); // State for the search term
  const [loadingComments, setLoadingComments] = useState({}); // State to track loading status of comments
  const { confirm } = Modal; // Ant Design's Modal for confirming delete

  // Fetch products on component mount
  useEffect(() => {
    const fetchProducts = async () => {
      setIsLoading(true);
      try {
        const response = await getProduct();
        if (response.status === 200) {
          const data = response.data.data.map((product) => ({
            ...product,
            key: product._id,
            HinhAnh:
                Array.isArray(product.HinhAnh) && product.HinhAnh.length > 0
                    ? product.HinhAnh.map((url) =>
                        `http://160.191.50.148:3000/${url}` // Nối tiền tố vào đường dẫn tương đối
                    )
                    : [],
            soLuongTon: product.KichThuoc.reduce(
              (total, size) => total + size.soLuongTon,
              0
            ),
          }));
          console.log("Processed Products:", data);
          setProducts(data); // Set the fetched products to state
        } else {
          message.error("Lấy danh sách sản phẩm không thành công!");
        }
      } catch (error) {
        message.error(error.response?.data?.message || "Đã xảy ra lỗi!");
      } finally {
        setIsLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Handle row expand to load comments for a product
  const handleRowExpand = async (expanded, record) => {
    if (expanded && !record.comments) {
      setLoadingComments((prev) => ({ ...prev, [record._id]: true })); // Mark as loading
      try {
        const comments = await getComment(record._id); // Fetch comments for the product
        setProducts((prevProducts) =>
          prevProducts.map((product) =>
            product._id === record._id ? { ...product, comments } : product
          )
        );
      } catch (error) {
        message.error("Không thể tải bình luận!");
      } finally {
        setLoadingComments((prev) => ({ ...prev, [record._id]: false })); // Mark as not loading
      }
    }
  };

  // Handle product add or edit
  const handleAddOrEditProduct = async (product) => {
    if (product.GiaBan < 1000) {
      message.error("Giá bán phải lớn hơn hoặc bằng 1,000 VND!");
      return;
    }
    setIsAddModalVisible(false);

    if (editingProduct) {
      try {
        const response = await updateProductById(editingProduct._id, product); // Update product if editing
        if (response.status === 200) {
          const updatedProduct = response.data;
          setProducts((prevProducts) =>
            prevProducts.map((p) =>
              p._id === updatedProduct._id
                ? { ...p, ...updatedProduct, key: updatedProduct._id }
                : p
            )
          );
          message.success("Sản phẩm đã được cập nhật!");
        }
      } catch (error) {
        message.error(error.response?.data?.message || "Cập nhật thất bại!");
      }
    } else {
      try {
        const response = await addProduct(product); // Add new product if not editing
        if (response.status === 201) {
          const newProduct = response.data;
          setProducts((prevProducts) => [
            ...prevProducts,
            { ...newProduct, key: newProduct._id },
          ]);
          message.success("Sản phẩm đã được thêm mới!");
        }
      } catch (error) {
        message.error(error.response?.data?.message || "Thêm mới thất bại!");
      }
    }

    setEditingProduct(null);
  };

  // Handle product deletion
  const handleDelete = (key) => {
    const productToDelete = products.find((item) => item.key === key);
    confirm({
      title: "Bạn có chắc chắn muốn xóa sản phẩm này không?",
      content: `Tên sản phẩm: ${productToDelete.TenSP}`,
      okText: "Xóa",
      okType: "danger",
      cancelText: "Hủy",
      onOk: async () => {
        try {
          await deleteProductById(productToDelete._id); // Delete the product
          setProducts((prevProducts) =>
            prevProducts.filter((item) => item.key !== key)
          );
          message.success("Sản phẩm đã được xóa thành công!");
        } catch (error) {
          message.error("Đã xảy ra lỗi khi xóa sản phẩm!");
        }
      },
    });
  };

  // Show the add product modal
  const showAddProductModal = () => {
    setIsAddModalVisible(true);
    setEditingProduct(null); // Clear the editing product
  };

  // Show the edit product modal
  const showEditProductModal = (product) => {
    setEditingProduct(product);
    setIsAddModalVisible(true);
  };

  // Close the modal
  const handleCancel = () => {
    setIsAddModalVisible(false);
    setEditingProduct(null);
  };

  // Handle search functionality
  const handleSearch = (e) => {
    setSearchTerm(e.target.value.toLowerCase());
  };

  // Filter products based on search term
  const filteredProducts = products.filter((product) =>
    product.TenSP.toLowerCase().includes(searchTerm)
  );

  // Define the table columns
  const columns = [
    { title: "Tên Sản Phẩm", dataIndex: "TenSP", key: "name" },
    {
      title: "Hình Ảnh",
      dataIndex: "HinhAnh",
      key: "HinhAnh",
      render: (HinhAnh) => (
        <Image.PreviewGroup>
          <Image width={50} src={HinhAnh[0]} style={{ cursor: "pointer" }} />
        </Image.PreviewGroup>
      ),
    },
    { title: "Thương Hiệu", dataIndex: "ThuongHieu", key: "ThuongHieu" },
    {
      title: "Giá Bán",
      dataIndex: "GiaBan",
      key: "GiaBan",
      sorter: (a, b) => a.GiaBan - b.GiaBan,
      render: (GiaBan) => (GiaBan ? `${GiaBan.toLocaleString()}VND` : "N/A"),
    },
    { title: "Số lượng tổng", dataIndex: "soLuongTon", key: "soLuongTon" },
    {
      title: "Hành Động",
      key: "action",
      render: (text, record) => (
        <>
          <Button type="link" onClick={() => showEditProductModal(record)}>
            Sửa
          </Button>
          <Button type="link" onClick={() => handleDelete(record.key)} danger>
            Xóa
          </Button>
        </>
      ),
    },
  ];

  return (
    <div>
      <Input.Search
        placeholder="Tìm kiếm sản phẩm..."
        onChange={handleSearch}
        style={{ marginBottom: "16px" }}
      />
      <Button
        type="primary"
        style={{ marginBottom: "16px" }}
        onClick={showAddProductModal}
      >
        Thêm Sản Phẩm
      </Button>
      <Table
        columns={columns}
        dataSource={filteredProducts}
        loading={isLoading}
        rowKey="key"
        expandable={{
          onExpand: handleRowExpand,
          expandedRowRender: (record) => (
            <div>
              <h4>Kích thước & Số lượng:</h4>
              <ul>
                {record.KichThuoc.map((size, index) => (
                  <li key={index}>
                    <strong>Size {size.size}:</strong> {size.soLuongTon} còn
                  </li>
                ))}
              </ul>
              <h4>Mô tả sản phẩm:</h4>
              <p>{record.MoTa}</p>
              <h4>Danh sách Comment:</h4>
              {loadingComments[record._id] ? (
                <Spin />
              ) : record.comments && record.comments.length > 0 ? (
                <ul>
                  {record.comments.map((comment, index) => (
                    <li key={index}>
                      <p>
                        <strong>{comment.Tentaikhoan || "Anonymous"}:</strong>{" "}
                        {comment.BinhLuan}
                      </p>
                      <p>
                        <strong>Đánh giá:</strong>{" "}
                        <Rate disabled defaultValue={comment.DanhGia} />
                      </p>
                    </li>
                  ))}
                </ul>
              ) : (
                <p>Chưa có bình luận nào</p>
              )}
            </div>
          ),
        }}
      />
      <AddProductModal
        visible={isAddModalVisible}
        onAdd={handleAddOrEditProduct}
        onCancel={handleCancel}
        initialValues={editingProduct}
      />
    </div>
  );
};

export default Products;
