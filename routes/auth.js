const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const CustomerAccounts = require('../models/CustomerAccounts');

// Route đăng ký
router.post('/register', async (req, res) => {
    try {
        const { Tentaikhoan, Hoten, Matkhau, Anhtk } = req.body;

        // Kiểm tra định dạng email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(Tentaikhoan)) {
            return res.status(400).json({ message: 'Tên tài khoản phải là một địa chỉ email hợp lệ' });
        }

        // Kiểm tra xem tên tài khoản đã tồn tại chưa
        const existingUser = await CustomerAccounts.findOne({ Tentaikhoan });
        if (existingUser) {
            return res.status(400).json({ message: 'Tên tài khoản đã tồn tại' });
        }

        // Mã hóa mật khẩu
        const hashedPassword = await bcrypt.hash(Matkhau, 10);

        // Tạo mã tài khoản tự động dựa trên seq
        const lastUser = await CustomerAccounts.findOne().sort({ seq: -1 });
        const accountNumber = `AC${(lastUser ? lastUser.seq + 1 : 1).toString().padStart(3, '0')}`;

        // Tạo tài khoản mới
        const newUser = new CustomerAccounts({
            Matk: accountNumber,
            Tentaikhoan,
            Hoten,
            Matkhau: hashedPassword,
            Anhtk: Anhtk || '', // Nếu không có ảnh đại diện, để trống
            seq: lastUser ? lastUser.seq + 1 : 1 // Tăng giá trị seq
        });

        // Lưu người dùng vào cơ sở dữ liệu
        await newUser.save();

        res.status(201).json({ message: 'Đăng ký thành công' });
    } catch (err) {
        console.error("Lỗi khi lưu vào cơ sở dữ liệu:", err);
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: err });
    }
});


// Route đăng nhập
router.post('/login', async (req, res) => {
    try {
        const { Tentaikhoan, Matkhau } = req.body;

        // Tìm tài khoản theo tên tài khoản
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) return res.status(400).json({ message: 'Tài khoản không tồn tại' });

        // Kiểm tra mật khẩu
        const isMatch = await bcrypt.compare(Matkhau, user.Matkhau);
        if (!isMatch) return res.status(400).json({ message: 'Mật khẩu không đúng' });

        // Tạo JWT token
        const token = jwt.sign({ id: user._id }, 'your_jwt_secret', { expiresIn: '1h' });

        res.status(200).json({ message: 'Đăng nhập thành công', token });
    } catch (err) {
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: err.message });
    }
});

//  Route để hiển thị danh sách tài khoản
router.get('/list-accounts', async (req, res) => {
    try {
        // Lấy danh sách tài khoản từ cơ sở dữ liệu
        const accounts = await CustomerAccounts.find({}, { Matkhau: 0 }); // Loại bỏ mật khẩu để đảm bảo an toàn

        res.status(200).json(accounts);
    } catch (err) {
        console.error("Lỗi khi lấy danh sách tài khoản:", err);
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: err });
    }
});

// Route xóa tài khoản
router.delete('/delete-account/:id', async (req, res) => {
    try {
        const accountId = req.params.id;

        // Kiểm tra xem tài khoản có tồn tại không
        const account = await CustomerAccounts.findById(accountId);
        if (!account) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Xóa tài khoản
        await CustomerAccounts.findByIdAndDelete(accountId);

        res.status(200).json({ message: 'Xóa tài khoản thành công' });
    } catch (err) {
        console.error("Lỗi khi xóa tài khoản:", err);
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: err });
    }
});
// Route cập nhật tài khoản
router.put('/update-account/:id', async (req, res) => {
    try {
        const accountId = req.params.id;
        const { Tentaikhoan, Hoten, Matkhau, Anhtk } = req.body;

        // Tìm tài khoản cần cập nhật
        const account = await CustomerAccounts.findById(accountId);
        if (!account) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Kiểm tra định dạng email (nếu có thay đổi Tentaikhoan)
        if (Tentaikhoan) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(Tentaikhoan)) {
                return res.status(400).json({ message: 'Tên tài khoản phải là một địa chỉ email hợp lệ' });
            }
        }

        // Mã hóa mật khẩu mới (nếu có)
        let hashedPassword = account.Matkhau; // Giữ nguyên mật khẩu cũ
        if (Matkhau) {
            hashedPassword = await bcrypt.hash(Matkhau, 10);
        }

        // Cập nhật thông tin tài khoản (bỏ qua kiểm tra trùng)
        account.Tentaikhoan = Tentaikhoan || account.Tentaikhoan;
        account.Hoten = Hoten || account.Hoten;
        account.Matkhau = hashedPassword;
        account.Anhtk = Anhtk || account.Anhtk;

        // Lưu thay đổi vào cơ sở dữ liệu
        await account.save();

        res.status(200).json({ message: 'Cập nhật tài khoản thành công', account });
    } catch (err) {
        console.error("Lỗi khi cập nhật tài khoản:", err);
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: err });
    }
});


module.exports = router;
