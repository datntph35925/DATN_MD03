const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const session = require('express-session');
const http = require('http'); // Thêm dòng này
const socketIo = require('socket.io'); // Import socket.io

const chatRoutes = require('./routes/chatRoutes');
const indexRouter = require('./routes/index');
const apiRouter = require('./routes/api');
const authRouter = require('./routes/auth');
const cartRouter = require('./routes/cart');
const database = require('./config/db');

const app = express();
const server = http.createServer(app); // Tạo server từ app
const io = socketIo(server); // Gắn socket.io vào server

// Cấu hình để phục vụ tệp tĩnh từ thư mục 'public'
app.use(express.static(path.join(__dirname, 'public')));

// Cài đặt view engine
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');

// Middleware
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

// Cấu hình session
app.use(session({
    secret: 'nadepzai', // Thay bằng khóa bí mật của bạn
    resave: false,
    saveUninitialized: true
}));

// Định nghĩa các route
app.use('/', indexRouter);
app.use('/api', apiRouter);
app.use('/auth', authRouter);
app.use('/cart', cartRouter);
app.use('/chatRouter', chatRoutes);

// Socket.io để xử lý chat thời gian thực
io.on('connection', (socket) => {
    console.log('Có người dùng kết nối:', socket.id);

    socket.on('sendMessage', (data) => {
        io.emit('receiveMessage', data); // Gửi tin nhắn đến tất cả các client
    });

    socket.on('disconnect', () => {
        console.log('Người dùng đã ngắt kết nối:', socket.id);
    });
});

// Kết nối đến MongoDB
database.connect().catch(() => {
    process.exit(1); // Thoát ứng dụng nếu không kết nối được
});

// Xử lý lỗi 404
app.use(function(req, res, next) {
    next(createError(404));
});

// Xử lý lỗi chung
app.use(function(err, req, res, next) {
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;
