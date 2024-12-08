import React from "react";

const vouchers = [
  {
    id: 1,
    name: "Voucher A",
    code: "SAVE10",
    discount: "10%",
    expiryDate: "2024-12-31",
  },
  {
    id: 2,
    name: "Voucher B",
    code: "OFF20",
    discount: "20%",
    expiryDate: "2024-11-30",
  },
  {
    id: 3,
    name: "Voucher C",
    code: "DISCOUNT50",
    discount: "50%",
    expiryDate: "2025-01-15",
  },
];

function VoucherList() {
  return (
    <div style={{ padding: "20px" }}>
      <h2>Danh Sách Voucher</h2>
      <div style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
        {vouchers.map((voucher) => (
          <div
            key={voucher.id}
            style={{
              border: "1px solid #ccc",
              borderRadius: "8px",
              padding: "10px",
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <div>
              <h3>{voucher.name}</h3>
              <p>
                <strong>Mã giảm giá:</strong> {voucher.code}
              </p>
              <p>
                <strong>Giảm giá:</strong> {voucher.discount}
              </p>
              <p>
                <strong>Hạn sử dụng:</strong> {voucher.expiryDate}
              </p>
            </div>
            <button
              style={{
                padding: "8px 16px",
                borderRadius: "5px",
                backgroundColor: "#28a745",
                color: "white",
                border: "none",
                cursor: "pointer",
              }}
            >
              Áp dụng
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default VoucherList;
