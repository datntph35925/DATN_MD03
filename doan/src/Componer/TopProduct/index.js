import React, { useEffect, useState } from "react";
import { Table, Image, Spin, Alert } from "antd";
import { getTopSellingProducts } from "../../Server/Order"; // Import API function

function TopProducts() {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(""); // Store error messages

    // useEffect to fetch data when the component mounts
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getTopSellingProducts(); // Call the API
                console.log(response); // Log the response to check structure

                if (response && response.length > 0) {
                    // Prepend the base URL to the image paths
                    const formattedData = response.map((data) => ({
                        ...data,
                        images:
                            data.images && Array.isArray(data.images)
                                ? data.images.map((url) => `http://160.191.50.148:3000/${url}`)
                                : [],
                    }));
                    setData(formattedData); // Set the fetched data to state
                } else {
                    setError("Không có sản phẩm bán chạy."); // Set error if no data found
                }
            } catch (error) {
                setError("Lỗi khi lấy dữ liệu sản phẩm bán chạy."); // Set error if API call fails
            } finally {
                setLoading(false); // Set loading to false once data is fetched
            }
        };

        fetchData();
    }, []); // Empty dependency array to run only once when the component mounts

    // Define the columns for the Ant Design Table
    const columns = [
        {
            title: "Xếp hạng",
            dataIndex: "rank",
            key: "rank",
            render: (text, record, index) => {
                const rank = index + 1; // Calculate rank
                const style = rank <= 3 ? { fontWeight: "bold", color: "#ff4d4f" } : {}; // Apply bold style for top 3
                return <span style={style}>{rank}</span>;
            },
        },
        {
            title: "Hình Ảnh",
            dataIndex: "images", // Use correct field name from response
            key: "images",
            render: (images) => (
                <>
                    {images && images.length > 0 ? (
                        <Image.PreviewGroup>
                            <Image
                                width={50}
                                src={images[0]} // Display the first image
                                style={{ cursor: "pointer" }}
                            />
                            {images.slice(1).map((url, index) => (
                                <Image key={index} src={url} style={{ display: "none" }} />
                            ))}
                        </Image.PreviewGroup>
                    ) : (
                        <div>
                            <span style={{ color: "red" }}>Không có hình ảnh</span>
                        </div>
                    )}
                </>
            ),
        },
        {
            title: "Tên sản phẩm",
            dataIndex: "name", // Use correct field name from response
            key: "name",
        },
        {
            title: "Số lượng bán",
            dataIndex: "quantitySold", // Use correct field name from response
            key: "quantitySold",
        },
        {
            title: "Giá bán (VND)",
            dataIndex: "price", // Use correct field name from response
            key: "price",
            render: (price) => price.toLocaleString("vi-VN"), // Format the price in Vietnamese currency
        },
    ];

    // If data is still loading, display a spinner
    if (loading) {
        return (
            <div style={{ textAlign: "center", marginTop: "20px" }}>
                <Spin size="large" />
            </div>
        );
    }

    // If there's an error, display an error message
    if (error) {
        return (
            <div style={{ textAlign: "center", marginTop: "20px" }}>
                <Alert message={error} type="error" />
            </div>
        );
    }

    return (
        <div>
            <h2>Top 10 Sản phẩm bán chạy</h2>
            <Table
                columns={columns}
                dataSource={data} // Pass the data to the Table
                rowKey="productId" // Ensure `productId` is unique for each row
                pagination={false} // Disable pagination (since it's top 10 products)
            />
        </div>
    );
}

export default TopProducts;
