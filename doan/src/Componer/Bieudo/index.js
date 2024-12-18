import React, { useEffect, useState } from "react";
import ReactApexChart from "react-apexcharts";
import { ConfigProvider, DatePicker } from "antd";
import viVN from "antd/es/locale/vi_VN";
import "./index.scss";

const { RangePicker } = DatePicker;

const ApexChart = () => {
  // Khởi tạo state cho revenueData, giả sử có 'dates' và 'prices'
  const [revenueData, setRevenueData] = useState({
    dates: [],
    prices: [],
  });

  const [options, setOptions] = useState({
    chart: {
      type: "area",
      height: 350,
      zoom: {
        enabled: false,
      },
    },
    dataLabels: {
      enabled: false,
    },
    stroke: {
      curve: "straight",
    },
    title: {
      text: "Thống kê doanh thu",
      align: "left",
    },
    xaxis: {
      type: "datetime",
      categories: revenueData.dates, // Sử dụng revenueData.dates
    },
    yaxis: {
      opposite: true,
    },
    legend: {
      horizontalAlign: "left",
    },
  });

  const [series, setSeries] = useState([
    {
      name: "Doanh thu",
      data: revenueData.prices, // Sử dụng revenueData.prices
    },
  ]);

  // Hàm lấy dữ liệu revenueData (giả sử bạn lấy từ API)
  const fetchRevenueData = async () => {
    try {
      // Giả sử API trả về dữ liệu có dạng { dates: [], prices: [] }
      const response = await fetch("API_URL"); // Thay thế "API_URL" với URL thực tế của bạn
      const data = await response.json();
      setRevenueData(data);
    } catch (error) {
      console.error("Lỗi khi lấy dữ liệu:", error);
    }
  };

  // Lấy dữ liệu khi component được mount
  useEffect(() => {
    fetchRevenueData();
  }, []);

  // Xử lý thay đổi ngày từ RangePicker
  const handleDateChange = (dates) => {
    if (!dates || dates.length < 2) return;

    const [start, end] = dates;
    const startDate = start.startOf("day").valueOf();
    const endDate = end.endOf("day").valueOf();

    const filteredDates = revenueData.dates.filter(
        (date) =>
            new Date(date).getTime() >= startDate &&
            new Date(date).getTime() <= endDate
    );
    const filteredPrices = revenueData.prices.slice(
        revenueData.dates.findIndex(
            (date) => new Date(date).getTime() >= startDate
        ),
        revenueData.dates.findIndex(
            (date) => new Date(date).getTime() <= endDate
        ) + 1
    );

    // Cập nhật biểu đồ ngay lập tức
    setOptions((prev) => ({
      ...prev,
      xaxis: {
        ...prev.xaxis,
        categories: filteredDates,
      },
    }));
    setSeries([
      {
        name: "Doanh thu",
        data: filteredPrices,
      },
    ]);
  };

  return (
      <ConfigProvider locale={viVN}>
        <div className="chart-wrapper">
          <RangePicker
              onChange={handleDateChange}
              format="DD/MM/YYYY" // Định dạng ngày hiển thị
          />
          <ReactApexChart
              options={options}
              series={series}
              type="area"
              height={350}
          />
        </div>
      </ConfigProvider>
  );
};

export default ApexChart;
