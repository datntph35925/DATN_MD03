import React, { useEffect, useState } from "react";
import ReactApexChart from "react-apexcharts";
import { ConfigProvider, DatePicker } from "antd";
import viVN from "antd/es/locale/vi_VN";
import "./index.scss";

const { RangePicker } = DatePicker;

const ApexChart = ({ revenueData }) => {
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
      categories: revenueData.dates || [],
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
      data: revenueData.prices || [],
    },
  ]);

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
