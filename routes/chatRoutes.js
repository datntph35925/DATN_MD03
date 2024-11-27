const express = require('express');
const router = express.Router();
const Chat = require('../models/Chat');

// Tích hợp Socket.IO (socket.io sẽ được truyền từ `app.js`)
let io;
module.exports = (socketIo) => {
    io = socketIo;
};

// Gửi tin nhắn từ khách hàng
router.post('/khach-hang-gui-tin-nhan', async (req, res) => {
    const { TentaiKhoan, message } = req.body;

    if (!TentaiKhoan || !message) {
        return res.status(400).json({ error: 'Thiếu thông tin TentaiKhoan hoặc message' });
    }

    try {
        const newMessage = new Chat({
            senderId: TentaiKhoan,
            receiverId: 'admin',
            message,
        });

        await newMessage.save();

        // Phát sự kiện tới admin qua Socket.IO
        if (io) {
            io.emit('receiveMessage', newMessage);
        }

        res.status(201).json({
            success: true,
            message: 'Tin nhắn đã gửi đến admin',
            data: newMessage,
        });
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
    }
});

// Gửi tin nhắn từ admin đến khách hàng
router.post('/admin-gui-tin-nhan', async (req, res) => {
    const { TentaiKhoan, message } = req.body;

    if (!TentaiKhoan || !message) {
        return res.status(400).json({ error: 'Thiếu thông tin TentaiKhoan hoặc message' });
    }

    try {
        const newMessage = new Chat({
            senderId: 'admin',
            receiverId: TentaiKhoan,
            message,
        });

        await newMessage.save();

        // Phát sự kiện tới khách hàng qua Socket.IO
        if (io) {
            io.emit('receiveMessage', newMessage);
        }

        res.status(201).json({
            success: true,
            message: 'Admin đã gửi tin nhắn cho khách hàng',
            data: newMessage,
        });
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
    }
});

// Lấy lịch sử tin nhắn
router.get('/lich-su-tin-nhan', async (req, res) => {
    const { TentaiKhoan } = req.query;

    if (!TentaiKhoan) {
        return res.status(401).json({ error: 'Thiếu thông tin TentaiKhoan' });
    }

    try {
        const messages = await Chat.find({
            $or: [
                { senderId: TentaiKhoan, receiverId: 'admin' },
                { senderId: 'admin', receiverId: TentaiKhoan },
            ],
        }).sort({ createdAt: 1 });

        res.status(200).json(messages);
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
    }
});

// Admin xóa tin nhắn
router.delete('/admin-xoa-tin-nhan/:id', async (req, res) => {
    const { id } = req.params;
    const { username } = req.body;

    if (username !== 'doantotnghiepmd03@gmail.com') {
        return res.status(403).json({ error: 'Bạn không có quyền thực hiện hành động này' });
    }

    try {
        const deletedMessage = await Chat.findByIdAndDelete(id);

        if (!deletedMessage) {
            return res.status(404).json({ error: 'Không tìm thấy tin nhắn để xóa' });
        }

        res.status(200).json({
            success: true,
            message: 'Tin nhắn đã được admin xóa thành công',
            data: deletedMessage,
        });

        // Phát sự kiện qua Socket.IO để cập nhật phía client
        if (io) {
            io.emit('messageDeleted', { id });
        }
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
    }
});

// Khách hàng xóa tin nhắn
router.delete('/khach-hang-xoa-tin-nhan/:id', async (req, res) => {
    const { id } = req.params;
    const { TentaiKhoan } = req.query;

    if (!id || !TentaiKhoan) {
        return res.status(400).json({ error: 'Thiếu ID hoặc TentaiKhoan' });
    }

    try {
        const message = await Chat.findOneAndDelete({
            _id: id,
            senderId: TentaiKhoan,
        });

        if (!message) {
            return res.status(404).json({ error: 'Không tìm thấy tin nhắn để xóa' });
        }

        res.status(200).json({ success: true, message: 'Đã xóa tin nhắn thành công' });

        // Phát sự kiện qua Socket.IO để cập nhật phía client
        if (io) {
            io.emit('messageDeleted', { id });
        }
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
    }
});

// Xóa tin nhắn chung
router.delete('/xoa-tin-nhan/:id', async (req, res) => {
    const { id } = req.params;

    try {
        const deletedMessage = await Chat.findByIdAndDelete(id);

        if (!deletedMessage) {
            return res.status(404).json({ error: 'Không tìm thấy tin nhắn để xóa' });
        }

        res.status(200).json({
            success: true,
            message: 'Tin nhắn đã được xóa thành công',
            data: deletedMessage,
        });

        // Phát sự kiện qua Socket.IO để cập nhật phía client
        if (io) {
            io.emit('messageDeleted', { id });
        }
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
    }
});

module.exports = router;
