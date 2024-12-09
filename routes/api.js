var express = require('express');
var router = express.Router();
const multer = require('multer');
const path = require('path');
const Products = require('../models/Products');

// Cấu hình Multer để lưu ảnh vào thư mục 'uploads'
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/'); // Thư mục lưu trữ các file tải lên
  },
  filename: (req, file, cb) => {
    cb(null, Date.now() + '-' + file.originalname); // Tên file sau khi tải lên
  },
});

const fileFilter = (req, file, cb) => {
  const fileTypes = /jpeg|jpg|png|gif/;
  const extName = fileTypes.test(path.extname(file.originalname).toLowerCase());
  const mimeType = fileTypes.test(file.mimetype);

  if (extName && mimeType) {
    cb(null, true); // Chấp nhận file
  } else {
    cb(new Error('Chỉ chấp nhận file ảnh định dạng jpeg, jpg, png, gif!'), false); // Từ chối file
  }
};

const upload = multer({
  storage: storage,
  fileFilter: fileFilter,
});

router.get("/get-list-product", async (req, res) => {
  try {
      //lấy ds sản phẩm
      const data = await Products.find().sort({ createdAt: -1 });
      if (data) {
          res.json({
              status: 200,
              messenger: "Lấy danh sách thành công",
              data: data,
          });
      } else {
          res.json({
              status: 400,
              messenger: "lấy danh sách thất bại",
              data: [],
          });
      }
  } catch (error) {
      console.log(error);
  }
});


router.post('/add-product', upload.array('HinhAnh', 10), async (req, res) => {
  try {
    const data = req.body;

    // Kiểm tra xem sản phẩm đã tồn tại chưa
    const existingProduct = await Products.findOne({ TenSP: data.TenSP });

    if (existingProduct) {
      return res.status(401).json({
        status: 401,
        messenger: 'Lỗi, sản phẩm đã tồn tại'
      });
    }

    // Lấy các đường dẫn ảnh từ Multer (upload từ máy tính)
    const imageLinksFromUpload = req.files ? req.files.map(file => file.path.replace(/\\/g, '/')) : [];

    // Lấy các link ảnh từ yêu cầu của người dùng (thường là từ frontend)
    let imageLinksFromUrls = [];
    if (data.HinhAnh) {
      // Kiểm tra xem HinhAnh có phải là chuỗi hay mảng
      if (typeof data.HinhAnh === 'string') {
        // Nếu là chuỗi, tách nó thành mảng URL
        imageLinksFromUrls = data.HinhAnh.split(',').map(url => url.trim());
      } else if (Array.isArray(data.HinhAnh)) {
        // Nếu là mảng, sử dụng trực tiếp
        imageLinksFromUrls = data.HinhAnh.map(url => url.trim());
      }
    }

    // Kết hợp tất cả các link ảnh (upload từ máy và link từ URL)
    const allImageLinks = [...imageLinksFromUpload, ...imageLinksFromUrls];

    // Kiểm tra và xử lý KichThuoc
    let kichThuoc = data.KichThuoc;
    if (typeof kichThuoc === 'string') {
      // Nếu KichThuoc là chuỗi, cố gắng chuyển nó thành mảng đối tượng
      try {
        kichThuoc = JSON.parse(kichThuoc); // Chuyển đổi chuỗi JSON thành mảng đối tượng
      } catch (error) {
        return res.status(400).json({
          status: 400,
          messenger: 'Lỗi: Dữ liệu KichThuoc không hợp lệ',
          error: error.message
        });
      }
    }

    // Kiểm tra nếu KichThuoc đã là mảng đối tượng hợp lệ
    if (!Array.isArray(kichThuoc)) {
      return res.status(400).json({
        status: 400,
        messenger: 'Lỗi: KichThuoc phải là mảng đối tượng'
      });
    }

    // Tạo sản phẩm mới với các đường dẫn ảnh kết hợp
    const newProduct = new Products({
      MaSanPham: data.MaSanPham,
      TenSP: data.TenSP,
      ThuongHieu: data.ThuongHieu,
      KichThuoc: kichThuoc, // Đảm bảo KichThuoc là mảng đối tượng hợp lệ
      GiaBan: data.GiaBan,
      MoTa: data.MoTa,
      HinhAnh: allImageLinks, // Lưu mảng các đường dẫn ảnh
      TrangThaiYeuThich: data.TrangThaiYeuThich
    });

    // Lưu sản phẩm mới vào cơ sở dữ liệu
    const result = await newProduct.save();
    
    res.status(200).json({
      status: 200,
      messenger: 'Thêm sản phẩm thành công',
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


router.put('/update-product-by-id/:id', upload.array('HinhAnh', 10), async (req, res) => {
  try {
    const { id } = req.params;
    const data = req.body;

    // Tìm sản phẩm theo ID
    const product = await Products.findById(id);

    if (product) {
      // Cập nhật các trường thông tin sản phẩm từ dữ liệu nhận được
      product.MaSanPham = data.MaSanPham ?? product.MaSanPham;
      product.TenSP = data.TenSP ?? product.TenSP;
      product.ThuongHieu = data.ThuongHieu ?? product.ThuongHieu;

      // Xử lý KichThuoc nếu có
      if (data.KichThuoc) {
        try {
          product.KichThuoc = JSON.parse(data.KichThuoc); // Chuyển chuỗi JSON thành đối tượng
        } catch (error) {
          return res.status(400).json({
            status: 400,
            messenger: 'Kích thước không hợp lệ'
          });
        }
      }

      product.GiaBan = data.GiaBan ?? product.GiaBan;
      product.MoTa = data.MoTa ?? product.MoTa;
      product.TrangThaiYeuThich = data.TrangThaiYeuThich ?? product.TrangThaiYeuThich;

      // Lấy các đường dẫn ảnh từ Multer (upload từ máy tính)
      const imageLinksFromUpload = req.files ? req.files.map(file => file.path.replace(/\\/g, '/')) : [];

      // Lấy các link ảnh từ yêu cầu của người dùng (URL gửi từ frontend)
      const imageLinksFromUrls = data.HinhAnh ? data.HinhAnh.split(',').map(url => url.trim()) : [];

      // Kết hợp các link ảnh từ Multer và link ảnh URL
      const allImageLinks = [...imageLinksFromUpload, ...imageLinksFromUrls];

      // Loại bỏ các đường dẫn ảnh trùng lặp (nếu có)
      product.HinhAnh = Array.from(new Set([...product.HinhAnh, ...allImageLinks]));

      // Lưu sản phẩm đã cập nhật
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
