import React, { useEffect, useState } from "react";
import ReactApexChart from "react-apexcharts";
import { ConfigProvider, DatePicker, message, Spin } from "antd";
import { getRevenueStatistics } from "../../Server/Order"; // API
import viVN from "antd/es/locale/vi_VN";
import moment from "moment";
import "./index.scss";

const { RangePicker } = DatePicker;

const ApexChart = () => {
  const [revenueData, setRevenueData] = useState({ dates: [], prices: [] });
  const [options, setOptions] = useState({
    chart: { type: "area", height: 350, zoom: { enabled: false } },
    dataLabels: { enabled: false },
    stroke: { curve: "straight" },
    title: { text: "Thống kê doanh thu", align: "left" },
    xaxis: {
      type: "datetime",
      categories: [],
      labels: {
        // Sử dụng format ngày "DD/MM/YYYY"
        formatter: function (value) {
          return moment(value).format("DD/MM/YYYY"); // Chuyển từ mốc thời gian thành ngày theo định dạng bạn muốn
        },
      },
    },
    yaxis: { opposite: true },
    legend: { horizontalAlign: "left" },
  });
  const [series, setSeries] = useState([{ name: "Doanh thu", data: [] }]);
  const [loading, setLoading] = useState(false);

  const fetchRevenueData = async (start, end) => {
    setLoading(true);
    try {
      console.log(`Fetching revenue data from ${start} to ${end}`);
      const response = await getRevenueStatistics(start, end);
      console.log("API response:", response);

      // Giả sử dữ liệu trả về có dạng mảng các đối tượng { date: 'YYYY-MM-DD', revenue: value }
      const data = response; // Dữ liệu trả về từ API

      // Kiểm tra tính hợp lệ của dữ liệu
      if (!Array.isArray(data)) {
        console.error("Dữ liệu không phải là mảng:", data);
        message.error("Dữ liệu không hợp lệ từ API");
        return;
      }

      // Xử lý mảng ngày và doanh thu từ dữ liệu API
      const dates = data.map((item) => item.date);
      const revenues = data.map((item) => item.revenue);

      // Tạo mảng ngày từ khoảng thời gian start đến end
      const dateRange = generateDateRange(start, end);
      const updatedDates = dateRange.map((date) => date.format("YYYY-MM-DD"));
      const updatedRevenues = dateRange.map((date) => {
        const index = dates.indexOf(date.format("YYYY-MM-DD"));
        return index >= 0 ? revenues[index] : 0;
      });

      // Cập nhật dữ liệu vào state
      setRevenueData({ dates: updatedDates, prices: updatedRevenues });
      setOptions((prev) => ({
        ...prev,
        xaxis: { ...prev.xaxis, categories: updatedDates },
      }));
      setSeries([{ name: "Doanh thu", data: updatedRevenues }]);
    } catch (error) {
      console.error("Lỗi khi gọi API:", error);
      message.error("Không thể tải dữ liệu doanh thu");
    } finally {
      setLoading(false);
    }
  };

  const generateDateRange = (start, end) => {
    const startDate = moment(start);
    const endDate = moment(end);
    const dateRange = [];

    let currentDate = startDate;
    while (currentDate <= endDate) {
      dateRange.push(currentDate.clone());
      currentDate = currentDate.add(1, "days");
    }

    return dateRange;
  };

  const handleDateChange = (dates) => {
    if (!dates || dates.length < 2) return;

    const [start, end] = dates;
    const startDate = start.startOf("day").format("YYYY-MM-DD");
    const endDate = end.endOf("day").format("YYYY-MM-DD");

    fetchRevenueData(startDate, endDate);
  };

  useEffect(() => {
    const now = new Date();
    const startOfWeek = new Date(now.setDate(now.getDate() - 7));
    fetchRevenueData(startOfWeek.toISOString(), new Date().toISOString());
  }, []);

  return (
      <ConfigProvider locale={viVN}>
        <div className="chart-wrapper">
          <RangePicker onChange={handleDateChange} format="DD/MM/YYYY" />
          {loading ? (
              <Spin className="loading-indicator" />
          ) : series[0].data.length > 0 ? (
              <ReactApexChart
                  options={options}
                  series={series}
                  type="area"
                  height={350}
              />
          ) : (
              <div className="no-data">Không có dữ liệu để hiển thị</div>
          )}
        </div>
      </ConfigProvider>
  );
};

export default ApexChart;
