const mongoose = require('mongoose');
const Schema = mongoose.Schema;



const FavoriteSchema = new Schema({
  Tentaikhoan: { type: String, required: true }, // Email hoặc ID tài khoản
  SanPham: { type: Schema.Types.ObjectId, ref: "product", required: true },
});


module.exports = mongoose.model('Favorite', FavoriteSchema);
