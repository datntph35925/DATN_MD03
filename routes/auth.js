const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const CustomerAccounts = require('../models/CustomerAccounts');
const TemporaryVerificationCodes = require('../models/TemporaryVerificationCodes');
const multer = require('multer');
const path = require("path");
const nodemailer = require('nodemailer');
const Admin = require('../models/Admin'); 

// Cấu hình tài khoản Gmail của bạn để gửi email
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'datnmd03@gmail.com', // Email của bạn dùng để gửi
        pass: 'nudl miju myuy wafp'         // Mật khẩu ứng dụng
    }
});

// Gửi mã OTP khi đăng nhập
router.post('/dang-nhap-admin', async (req, res) => {
    const { username, password } = req.body;

    try {
        const admin = await Admin.findOne({ username });
        if (!admin) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại!' });
        }

        // Kiểm tra mật khẩu
        if (admin.password !== password) {
            return res.status(401).json({ message: 'Mật khẩu không chính xác!' });
        }

        // Tạo mã OTP
        const verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
        const codeExpiry = new Date(Date.now() + 3 * 60 * 1000); // Hết hạn sau 3 phút

        // Lưu mã OTP và thời gian hết hạn
        admin.verificationCode = verificationCode;
        admin.codeExpiry = codeExpiry;
        await admin.save();

        // Gửi email chứa mã OTP
        await transporter.sendMail({
            from: 'datnmd03@gmail.com',
            to: username,
            subject: 'Mã xác thực đăng nhập',
            text: `Mã xác thực của bạn là: ${verificationCode}. Mã này có hiệu lực trong 10 phút.`
        });

        res.json({ message: 'Mã xác thực đã được gửi tới email!' });
    } catch (err) {
        res.status(500).json({ message: 'Có lỗi xảy ra khi gửi mã xác thực!', error: err.message });
    }
});

// Xác thực mã OTP để hoàn tất đăng nhập
router.post('/xac-thuc-ma-dang-nhap-admin', async (req, res) => {
    const { username, verificationCode } = req.body;

    try {
        const admin = await Admin.findOne({ username });
        if (!admin) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại!' });
        }

        // Kiểm tra mã OTP và thời gian hết hạn
        if (admin.verificationCode !== verificationCode) {
            return res.status(401).json({ message: 'Mã xác thực không chính xác!' });
        }
        if (admin.codeExpiry < Date.now()) {
            return res.status(401).json({ message: 'Mã xác thực đã hết hạn!' });
        }

        // Xác thực thành công
        admin.verificationCode = null; // Xóa mã OTP sau khi sử dụng
        admin.codeExpiry = null;
        await admin.save();

        res.json({ message: 'Đăng nhập thành công!' });
    } catch (err) {
        res.status(500).json({ message: 'Có lỗi xảy ra khi xác thực mã!', error: err.message });
    }
});


// Gửi mã OTP để đổi mật khẩu
router.post('/gui-ma-doi-mat-khau-admin', async (req, res) => {
    const { username, oldPassword } = req.body;

    try {
        const admin = await Admin.findOne({ username });
        if (!admin) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại!' });
        }

        // Kiểm tra mật khẩu cũ
        if (admin.password !== oldPassword) {
            return res.status(401).json({ message: 'Mật khẩu cũ không chính xác!' });
        }

        // Tạo mã OTP
        const verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
        const codeExpiry = new Date(Date.now() + 3 * 60 * 1000); // Hết hạn sau 3 phút

        // Lưu mã OTP và thời gian hết hạn
        admin.verificationCode = verificationCode;
        admin.codeExpiry = codeExpiry;
        await admin.save();

        // Gửi email chứa mã OTP
        await transporter.sendMail({
            from: 'datnmd03@gmail.com',
            to: username,
            subject: 'Mã xác thực đổi mật khẩu',
            text: `Mã xác thực của bạn là: ${verificationCode}. Mã này có hiệu lực trong 3 phút.`
        });

        res.json({ message: 'Mã xác thực đã được gửi tới email!' });
    } catch (err) {
        res.status(500).json({ message: 'Có lỗi xảy ra khi gửi mã xác thực!', error: err.message });
    }
});

// Xác thực OTP và đổi mật khẩu
router.post('/xac-thuc-ma-doi-mat-khau-admin', async (req, res) => {
    const { username, verificationCode, newPassword } = req.body;

    try {
        const admin = await Admin.findOne({ username });
        if (!admin) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại!' });
        }

        // Kiểm tra mã OTP và thời gian hết hạn
        if (admin.verificationCode !== verificationCode) {
            return res.status(401).json({ message: 'Mã xác thực không chính xác!' });
        }
        if (admin.codeExpiry < Date.now()) {
            return res.status(401).json({ message: 'Mã xác thực đã hết hạn!' });
        }

        // Cập nhật mật khẩu mới
        admin.password = newPassword;
        admin.verificationCode = null;
        admin.codeExpiry = null;
        await admin.save();

        res.json({ message: 'Đổi mật khẩu thành công!' });
    } catch (err) {
        res.status(500).json({ message: 'Có lỗi xảy ra khi đổi mật khẩu!', error: err.message });
    }
});


// Gửi mã OTP để đặt lại mật khẩu
router.post('/gui-ma-dat-lai-mat-khau-admin', async (req, res) => {
    const { username } = req.body;

    try {
        const admin = await Admin.findOne({ username });
        if (!admin) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại!' });
        }

        // Tạo mã OTP
        const verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
        const codeExpiry = new Date(Date.now() + 3 * 60 * 1000); // Hết hạn sau 3 phút

        // Lưu mã OTP và thời gian hết hạn
        admin.verificationCode = verificationCode;
        admin.codeExpiry = codeExpiry;
        await admin.save();

        // Gửi email chứa mã OTP
        await transporter.sendMail({
            from: 'datnmd03@gmail.com',
            to: username,
            subject: 'Mã xác thực đặt lại mật khẩu',
            text: `Mã xác thực của bạn là: ${verificationCode}. Mã này có hiệu lực trong 3 phút.`
        });

        res.json({ message: 'Mã xác thực đã được gửi tới email!' });
    } catch (err) {
        res.status(500).json({ message: 'Có lỗi xảy ra khi gửi mã xác thực!', error: err.message });
    }
});

// Xác thực OTP và đặt lại mật khẩu
router.post('/xac-thuc-ma-doi-mat-khau-admin', async (req, res) => {
    const { username, verificationCode, newPassword } = req.body;

    try {
        const admin = await Admin.findOne({ username });
        if (!admin) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại!' });
        }

        // Kiểm tra mã OTP và thời gian hết hạn
        if (admin.verificationCode !== verificationCode) {
            return res.status(401).json({ message: 'Mã xác thực không chính xác!' });
        }
        if (admin.codeExpiry < Date.now()) {
            return res.status(401).json({ message: 'Mã xác thực đã hết hạn!' });
        }

        // Đặt mật khẩu mới
        admin.password = newPassword;
        admin.verificationCode = null;
        admin.codeExpiry = null;
        await admin.save();

        res.json({ message: 'Đặt lại mật khẩu thành công!' });
    } catch (err) {
        res.status(500).json({ message: 'Có lỗi xảy ra khi đặt lại mật khẩu!', error: err.message });
    }
});

//  Route đăng ký (Kiểm tra thông tin và gửi mã xác thực)
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

        // Lưu thông tin vào bộ nhớ tạm thời (không tạo tài khoản ngay)
        await TemporaryVerificationCodes.create({
            Tentaikhoan,
            Hoten,
            Matkhau,
            Anhtk
        });

        res.status(200).json({ message: 'Thông tin hợp lệ, vui lòng gửi mã xác thực để tiếp tục' });
    } catch (err) {
        console.error("Lỗi khi xử lý đăng ký:", err);
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: err });
    }
});

//  Route gửi mã xác thực qua email
router.post('/guimataotk', async (req, res) => {
    const { Tentaikhoan } = req.body;
    try {
        // Kiểm tra xem email có trong dữ liệu tạm thời không
        const tempUser = await TemporaryVerificationCodes.findOne({ Tentaikhoan });
        if (!tempUser) {
            return res.status(400).json({ message: 'Email không hợp lệ hoặc chưa được đăng ký' });
        }

        // Tạo mã xác thực 6 số
        const verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
        tempUser.verificationCode = verificationCode;
        await tempUser.save();

        // Gửi mã xác thực qua email
        const mailOptions = {
            from: 'datnmd03@gmail.com',
            to: Tentaikhoan,
            subject: 'Mã xác thực tài khoản của bạn',
            html: `<p>Mã xác thực của bạn là: <strong>${verificationCode}</strong></p>
                   <p>Mã này có hiệu lực trong 5 phút.</p>`
        };

        await transporter.sendMail(mailOptions);
        res.status(200).json({ message: 'Mã xác thực đã được gửi qua email' });
    } catch (error) {
        console.error('Lỗi khi gửi mã xác thực:', error);
        res.status(500).json({ message: 'Lỗi khi gửi mã xác thực', error });
    }
});

//  Route xác thực mã và tạo tài khoản
router.post('/xacthucmataotk', async (req, res) => {
    const { Tentaikhoan, verificationCode } = req.body;
    try {
        // Kiểm tra xem mã xác thực có hợp lệ không
        const tempUser = await TemporaryVerificationCodes.findOne({ Tentaikhoan, verificationCode });
        if (!tempUser) {
            return res.status(400).json({ message: 'Mã xác thực không hợp lệ hoặc đã hết hạn' });
        }

        // Tạo mã tài khoản tự động dựa trên seq
        const lastUser = await CustomerAccounts.findOne().sort({ seq: -1 });
        const accountNumber = `AC${(lastUser ? lastUser.seq + 1 : 1).toString().padStart(3, '0')}`;

        // Tạo tài khoản mới
        const newUser = new CustomerAccounts({
            Matk: accountNumber,
            Tentaikhoan: tempUser.Tentaikhoan,
            Hoten: tempUser.Hoten,
            Matkhau: tempUser.Matkhau,
            Anhtk: tempUser.Anhtk || '',
            seq: lastUser ? lastUser.seq + 1 : 1
        });

        // Lưu người dùng vào cơ sở dữ liệu
        await newUser.save();

        // Xóa thông tin tạm thời sau khi tạo tài khoản
        await TemporaryVerificationCodes.deleteOne({ Tentaikhoan });

        res.status(201).json({ message: 'Tài khoản đã được tạo thành công' });
    } catch (err) {
        console.error("Lỗi khi tạo tài khoản:", err);
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

        // So sánh mật khẩu mà không mã hóa
        if (Matkhau !== user.Matkhau) {
            return res.status(400).json({ message: 'Mật khẩu không đúng' });
        }

        // Tạo JWT token
        const token = jwt.sign({ id: user._id }, 'your_jwt_secret', { expiresIn: '1h' });

        res.status(200).json({ message: 'Đăng nhập thành công', token });
    } catch (err) {
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: err.message });
    }
});

// //  Route để hiển thị danh sách tài khoản
// router.get('/list-accounts', async (req, res) => {
//     try {
//         // Lấy danh sách tài khoản từ cơ sở dữ liệu
//         const accounts = await CustomerAccounts.find({}, { Matkhau: 0 }); // Loại bỏ mật khẩu để đảm bảo an toàn

//         res.status(200).json(accounts);
//     } catch (err) {
//         console.error("Lỗi khi lấy danh sách tài khoản:", err);
//         res.status(500).json({ message: 'Đã xảy ra lỗi', error: err });
//     }
// });

// Route để hiển thị danh sách tài khoản (hiển thị cả mật khẩu)
router.get('/list-accounts', async (req, res) => {
    try {
        // Lấy danh sách tài khoản từ cơ sở dữ liệu và bao gồm mật khẩu
        const accounts = await CustomerAccounts.find({}); // Bao gồm cả trường Matkhau

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

// Route đổi mật khẩu tạm thời
router.post('/doi-matkhau-tam-thoi', async (req, res) => {
    const { Tentaikhoan, MatkhauMoi } = req.body;

    try {
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Lưu mật khẩu mới trực tiếp mà không mã hóa
        user.MatkhauMoi = MatkhauMoi;
        await user.save();

        res.status(200).json({ message: 'Mật khẩu mới đã được lưu tạm thời. Vui lòng xác thực qua email' });
    } catch (error) {
        console.error('Lỗi khi lưu mật khẩu tạm thời:', error);
        res.status(500).json({ message: 'Lỗi khi lưu mật khẩu tạm thời', error });
    }
});

// Route xác thực mã và đổi mật khẩu
router.post('/xacthucma-doi-matkhau', async (req, res) => {
    const { Tentaikhoan, verificationCode } = req.body;

    try {
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Kiểm tra mã xác thực
        if (user.verificationCode !== verificationCode) {
            return res.status(400).json({ message: 'Mã xác thực không hợp lệ hoặc đã hết hạn' });
        }

        // Kiểm tra nếu mật khẩu mới chưa được lưu tạm thời
        if (!user.MatkhauMoi) {
            return res.status(400).json({ message: 'Mật khẩu mới chưa được lưu tạm thời' });
        }

        // Cập nhật mật khẩu mới mà không mã hóa
        user.Matkhau = user.MatkhauMoi;
        user.MatkhauMoi = null;
        user.verificationCode = null;
        await user.save();

        res.status(200).json({ message: 'Mật khẩu đã được đổi thành công' });
    } catch (error) {
        console.error('Lỗi khi xác thực mã và đổi mật khẩu:', error);
        res.status(500).json({ message: 'Lỗi khi xác thực mã và đổi mật khẩu', error });
    }
});
// Route để đổi họ tên
router.put('/doi-hoten', async (req, res) => {
    try {
        const { Tentaikhoan, HotenMoi } = req.body;

        // Tìm người dùng theo tên tài khoản
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(400).json({ message: 'Tài khoản không tồn tại' });
        }

        // Cập nhật họ tên mới
        user.Hoten = HotenMoi;
        await user.save();

        res.status(200).json({ message: 'Họ tên đã được cập nhật thành công' });
    } catch (error) {
        console.error('Lỗi khi đổi họ tên:', error);
        res.status(500).json({ message: 'Đã xảy ra lỗi khi đổi họ tên', error });
    }
});
// Cấu hình Multer để lưu ảnh vào thư mục 'uploads'
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'uploads/'); // Thư mục lưu ảnh
    },
    filename: function (req, file, cb) {
        cb(null, Date.now() + path.extname(file.originalname)); // Tên file là timestamp + extension
    }
});

const upload = multer({
    storage: storage,
    fileFilter: function (req, file, cb) {
        const fileTypes = /jpeg|jpg|png|gif/;
        const extname = fileTypes.test(path.extname(file.originalname).toLowerCase());
        const mimetype = fileTypes.test(file.mimetype);

        if (extname && mimetype) {
            cb(null, true);
        } else {
            cb(new Error('Chỉ hỗ trợ các tệp hình ảnh có định dạng jpeg, jpg, png hoặc gif'));
        }
    }
});

// Route cập nhật ảnh đại diện
router.put('/doi-anh', upload.single('AnhtkMoi'), async (req, res) => {
    try {
        const { Tentaikhoan } = req.body;
        const avatarFile = req.file;  // Lấy ảnh từ form

        if (!avatarFile) {
            return res.status(400).json({ message: 'Vui lòng chọn ảnh để cập nhật' });
        }

        // Tìm người dùng theo tên tài khoản
        const user = await CustomerAccounts.findOne({ Tentaikhoan });
        if (!user) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Cập nhật đường dẫn ảnh (sử dụng '/' thay cho '\\' để chuẩn hóa đường dẫn)
        user.Anhtk = avatarFile.path.replace(/\\/g, '/'); // Thay '\\' thành '/'
        await user.save();

        res.status(200).json({ 
            message: 'Ảnh đại diện đã được cập nhật thành công', 
            avatarUrl: user.Anhtk 
        });
    } catch (error) {
        console.error('Lỗi khi đổi ảnh đại diện:', error);
        res.status(500).json({ message: 'Đã xảy ra lỗi khi đổi ảnh đại diện', error });
    }
});
router.get('/chitiettaikhoan', async (req, res) => {
    try {
        // Lấy Tentaikhoan từ query params
        const { Tentaikhoan } = req.query;

        // Kiểm tra nếu không có Tentaikhoan
        if (!Tentaikhoan) {
            return res.status(400).json({ 
                message: 'Thiếu thông tin Tentaikhoan' 
            });
        }

        // Tìm user theo Tentaikhoan
        const user = await CustomerAccounts.findOne({ Tentaikhoan: Tentaikhoan });

        // Kiểm tra nếu không tìm thấy user
        if (!user) {
            return res.status(404).json({ 
                message: 'Không tìm thấy người dùng' 
            });
        }

        // Trả về thông tin người dùng
        res.status(200).json({
            Tentaikhoan: user.Tentaikhoan,
            Hoten: user.Hoten,
            Anhtk: user.Anhtk
        });
    } catch (error) {
        console.error('Lỗi khi lấy thông tin tài khoản:', error);
        res.status(500).json({ 
            message: 'Đã xảy ra lỗi trong quá trình xử lý' 
        });
    }
});



module.exports = router;
