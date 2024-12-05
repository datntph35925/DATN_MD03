var express = require('express');
var router = express.Router();
const PaymentAuthentications = require('../models/PaymentAuthentications');

router.get("/get-list-paymentauthentication", async (req, res) => {
    try {
        //lấy ds sản phẩm
        const data = await PaymentAuthentications.find().sort({ createdAt: -1 });
        if (data) {
            res.json({
                status: 200,
                messenger: "Lấy danh sách thành công",
                data: data,
            });
        } else {
            res.json({
                status: 400,
                messenger: "lấy danh sách thất bại",
                data: [],
            });
        }
    } catch (error) {
        console.log(error);
    }
});

router.post("/add-paymentauthentication", async (req, res) => {
    const { MaDonHang, Tentaikhoan, SoTien } = req.body;
    try {
        // Kiểm tra xem đã có thanh toán cho MaDonHang này chưa
        const existingPayment = await PaymentAuthentications.findOne({ MaDonHang });
        if (existingPayment) {
            return res.status(400).json({ message: 'Đơn hàng đã có thanh toán xác thực.' });
        }

        // Tạo một bản ghi thanh toán mới
        const newPayment = new PaymentAuthentications({
            MaDonHang,
            Tentaikhoan,
            SoTien,
            TrangThai: 'Chưa thanh toán'
        });

        await newPayment.save();
        res.status(201).json({
            success: true,
            message: 'Thanh toán đã được thêm thành công.',
            data: newPayment
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi thêm thanh toán.', error: error.message });
    }
});

router.put('/update-paymentauthentication-status/:id', async (req, res) => {
    const { id } = req.params; // Lấy id từ URL
    const { TrangThai } = req.body; // Lấy trạng thái mới từ request body

    try {
        const validStatuses = ['Chưa thanh toán', 'Đã thanh toán', 'Thất bại'];
        if (!validStatuses.includes(TrangThai)) {
            return res.status(400).json({
                status: 400,
                message: 'Trạng thái không hợp lệ.',
            });
        }

        const updatedPaymentAuthentications = await PaymentAuthentications.findByIdAndUpdate(
            id,
            { TrangThai }, 
            { new: true } 
        );

        if (!updatedPaymentAuthentications) {
            return res.status(404).json({
                status: 404,
                message: 'Không tìm thấy đơn hàng với ID đã cung cấp.',
            });
        }

        res.status(200).json({
            status: 200,
            message: 'Cập nhật trạng thái đơn hàng thành công.',
            data: updatedPaymentAuthentications, 
        });
    } catch (error) {
        console.error('Lỗi khi cập nhật trạng thái đơn hàng:', error);
        res.status(500).json({
            status: 500,
            message: 'Đã xảy ra lỗi khi cập nhật trạng thái đơn hàng.',
        });
    }
});

module.exports = router;