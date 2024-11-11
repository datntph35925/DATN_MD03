const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const CustomerAccounts = require('../models/CustomerAccounts');

const nodemailer = require('nodemailer');

// Cấu hình tài khoản Gmail của bạn để gửi email
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'datnmd03@gmail.com', // Email của bạn dùng để gửi
        pass: 'nudl miju myuy wafp'         // Mật khẩu ứng dụng
    }
});


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

// Route gửi mã đặt lại mật khẩu
router.post('/send-reset-password-email', async (req, res) => {
    const { Tentaikhoan } = req.body;
    try {
        // Tìm người dùng theo email
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tạo JWT token
        const token = jwt.sign({ id: user._id }, 'namanhdepzaivippromax2604@#!%', { expiresIn: '15m' }); // Token có hiệu lực 15 phút

        // Tạo liên kết đặt lại mật khẩu
        const resetLink = `http://localhost:3000/reset-password.html?token=${token}`;

        // Cấu hình email
        const mailOptions = {
            from: 'datnmd03@gmail.com',
            to: Tentaikhoan,
            subject: 'Đặt lại mật khẩu của bạn',
            html: `<p>Nhấp vào liên kết sau để đặt lại mật khẩu:</p>
                   <a href="${resetLink}">Đặt lại mật khẩu</a>
                   <p>Liên kết này có hiệu lực trong 15 phút.</p>`
        };

        // Gửi email
        await transporter.sendMail(mailOptions);
        res.status(200).json({ message: 'Email đặt lại mật khẩu đã được gửi' });
    } catch (error) {
        console.error('Lỗi khi gửi email:', error);
        res.status(500).json({ message: 'Lỗi khi gửi email', error });
    }
});
// Route xử lý yêu cầu đặt lại mật khẩu
router.post('/reset-password', async (req, res) => {
    const { token, newPassword } = req.body;
    try {
        // Xác thực token và xử lý lỗi
        const decoded = jwt.verify(token, 'namanhdepzaivippromax2604@#!%');
        const userId = decoded.id;

        // Mã hóa mật khẩu mới
        const hashedPassword = await bcrypt.hash(newPassword, 10);

        // Cập nhật mật khẩu trong cơ sở dữ liệu
        await CustomerAccounts.findByIdAndUpdate(userId, { Matkhau: hashedPassword });

        res.status(200).json({ message: 'Mật khẩu đã được đặt lại thành công' });
    } catch (error) {
        // Xử lý các lỗi liên quan đến token
        if (error.name === 'TokenExpiredError') {
            return res.status(400).json({ message: 'Token đã hết hạn' });
        } else if (error.name === 'JsonWebTokenError') {
            return res.status(400).json({ message: 'Token không hợp lệ' });
        } else {
            return res.status(400).json({ message: 'Có lỗi xảy ra với token' });
        }
    }
});


module.exports = router;
