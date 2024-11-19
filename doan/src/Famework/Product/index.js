import React, { useState, useEffect } from "react";
import { Table, Button, Image, message, Modal } from "antd";
import AddProductModal from "../../Modal/ModalAddProduct";
import { getProduct, deleteProductById } from "../../Server/ProductsApi.js"; // Adjust the import path as necessary

const Products = () => {
  const [products, setProducts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isAddModalVisible, setIsAddModalVisible] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const { confirm } = Modal;
  // Fetch products from the API
  useEffect(() => {
    const fetchProducts = async () => {
      setIsLoading(true);
      try {
        const response = await getProduct(); // Lấy sản phẩm từ API
        console.log("product", response);

        if (response.status === 200) {
          const data = response.data.data.map((product) => ({
            ...product,
            key: product._id, // Sử dụng _id làm key
            TenSP: product.TenSP,
            ThuongHieu: product.ThuongHieu,
            GiaBan: product.GiaBan,
            HinhAnh: product.HinhAnh[0] || [], // Lấy ảnh đầu tiên trong mảng
            soLuongTon: product.KichThuoc.reduce(
              (total, size) => total + size.soLuongTon,
              0
            ),
            KichThuoc: product.KichThuoc.map((size) => ({
              size: size.size,
              soLuongTon: size.soLuongTon,
            })), // Lưu trữ kích thước và số lượng tồn
            MoTa: product.MoTa, // Mô tả sản phẩm để hiển thị trong expanded row
          }));
          setProducts(data); // Cập nhật sản phẩm vào state
        } else {
          message.error("Lấy danh sách sản phẩm không thành công!");
        }
      } catch (error) {
        message.error("Đã có lỗi xảy ra khi lấy sản phẩm!");
        console.error("Error fetching products:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Handle adding and editing products
  const handleAddProduct = (product) => {
    if (editingProduct) {
      setProducts(
        products.map((p) =>
          p.key === editingProduct.key ? { ...editingProduct, ...product } : p
        )
      );
    } else {
      const newProduct = { ...product, key: products.length + 1 };
      setProducts([...products, newProduct]);
    }
    setIsAddModalVisible(false);
    setEditingProduct(null);
  };

  // Handle deleting product
  const handleDelete = (key) => {
    const productToDelete = products.find((item) => item.key === key);

    confirm({
      title: "Bạn có chắc chắn muốn xóa sản phẩm này không?",
      content: `Tên sản phẩm: ${productToDelete.TenSP}`,
      okText: "Xóa",
      okType: "danger",
      cancelText: "Hủy",
      onOk: async () => {
        setDeleteLoading(true);
        try {
          await deleteProductById(productToDelete._id); // Gọi API để xóa sản phẩm
          setProducts(products.filter((item) => item.key !== key)); // Cập nhật lại danh sách sản phẩm
          message.success("Sản phẩm đã được xóa thành công!");
        } catch (error) {
          message.error("Đã xảy ra lỗi khi xóa sản phẩm!");
        } finally {
          setDeleteLoading(false);
        }
      },
      onCancel() {
        console.log("Hủy xóa sản phẩm");
      },
    });
  };

  // Modal visibility functions
  const showAddProductModal = () => {
    setIsAddModalVisible(true);
    setEditingProduct(null);
  };

  const showEditProductModal = (product) => {
    setEditingProduct(product);
    setIsAddModalVisible(true);
  };

  const handleCancel = () => {
    setIsAddModalVisible(false);
    setEditingProduct(null);
  };

  // Define columns for the Table
  const columns = [
    { title: "Tên Sản Phẩm", dataIndex: "TenSP", key: "name" },
    {
      title: "Hình Ảnh",
      dataIndex: "HinhAnh",
      key: "HinhAnh",
      render: (HinhAnh) => <Image width={50} src={HinhAnh} />,
    },
    {
      title: "Thương Hiệu",
      dataIndex: "ThuongHieu",
      key: "ThuongHieu",
    },
    {
      title: "Giá Bán",
      dataIndex: "GiaBan",
      key: "GiaBan",
      render: (GiaBan) => `${GiaBan.toLocaleString()} VND`, // Định dạng giá tiền
    },
    { title: "Số Lượng Tồn", dataIndex: "soLuongTon", key: "soLuongTon" },

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
    <div className="container_addProduct">
      <Button
        type="primary"
        style={{ marginBottom: "16px" }}
        onClick={showAddProductModal}
      >
        Thêm Sản phẩm
      </Button>

      <Table
        columns={columns}
        dataSource={products}
        loading={isLoading}
        rowKey="key" // Ensure Ant Design can uniquely identify rows
        expandable={{
          expandedRowRender: (record) => (
            <div>
              <h4>Kích thước & Số lượng:</h4>
              <ul>
                {record.KichThuoc.map((size, index) => (
                  <li key={index}>
                    <strong> Size :{size.size}:</strong> {size.soLuongTon} còn
                  </li>
                ))}
              </ul>
              <h4>Mô tả sản phẩm:</h4>
              <p>{record.MoTa}</p>
            </div>
          ),
          rowExpandable: (record) => record.name !== "Not Expandable", // Điều kiện mở rộng dòng (có thể thay đổi)
        }}
      />

      <AddProductModal
        visible={isAddModalVisible}
        onAdd={handleAddProduct}
        onCancel={handleCancel}
        initialValues={editingProduct} // Pass initial values for editing
      />
    </div>
  );
};

export default Products;
