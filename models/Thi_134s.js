const mongoose = require('mongoose');
const Scheme = mongoose.Schema;

const Thi_134s = new Scheme({
    ho_ten_ph35925: {type: String, required: true },
    mon_thi_ph35925: {type: String, required: true},
    ngay_thi_ph35925: {type: String, required: true},
    ca_thi_ph35925: {type: Number,required: true, default:1},
    hinh_anh_ph35925: {type: String},
},{
    timestamps: true
})

module.exports = mongoose.model('thi_134',Thi_134s)