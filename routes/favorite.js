const express = require('express');
const router = express.Router();
const Favorite = require('../models/Favorites');
const Product = require('../models/Products');
const CustomerAccounts = require('../models/CustomerAccounts');

// Thêm sản phẩm vào danh sách yêu thích
router.post("/add-favorites/:Tentaikhoan", async (req, res) => {
  const { Tentaikhoan } = req.params;
  const { SanPham } = req.body;

  try {
      // Kiểm tra xem tài khoản người dùng có tồn tại không
      const userAccount = await CustomerAccounts.findOne({ Tentaikhoan });
      if (!userAccount) {
          return res.status(404).json({ message: "Tài khoản không tồn tại." });
      }

      // Kiểm tra xem sản phẩm có tồn tại không
      const product = await Product.findById(SanPham);
      if (!product) {
          return res.status(405).json({ message: "Sản phẩm không tồn tại." });
      }

      // Kiểm tra xem sản phẩm đã có trong danh sách yêu thích chưa
      const existingFavorite = await Favorite.findOne({ Tentaikhoan, SanPham });
      if (existingFavorite) {
          return res.status(400).json({ message: "Sản phẩm đã có trong danh sách yêu thích." });
      }

      // Tạo mới và lưu vào danh sách yêu thích
      const favorite = new Favorite({ Tentaikhoan, SanPham });
      await favorite.save();

      res.status(200).json({ message: "Sản phẩm đã được thêm vào danh sách yêu thích", favorite });
  } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi khi thêm sản phẩm vào danh sách yêu thích.", error });
  }
});
// Lấy danh sách yêu thích của một người dùng
router.get("/get-favorites/:Tentaikhoan", async (req, res) => {
    const { Tentaikhoan } = req.params;
  
    try {
      const favorites = await Favorite.find({ Tentaikhoan }).populate("SanPham");
      res.status(200).json({ message: "Danh sách yêu thích", data: favorites });
    } catch (error) {
      res.status(500).json({ message: "Lỗi lấy danh sách yêu thích", error });
    }
  });

// Xóa sản phẩm khỏi danh sách yêu thích
router.delete("/delete-favorites/:Tentaikhoan", async (req, res) => {
    try {
      const {Tentaikhoan} = req.params
      const { SanPham } = req.body;

      const userAccount = await CustomerAccounts.findOne({ Tentaikhoan });
      if (!userAccount) {
          return res.status(404).json({ message: "Tài khoản không tồn tại." });
      }

      // Kiểm tra xem sản phẩm có tồn tại không
      const product = await Product.findById(SanPham);
      if (!product) {
          return res.status(405).json({ message: "Sản phẩm không tồn tại." });
      }

      await Favorite.findOneAndDelete({ Tentaikhoan, SanPham });
      res.status(200).json({ message: "Đã xóa khỏi danh sách yêu thích" });
    } catch (error) {
      res.status(500).json({ message: "Lỗi xóa sản phẩm yêu thích", error });
    }
  });
  

  // Route lấy trạng thái yêu thích cho một sản phẩm của người dùng
router.get("/get-favorite-status/:Tentaikhoan/:SanPhamId", async (req, res) => {
  const { Tentaikhoan, SanPhamId } = req.params;

  try {
      // Kiểm tra xem tài khoản người dùng có tồn tại không
      const userAccount = await CustomerAccounts.findOne({ Tentaikhoan });
      if (!userAccount) {
          return res.status(404).json({ message: "Tài khoản không tồn tại." });
      }

      // Kiểm tra xem sản phẩm có tồn tại không
      const product = await Product.findById(SanPhamId);
      if (!product) {
          return res.status(405).json({ message: "Sản phẩm không tồn tại." });
      }

      // Kiểm tra xem sản phẩm đã có trong danh sách yêu thích của người dùng chưa
      const existingFavorite = await Favorite.findOne({ Tentaikhoan, SanPham: SanPhamId });

      // Trả về trạng thái yêu thích
      if (existingFavorite) {
          return res.status(200).json({ message: "Sản phẩm là yêu thích", isFavorite: true });
      } else {
          return res.status(200).json({ message: "Sản phẩm không phải yêu thích", isFavorite: false });
      }
  } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi khi lấy trạng thái yêu thích.", error });
  }
});

// Route cập nhật trạng thái yêu thích cho sản phẩm của người dùng
// Route cập nhật trạng thái yêu thích cho sản phẩm của người dùng (thêm hoặc xóa)
router.put("/update-favorite-status/:Tentaikhoan/:SanPhamId", async (req, res) => {
  const { Tentaikhoan, SanPhamId } = req.params;
  const { isFavorite } = req.body; // Lấy trạng thái yêu thích từ request body

  try {
    // Kiểm tra xem tài khoản người dùng có tồn tại không
    const userAccount = await CustomerAccounts.findOne({ Tentaikhoan });
    if (!userAccount) {
      return res.status(404).json({ message: "Tài khoản không tồn tại." });
    }

    // Kiểm tra xem sản phẩm có tồn tại không
    const product = await Product.findById(SanPhamId);
    if (!product) {
      return res.status(405).json({ message: "Sản phẩm không tồn tại." });
    }

    // Xử lý thêm hoặc xóa sản phẩm khỏi yêu thích
    if (isFavorite) {
      // Thêm sản phẩm vào danh sách yêu thích
      const existingFavorite = await Favorite.findOne({ Tentaikhoan, SanPham: SanPhamId });
      if (!existingFavorite) {
        const favorite = new Favorite({ Tentaikhoan, SanPham: SanPhamId });
        await favorite.save();
        return res.status(200).json({ message: "Sản phẩm đã được thêm vào danh sách yêu thích", isFavorite: true });
      } else {
        return res.status(200).json({ message: "Sản phẩm đã có trong danh sách yêu thích", isFavorite: true });
      }
    } else {
      // Xóa sản phẩm khỏi danh sách yêu thích
      const existingFavorite = await Favorite.findOne({ Tentaikhoan, SanPham: SanPhamId });
      if (existingFavorite) {
        await Favorite.findOneAndDelete({ Tentaikhoan, SanPham: SanPhamId });
        return res.status(200).json({ message: "Sản phẩm đã bị xóa khỏi danh sách yêu thích", isFavorite: false });
      } else {
        return res.status(200).json({ message: "Sản phẩm không có trong danh sách yêu thích", isFavorite: false });
      }
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Lỗi khi cập nhật trạng thái yêu thích.", error });
  }
});


module.exports = router;
