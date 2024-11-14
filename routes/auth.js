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
// Route gửi mã xác thực
router.post('/ma-xac-thuc', async (req, res) => {
    const { Tentaikhoan } = req.body;
    try {
        // Tìm người dùng theo email
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Kiểm tra nếu mã xác thực hiện tại vẫn tồn tại
        if (user.verificationCode) {
            return res.status(400).json({ message: 'Mã xác thực hiện tại vẫn còn hiệu lực, vui lòng thử lại sau' });
        }

        // Tạo mã xác thực 6 số
        const verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
        user.verificationCode = verificationCode;

        // Lưu mã xác thực vào cơ sở dữ liệu
        console.log('Mã xác thực được gán:', user.verificationCode);
        await user.save();
        console.log('Đã lưu mã xác thực vào cơ sở dữ liệu');

        // Sử dụng `setTimeout` để xóa mã sau 3 phút
        setTimeout(async () => {
            user.verificationCode = null; // Xóa mã xác thực
            await user.save();
            console.log(`Mã xác thực cho tài khoản ${Tentaikhoan} đã hết hạn và bị xóa.`);
        }, 3 * 60 * 1000); // 3 phút

        // Gửi email
        const mailOptions = {
            from: 'datnmd03@gmail.com',
            to: Tentaikhoan,
            subject: 'Mã xác thực Gmail của bạn',
            html: `<p>Mã xác thực của bạn là: <strong>${verificationCode}</strong></p>
                   <p>Mã này có hiệu lực trong 3 phút.</p>`
        };
        await transporter.sendMail(mailOptions);

        res.status(200).json({ message: 'Mã xác thực đã được gửi qua email' });
    } catch (error) {
        console.error('Lỗi khi gửi email:', error);
        res.status(500).json({ message: 'Lỗi khi gửi email', error });
    }
});
// Route xác thực mã
router.post('/xacthucma', async (req, res) => {
    const { Tentaikhoan, verificationCode } = req.body;
    try {
        // Tìm người dùng theo email
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Kiểm tra xem mã xác thực có tồn tại và chưa hết hạn
        if (!user.verificationCode) {
            return res.status(400).json({ message: 'Mã xác thực không tồn tại hoặc đã hết hạn' });
        }

        // Kiểm tra mã xác thực
        if (user.verificationCode !== verificationCode) {
            return res.status(400).json({ message: 'Mã xác thực không hợp lệ' });
        }

        // Mã xác thực hợp lệ
        user.verificationCode = null; // Xóa mã xác thực sau khi xác thực thành công
        await user.save();

        res.status(200).json({ message: 'Mã xác thực hợp lệ và đã được xác thực thành công' });
    } catch (error) {
        console.error('Lỗi khi xác thực mã:', error);
        res.status(500).json({ message: 'Lỗi khi xác thực mã', error });
    }
});

// Route gửi mã xác thực đến email mới
router.post('/ma-xac-thuc-email-moi', async (req, res) => {
    const { Tentaikhoan, EmailMoi } = req.body;

    // Kiểm tra xem `Tentaikhoan` và `EmailMoi` có dữ liệu không
    if (!Tentaikhoan || !EmailMoi) {
        return res.status(400).json({ message: 'Thiếu Tentaikhoan hoặc EmailMoi' });
    }

    console.log("Tentaikhoan received:", Tentaikhoan);
    console.log("EmailMoi received:", EmailMoi);

    try {
        // Tìm người dùng theo tên tài khoản (email hiện tại)
        const user = await CustomerAccounts.findOne({ Tentaikhoan: Tentaikhoan.toLowerCase() });
        if (!user) {
            console.log("User not found with Tentaikhoan:", Tentaikhoan);
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tạo mã xác thực 6 số
        const verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
        user.verificationCode = verificationCode;

        // Lưu mã xác thực vào cơ sở dữ liệu
        await user.save();
        console.log("Verification code saved:", verificationCode);

        // Sử dụng `setTimeout` để xóa mã sau 3 phút
        setTimeout(async () => {
            user.verificationCode = null; // Xóa mã xác thực
            user.EmailMoi = ''; // Đặt EmailMoi thành chuỗi rỗng
            await user.save();
            console.log(`Mã xác thực cho tài khoản ${Tentaikhoan} đã hết hạn và EmailMoi đã bị xóa.`);
        }, 3 * 60 * 1000); // 3 phút

        // Gửi email xác thực đến email mới
        const mailOptions = {
            from: 'datnmd03@gmail.com',
            to: EmailMoi,
            subject: 'Mã xác thực Gmail mới của bạn',
            html: `<p>Mã xác thực của bạn là: <strong>${verificationCode}</strong></p>
                   <p>Mã này có hiệu lực trong 3 phút.</p>`
        };

        // Gửi email và xử lý lỗi nếu xảy ra
        transporter.sendMail(mailOptions, (error, info) => {
            if (error) {
                console.error('Lỗi khi gửi email:', error);
                return res.status(500).json({ message: 'Lỗi khi gửi email', error });
            }
            console.log('Email sent:', info.response);
            res.status(200).json({ message: 'Mã xác thực đã được gửi đến email mới' });
        });

    } catch (error) {
        console.error('Lỗi khi gửi mã xác thực đến email mới:', error);
        res.status(500).json({ message: 'Lỗi khi gửi mã xác thực đến email mới', error });
    }
});


// Route để xác thực mã và cập nhật email mới nếu hợp lệ
router.post('/xacthucma-email-moi', async (req, res) => {
    const { Tentaikhoan, EmailMoi, verificationCode } = req.body;

    try {
        // Tìm người dùng theo tên tài khoản hiện tại (email hiện tại)
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Kiểm tra mã xác thực của người dùng
        if (user.verificationCode !== verificationCode) {
            return res.status(400).json({ message: 'Mã xác thực không hợp lệ hoặc đã hết hạn' });
        }

        // Cập nhật email mới
        user.Tentaikhoan = EmailMoi; // Cập nhật email mới
        user.verificationCode = null; // Xóa mã xác thực sau khi cập nhật thành công
        await user.save();

        res.status(200).json({ message: 'Email đã được cập nhật thành công' });
    } catch (error) {
        console.error('Lỗi khi xác thực mã và cập nhật email:', error);
        res.status(500).json({ message: 'Lỗi khi xác thực mã và cập nhật email', error });
    }
});



module.exports = router;
