var express = require('express');
var router = express.Router();
const multer = require('multer');
const path = require('path');
const Products = require('../models/Products');

// Cấu hình Multer để lưu ảnh vào thư mục 'uploads'
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'uploads/'); // Thư mục lưu ảnh
  },
  filename: function (req, file, cb) {
    cb(null, Date.now() + path.extname(file.originalname)); // Tên file là timestamp + extension
  }
});

const fileFilter = (req, file, cb) => {
  const fileTypes = /jpeg|jpg|png|gif/;
  const extName = fileTypes.test(path.extname(file.originalname).toLowerCase());
  const mimeType = fileTypes.test(file.mimetype);

  if (extName && mimeType) {
    cb(null, true);
  } else {
    cb(new Error('Chỉ chấp nhận file ảnh định dạng jpeg, jpg, png, gif!'), false);
  }
};

const upload = multer({
  storage: storage,
  fileFilter: fileFilter,
});

router.get("/get-list-product", async (req, res) => {
  try {
    const data = await Products.find().sort({ createdAt: -1 });
    const totalCount = await Products.countDocuments();

    if (data) {
      // Thêm tiền tố URL vào đường dẫn ảnh nếu cần
      const productsWithImageURLs = data.map(product => ({
        ...product.toObject(),
        HinhAnh: req.protocol + '://' + req.get('host') + '/' + product.HinhAnh // Tạo URL hoàn chỉnh
      }));

      res.json({
        status: 200,
        messenger: "Lấy danh sách thành công",
        data: productsWithImageURLs, // Danh sách sản phẩm với đường dẫn ảnh hoàn chỉnh
        totalCount: totalCount
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lấy danh sách thất bại",
        data: [],
        totalCount: 0
      });
    }
  } catch (error) {
    console.log(error);
    res.json({
      status: 500,
      messenger: "Lỗi server",
      data: [],
      totalCount: 0
    });
  }
});




router.post('/add-product', upload.single('HinhAnh'), async (req, res) => {
  try {
    const data = req.body;
    const existingProduct = await Products.findOne({ TenSP: data.TenSP });

    if (existingProduct) {
      return res.status(401).json({
        status: 401,
        messenger: 'Lỗi, sản phẩm đã tồn tại'
      });
    }

    const newProduct = new Products({
      MaSanPham: data.MaSanPham,
      TenSP: data.TenSP,
      ThuongHieu: data.ThuongHieu,
      KichThuoc: data.KichThuoc,
      GiaBan: data.GiaBan,
      MoTa: data.MoTa,
      HinhAnh: req.file ? req.file.path : null,
      TrangThaiYeuThich: data.TrangThaiYeuThich
    });

    const result = await newProduct.save();
    res.status(200).json({
      status: 200,
      messenger: 'Thêm thành công',
      data: result
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: 500,
      messenger: 'Lỗi server',
      error: error.message
    });
  }
});

router.delete("/delete-product-by-id/:id", async (req, res) => {
  try {
    const { id } = req.params;
    const result = await Products.findByIdAndDelete(id);
    if (result) {
      res.json({
        status: 200,
        messenger: "tìm và xóa theo id thành công",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        messenger: "tìm và xóa thất bại",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});


router.put('/update-product-by-id/:id', upload.single('HinhAnh'), async (req, res) => {
  try {
    const { id } = req.params;
    const data = req.body;
    const product = await Products.findById(id);

    if (product) {
      product.MaSanPham = data.MaSanPham ?? product.MaSanPham;
      product.TenSP = data.TenSP ?? product.TenSP;
      product.ThuongHieu = data.ThuongHieu ?? product.ThuongHieu;
      product.KichThuoc = data.KichThuoc ?? product.KichThuoc;
      product.GiaBan = data.GiaBan ?? product.GiaBan;
      product.MoTa = data.MoTa ?? product.MoTa;
      product.HinhAnh = req.file ? req.file.path : product.HinhAnh;
      product.TrangThaiYeuThich = data.TrangThaiYeuThich ?? product.TrangThaiYeuThich;

      const result = await product.save();
      res.status(200).json({
        status: 200,
        messenger: 'Cập nhật thành công',
        data: result
      });
    } else {
      res.status(404).json({
        status: 404,
        messenger: 'Không tìm thấy sản phẩm'
      });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: 500,
      messenger: 'Lỗi server',
      error: error.message
    });
  }
});

router.get('/get-product-by-id/:id', async (req, res) => {
  try {
    const { id } = req.params; // Lấy id từ params
    const product = await Products.findById(id); // Tìm sản phẩm theo id

    if (product) {
      res.status(200).json({
        status: 200,
        message: 'Lấy thông tin sản phẩm thành công',
        data: product
      });
    } else {
      res.status(404).json({
        status: 404,
        message: 'Không tìm thấy sản phẩm',
        data: null
      });
    }
  } catch (error) {
    console.error('Lỗi:', error);
    res.status(500).json({
      status: 500,
      message: 'Lỗi server',
      error: error.message
    });
  }
});

router.get('/total-products', async (req, res) => {
  try {
    const totalProducts = await Products.countDocuments();
    res.status(200).json({
      success: true,
      totalProducts: totalProducts
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Lỗi khi lấy tổng số lượng tài khoản khách hàng',
      error: error.message
    });
  }
});

router.post('/check-upload', upload.single('image'), async (req, res) => {
  // Nếu upload thành công, Multer sẽ tự động đưa file vào req.file
  if (req.file) {
    res.json({
      status: 200,
      message: 'Upload thành công!',
      fileInfo: {
        filename: req.file.filename, // Tên file đã lưu
        path: req.file.path, // Đường dẫn lưu file trên server
        size: req.file.size, // Kích thước file
        mimeType: req.file.mimetype // Kiểu file (ví dụ: image/jpeg)
      }
    });
  } else {
    res.status(400).json({
      status: 400,
      message: 'Lỗi, không có file nào được upload!'
    });
  }
});

module.exports = router;



//nam anh đẹp zai commit
//test branch
