const express = require('express');
const router = express.Router();
const Chat = require('../models/Chat');
//khách hàng gửi tin nhắn 
router.post('/khach-hang-gui-tin-nhan', async (req, res) => {
  const { TentaiKhoan, message } = req.body; // Tài khoản khách hàng và nội dung tin nhắn

  if (!TentaiKhoan || !message) {
      return res.status(400).json({ error: 'Thiếu thông tin TentaiKhoan hoặc message' });
  }

  try {
      // Tạo tin nhắn mới
      const newMessage = new Chat({
          senderId: TentaiKhoan, // TentaiKhoan của khách hàng
          receiverId: 'admin', // Mặc định là admin
          message,
      });

      await newMessage.save();

      res.status(201).json({
          success: true,
          message: 'Tin nhắn đã gửi đến admin',
          data: newMessage,
      });
  } catch (err) {
      res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
  }
});
//admin gửi khách hàng
router.post('/admin-gui-tin-nhan', async (req, res) => {
  const { TentaiKhoan, message } = req.body; // Tài khoản khách hàng và nội dung tin nhắn

  if (!TentaiKhoan || !message) {
      return res.status(400).json({ error: 'Thiếu thông tin TentaiKhoan hoặc message' });
  }

  try {
      // Tạo tin nhắn mới
      const newMessage = new Chat({
          senderId: 'admin', // Mặc định là admin
          receiverId: TentaiKhoan, // TentaiKhoan của khách hàng
          message,
      });

      await newMessage.save();

      res.status(201).json({
          success: true,
          message: 'Admin đã gửi tin nhắn cho khách hàng',
          data: newMessage,
      });
  } catch (err) {
      res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
  }
});
// lịch sự tin nhắn
router.get('/lich-su-tin-nhan', async (req, res) => {
  const { TentaiKhoan } = req.body; // Đổi từ req.query thành req.body

  if (!TentaiKhoan) {
      return res.status(400).json({ error: 'Thiếu thông tin TentaiKhoan' });
  }

  try {
      const messages = await Chat.find({
          $or: [
              { senderId: TentaiKhoan, receiverId: 'admin' },
              { senderId: 'admin', receiverId: TentaiKhoan },
          ],
      }).sort({ timestamp: 1 }); // Sắp xếp theo thứ tự thời gian

      res.status(200).json({
          success: true,
          messages,
      });
  } catch (err) {
      res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
  }
});
//admin thu hồi tin nhắn
router.delete('/admin-xoa-tin-nhan/:id', async (req, res) => {
  const { id } = req.params; // Lấy ID tin nhắn từ URL
  const { username } = req.body; // Lấy username của admin từ body

  // Kiểm tra username có phải admin không
  if (username !== 'doantotnghiepmd03@gmail.com') {
      return res.status(403).json({ error: 'Bạn không có quyền thực hiện hành động này' });
  }

  try {
      // Tìm và xóa tin nhắn theo ID
      const deletedMessage = await Chat.findByIdAndDelete(id);

      if (!deletedMessage) {
          return res.status(404).json({ error: 'Không tìm thấy tin nhắn để xóa' });
      }

      res.status(200).json({
          success: true,
          message: 'Tin nhắn đã được admin xóa thành công',
          data: deletedMessage,
      });
  } catch (err) {
      console.error('Lỗi khi admin xóa tin nhắn:', err.message); // Log lỗi nếu có
      res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
  }
});

//Khách hàng thu hồi tin nhắn
router.delete('/khach-hang-xoa-tin-nhan/:id', async (req, res) => {
  const { id } = req.params; // Lấy ID tin nhắn từ URL
  const { TentaiKhoan } = req.body; // Lấy tài khoản khách hàng từ body

  if (!TentaiKhoan) {
      return res.status(400).json({ error: 'Thiếu thông tin tài khoản khách hàng (TentaiKhoan)' });
  }

  try {
      // Tìm tin nhắn dựa trên ID và senderId khớp với TentaiKhoan
      const message = await Chat.findOne({ _id: id, senderId: TentaiKhoan });

      if (!message) {
          return res.status(404).json({ error: 'Không tìm thấy tin nhắn thuộc tài khoản của bạn' });
      }

      // Xóa tin nhắn
      await Chat.findByIdAndDelete(id);

      res.status(200).json({
          success: true,
          message: 'Tin nhắn đã được xóa thành công',
          data: message,
      });
  } catch (err) {
      console.error('Lỗi khi xóa tin nhắn:', err.message); // Log lỗi nếu có
      res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
  }
});

//xóa tn
router.delete('/xoa-tin-nhan/:id', async (req, res) => {
  const { id } = req.params; // Lấy ID từ URL

  try {
      // Xóa tin nhắn dựa trên ID
      const deletedMessage = await Chat.findByIdAndDelete(id);

      if (!deletedMessage) {
          return res.status(404).json({ error: 'Không tìm thấy tin nhắn để xóa' });
      }

      // Trả về phản hồi thành công
      res.status(200).json({
          success: true,
          message: 'Tin nhắn đã được xóa thành công',
          data: deletedMessage,
      });
  } catch (err) {
      res.status(500).json({ error: 'Lỗi máy chủ', details: err.message });
  }
});

module.exports = router;
