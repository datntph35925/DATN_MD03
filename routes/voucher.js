const express = require('express');
const router = express.Router();
const Vouchers = require('../models/Vouchers');

// Lấy danh sách tất cả voucher
router.get('/get-list-vouchers', async (req, res) => {
    try {
        const vouchers = await Vouchers.find();
        res.status(200).json({
            status: 200,
            message: 'Lấy danh sách voucher thành công',
            data: vouchers
        });
    } catch (error) {
        console.error('Lỗi khi lấy danh sách voucher:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server khi lấy danh sách voucher'
        });
    }
});

//lấy danh sách voucher cho Tentaikhoan

// Lấy danh sách voucher theo Tentaikhoan từ req.params
router.get('/get-list-vouchers-acount/:Tentaikhoan?', async (req, res) => {
    try {
        // Lấy Tentaikhoan từ params
        const { Tentaikhoan } = req.params;

        // Kiểm tra nếu Tentaikhoan được cung cấp
        let vouchers;
        if (Tentaikhoan) {
            vouchers = await Vouchers.find({ UsedBy: Tentaikhoan }); // Tìm voucher dựa trên Tentaikhoan
        } else {
            vouchers = await Vouchers.find(); // Lấy tất cả voucher nếu không có Tentaikhoan
        }

        res.status(200).json({
            status: 200,
            message: 'Lấy danh sách voucher thành công',
            data: vouchers
        });
    } catch (error) {
        console.error('Lỗi khi lấy danh sách voucher:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server khi lấy danh sách voucher'
        });
    }
});


// Thêm một voucher mới
router.post('/add-vouchers', async (req, res) => {
    try {
        const { MaVoucher, GiaTri, LoaiVoucher, NgayBatDau, NgayKetThuc } = req.body;

        // Tạo một voucher mới
        const newVoucher = new Vouchers({
            MaVoucher,
            GiaTri,
            LoaiVoucher,
            NgayBatDau,
            NgayKetThuc
        });

        const savedVoucher = await newVoucher.save();
        res.status(201).json({
            status: 201,
            message: 'Thêm voucher thành công',
            data: savedVoucher
        });
    } catch (error) {
        console.error('Lỗi khi thêm voucher:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server khi thêm voucher'
        });
    }
});

// Cập nhật thông tin voucher
router.put('/update-vouchers/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const { MaVoucher, GiaTri, LoaiVoucher, NgayBatDau, NgayKetThuc, TrangThai } = req.body;

        const updatedVoucher = await Vouchers.findByIdAndUpdate(
            id,
            { MaVoucher, GiaTri, LoaiVoucher, NgayBatDau, NgayKetThuc, TrangThai },
            { new: true } // Trả về tài liệu sau khi cập nhật
        );

        if (!updatedVoucher) {
            return res.status(404).json({
                status: 404,
                message: 'Không tìm thấy voucher với ID đã cung cấp'
            });
        }

        res.status(200).json({
            status: 200,
            message: 'Cập nhật voucher thành công',
            data: updatedVoucher
        });
    } catch (error) {
        console.error('Lỗi khi cập nhật voucher:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server khi cập nhật voucher'
        });
    }
});

// Xóa một voucher
router.delete('/delete-vouchers/:id', async (req, res) => {
    try {
        const { id } = req.params;

        const deletedVoucher = await Vouchers.findByIdAndDelete(id);

        if (!deletedVoucher) {
            return res.status(404).json({
                status: 404,
                message: 'Không tìm thấy voucher với ID đã cung cấp'
            });
        }

        res.status(200).json({
            status: 200,
            message: 'Xóa voucher thành công',
            data: deletedVoucher
        });
    } catch (error) {
        console.error('Lỗi khi xóa voucher:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server khi xóa voucher'
        });
    }
});

module.exports = router;
