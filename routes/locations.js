const express = require('express');
const router = express.Router();
const Location = require('../models/Location');
const CustomerAccounts = require('../models/CustomerAccounts');

// API lấy danh sách địa chỉ của người dùng theo ID tài khoản
router.get('/get-list-location-by-id/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: id });

        // Nếu không tìm thấy tài khoản
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        const data = await Location.find({ Tentaikhoan: userAccount.Tentaikhoan });

        if (data && data.length > 0) {
            res.json({
                status: 200,
                message: "Lấy danh sách địa chỉ thành công",
                data: data,
            });
        } else {
            res.json({
                status: 400,
                message: "Không có địa chỉ nào",
                data: [],
            });
        }
    } catch (error) {
        console.error('Lỗi:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server',
            error: error.message,
        });
    }
});

// API lấy danh sách tất cả các địa chỉ
router.get("/get-list-location", async (req, res) => {
    try {
        const data = await Location.find().sort({ createdAt: -1 });
        if (data) {
            res.json({
                status: 200,
                message: "Lấy danh sách địa chỉ thành công",
                data: data,
            });
        } else {
            res.json({
                status: 400,
                message: "Lấy danh sách địa chỉ thất bại",
                data: [],
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: 'Lỗi server', error: error.message });
    }
});

// API thêm địa chỉ giao hàng mới
router.post('/add-location/:userId', async (req, res) => {
    try {
        const { userId } = req.params;
        const { tenNguoiNhan, diaChi, sdt } = req.body;

        // Kiểm tra các thông tin đầu vào
        if (!tenNguoiNhan || !diaChi || !sdt) {
            return res.status(400).json({ message: 'Thiếu thông tin bắt buộc' });
        }

        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tạo địa chỉ mới
        const newLocation = new Location({
            Tentaikhoan: userId,
            tenNguoiNhan,
            diaChi,
            sdt,
        });

      
        // Lưu địa chỉ mới
        await newLocation.save();

        // Tạo thông báo
          const notification = new Notification({
            tentaikhoan: userId,
            title: 'Thêm địa chỉ mới',
            message: `Địa chỉ mới với người nhận là ${tenNguoiNhan} đã được thêm thành công.`,
            read: false,
        });
        await notification.save();

        res.status(200).json({ message: 'Địa chỉ được thêm thành công', location: newLocation });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Có lỗi xảy ra khi thêm địa chỉ', error: error.message });
    }
});

// API xóa một địa chỉ theo ID của tài khoản
router.delete('/remove-location/:userId', async (req, res) => {
    try {
        const { userId } = req.params;
        const { locationId } = req.body; // Lấy ID địa chỉ từ body

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tìm và xóa địa chỉ
        const location = await Location.findOneAndDelete({ _id: locationId, Tentaikhoan: userAccount.Tentaikhoan });
        if (!location) {
            return res.status(404).json({ message: 'Địa chỉ không tồn tại' });
        }

         // Tạo thông báo
         const notification = new Notification({
            tentaikhoan: userId,
            title: 'Xóa địa chỉ',
            message: `Địa chỉ với người nhận là ${location.tenNguoiNhan} đã được xóa.`,
            read: false,
        });
        await notification.save();

        res.status(200).json({ message: 'Địa chỉ đã được xóa thành công' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Có lỗi xảy ra khi xóa địa chỉ', error: error.message });
    }
});

// API cập nhật địa chỉ
router.put('/update-location/:userId', async (req, res) => {
    try {
        const { userId } = req.params;
        const { _id, tenNguoiNhan, diaChi, sdt } = req.body;

        // Kiểm tra các thông tin đầu vào
        if (!tenNguoiNhan || !diaChi || !sdt) {
            return res.status(400).json({ message: 'Thiếu thông tin bắt buộc' });
        }

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Cập nhật địa chỉ
        const updatedLocation = await Location.findOneAndUpdate(
            { _id: _id, Tentaikhoan: userAccount.Tentaikhoan },
            { tenNguoiNhan, diaChi, sdt },
            { new: true } // Trả về document sau khi cập nhật
        );

        if (!updatedLocation) {
            return res.status(404).json({ message: 'Địa chỉ không tồn tại' });
        }
        // Tạo thông báo
        const notification = new Notification({
            tentaikhoan: userId,
            title: 'Cập nhật địa chỉ',
            message: `Địa chỉ với người nhận là ${tenNguoiNhan} đã được cập nhật.`,
            read: false,
        });
        await notification.save();

        res.status(200).json({ message: 'Địa chỉ đã được cập nhật thành công', location: updatedLocation });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Có lỗi xảy ra khi cập nhật địa chỉ', error: error.message });
    }
});

module.exports = router;
