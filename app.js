const createError = require("http-errors");
const express = require("express");
const path = require("path");
const cookieParser = require("cookie-parser");
const logger = require("morgan");
const session = require("express-session");
const http = require("http"); // Thêm dòng này
const socketIo = require("socket.io"); // Import socket.io

const chatRoutes = require("./routes/chatRoutes");
const indexRouter = require("./routes/index");
const apiRouter = require("./routes/api");
const authRouter = require("./routes/auth");
const cartRouter = require("./routes/cart");
const orderRouter = require("./routes/order");
const database = require("./config/db");
const cors = require("cors");
const Admin = require("./models/Admin"); // Import model Admin
const locationRouter = require("./routes/locations");
const notificationRouter = require('./routes/NotificationRouter'); 


const app = express();
const server = http.createServer(app); // Tạo server từ app
const io = socketIo(server); // Gắn socket.io vào server

// Cấu hình để phục vụ tệp tĩnh từ thư mục 'public'
app.use(express.static(path.join(__dirname, "public")));

// Cấu hình để phục vụ các tệp tĩnh từ thư mục 'uploads'
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));



// Cài đặt view engine
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "hbs");

// Middleware
app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

// Cấu hình session
app.use(
  session({
    secret: "nadepzai", // Thay bằng khóa bí mật của bạn
    resave: false,
    saveUninitialized: true,
  })
);
app.use(
  cors({
    origin: "http://localhost:3001", // Replace with your actual domain
    methods: ["GET", "POST", "PUT", "DELETE"], // Allow GET, POST, PUT, and DELETE methods
    allowedHeaders: ["Content-Type", "Authorization"],
    credentials: true, // Specify allowed headers
  })
);

// Định nghĩa các route
app.use("/", indexRouter);
app.use("/api", apiRouter);
app.use("/auth", authRouter);
app.use("/cart", cartRouter);
app.use("/chatRouter", chatRoutes);
app.use("/order", orderRouter);
app.use("/locations", locationRouter)
app.use('/routes/notifications', notificationRouter);
// Socket.io để xử lý chat thời gian thực
io.on("connection", (socket) => {
  console.log("Có người dùng kết nối:", socket.id);

  socket.on("sendMessage", (data) => {
    io.emit("receiveMessage", data); // Gửi tin nhắn đến tất cả các client
  });

  socket.on("disconnect", () => {
    console.log("Người dùng đã ngắt kết nối:", socket.id);
  });
});

// Hàm tạo tài khoản admin mặc định
async function createDefaultAdmin() {
  const defaultAdmin = {
    username: "doantotnghiepmd03@gmail.com", // Email mặc định
    password: "123456", // Mật khẩu mặc định
  };

  try {
    const adminExists = await Admin.findOne({ username: defaultAdmin.username });

    if (!adminExists) {
      const admin = new Admin(defaultAdmin);
      await admin.save();
      // console.log("Tài khoản admin mặc định đã được tạo!");
    } else {
      // console.log("Tài khoản admin mặc định đã tồn tại!");
    }
  } catch (err) {
    console.error("Lỗi khi tạo admin mặc định:", err);
  }
}

// Socket.IO xử lý chat thời gian thực
// io.on("connection", (socket) => {
//   console.log(`Người dùng kết nối: ${socket.id}`);

//   // Lắng nghe sự kiện gửi tin nhắn từ client
//   socket.on("sendMessage", (data) => {
//     console.log(`Tin nhắn từ ${data.sender}: ${data.message}`);
//     io.emit("receiveMessage", data); // Phát tin nhắn đến tất cả client
//   });

//   // Lắng nghe sự kiện ngắt kết nối
//   socket.on("disconnect", () => {
//     console.log(`Người dùng ngắt kết nối: ${socket.id}`);
//   });
// });
// Kết nối đến MongoDB và tạo tài khoản admin mặc định
database.connect()
  .then(() => {
    // console.log("Kết nối MongoDB thành công!");
    createDefaultAdmin(); // Gọi hàm tạo tài khoản admin mặc định
  })
  .catch(() => {
    console.error("Kết nối MongoDB thất bại!");
    process.exit(1); // Thoát ứng dụng nếu không kết nối được
  });

// Xử lý lỗi 404
app.use(function (req, res, next) {
  next(createError(404));
});

// Xử lý lỗi chung
app.use(function (err, req, res, next) {
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};
  res.status(err.status || 500);
  res.render("error");
});

module.exports = app;
