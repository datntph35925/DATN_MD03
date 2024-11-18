const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const ChatSchema = new Schema({
  senderId: { type: String, required: true }, // ID người gửi
  receiverId: { type: String, required: true }, // ID người nhận
  message: { type: String, required: true }, // Nội dung tin nhắn
  timestamp: { type: Date, default: Date.now } // Thời gian tin nhắn
});

module.exports = mongoose.model('Chat', ChatSchema);
