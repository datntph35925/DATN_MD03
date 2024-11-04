const mongoose = require('mongoose');

mongoose.set('strictQuery', true);

const atlas = "mongodb+srv://datntph35925:6WJndb7e3GC7MLoa@datn.4cj8t.mongodb.net/DatabaseDATN?retryWrites=true&w=majority&appName=DATN";

const connect = async () => {
  try {
    await mongoose.connect(atlas); 
    console.log('Kết nối thành công đến MongoDB Atlas');
  } catch (error) {
    console.error('Lỗi kết nối:', error);
  }
};

module.exports = { connect };
