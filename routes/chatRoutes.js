const express = require('express');
const router = express.Router();
const Chat = require('../models/Chat');

// API lấy tin nhắn với bộ lọc
router.get('/messages', async (req, res) => {
  const { senderId, receiverId } = req.query;

  try {
    let filter = {};
    if (senderId && receiverId) {
      filter = { 
        $or: [
          { senderId, receiverId },
          { senderId: receiverId, receiverId: senderId }
        ] 
      };
    } else if (senderId) {
      filter = { senderId };
    } else if (receiverId) {
      filter = { receiverId };
    }

    const messages = await Chat.find(filter).sort({ createdAt: -1 });
    res.status(200).json(messages);
  } catch (err) {
    res.status(500).json({ error: 'Lỗi khi lấy tin nhắn' });
  }
});

module.exports = router;
