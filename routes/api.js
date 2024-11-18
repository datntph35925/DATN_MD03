var express = require('express');
var router = express.Router();



const Products = require('../models/Products');
module.exports = router;

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


    router.post('/add-product', async (req, res) => {
      try {
          const data = req.body; // Lấy dữ liệu từ body 
  
          // Tạo một đối tượng sản phẩm mới với dữ liệu từ body
          const newProducts = new Products({
              MaSanPham: data.MaSanPham,
              TenSP: data.TenSP,
              ThuongHieu: data.ThuongHieu,
              KichThuoc: data.KichThuoc,
              GiaBan: data.GiaBan,
              MoTa: data.MoTa,
              HinhAnh: data.HinhAnh,
              TrangThaiYeuThich: data.TrangThaiYeuThich
          });
  
          // Lưu sản phẩm vào cơ sở dữ liệu
          const result = await newProducts.save();
  
          // Nếu thêm thành công, trả về dữ liệu
          if (result) {
              res.json({
                  status: 200,
                  messenger: "Thêm thành công",
                  data: result
              });
          } else {
              // Nếu thêm không thành công, thông báo lỗi
              res.json({
                  status: 400,
                  messenger: "Lỗi, thêm không thành công",
                  data: []
              });
          }
      } catch (error) {
          console.error(error);
          res.status(500).json({
              status: 500,
              messenger: "Lỗi server",
              error: error.message
          });
      }
  });

// router.post('/add', async(req,res) =>{
//     try{
//       const model = new Products(req.body)
//       const result = await model.save()
//       if (result) {
//         res.json({
//           "status":200,
//           "message":"Thêm thành công",
//           "data": resultzzz
//         })
//       }else{
//         res.json({
//           "status":400,
//           "message":"Thêm thất bại",
//           "data": []
//         })
//       }
//     }catch(error){
//       console.log('Error:' +error);
      
//     }
// })

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

  router.put('/update-product-by-id/:id', async (req, res) => {
    try {
        const {id} = req.params
        const data = req.body; // lấy dữ liệu từ body
        const updateProduct = await Products.findById(id)
        let result = null;
        if(updateProduct){
          updateProduct.Masanpham = data.Masanpham ?? updateProduct.Masanpham;
          updateProduct.TenSP = data.TenSP ?? updateProduct.TenSP,
          updateProduct.Thuonghieu = data.Thuonghieu ?? updateProduct.Thuonghieu,
          updateProduct.KichThuoc = data.KichThuoc ?? updateProduct.KichThuoc,
          updateProduct.GiaBan = data.GiaBan ?? updateProduct.GiaBan,
          updateProduct.MoTa = data.MoTa ?? updateProduct.MoTa,
          updateProduct.HinhAnh = data.HinhAnh ?? updateProduct.HinhAnh,
          updateProduct.TrangThaiYeuThich = data.TrangThaiYeuThich ?? updateProduct.TrangThaiYeuThich,
            result = await updateProduct.save();
        }
        if(result)
        {
        // Nếu thêm thành công result Inull trở về dữ liệu
            res.json({
                "status": 200,
                "messenger": "Cập nhật thành công",
                "data": result
            })
        }else{
            // Nếu thêm không thành công result null, thông báo không thành công
            res.json({
                "status": 400,
                "messenger": "Lỗi, Cập nhật không thành công",
                "data":[]
            })
        }
    } catch (error) {
        console.log(error);
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

router.get('/get-so-luong-ton/:id/:size', async (req, res) => {
  try {
    const { id, size } = req.params; // Get product ID and size from the request parameters
    const product = await Products.findById(id); // Find the product by ID

    if (product) {
      // Find the specific size in the KichThuoc array
      const sizeData = product.KichThuoc.find(item => item.size === parseInt(size));

      if (sizeData) {
        res.status(200).json({
          status: 200,
          message: 'Lấy soLuongTon thành công',
          soLuongTon: sizeData.soLuongTon
        });
      } else {
        res.status(404).json({
          status: 404,
          message: 'Không tìm thấy kích cỡ này',
          soLuongTon: null
        });
      }
    } else {
      res.status(404).json({
        status: 404,
        message: 'Không tìm thấy sản phẩm',
        soLuongTon: null
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




//nam anh đẹp zai commit
//test branch
