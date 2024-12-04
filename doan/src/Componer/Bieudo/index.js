import React, { useState } from "react";
import ReactApexChart from "react-apexcharts";
import { DatePicker, Space, ConfigProvider } from "antd";
import dayjs from "dayjs";
import viVN from "antd/es/locale/vi_VN"; // Import Vietnamese locale
import "./index.scss";

const { RangePicker } = DatePicker;

const ApexChart = () => {
  // Initial data
  const initialData = {
    prices: [31, 40, 28, 51, 42, 109, 100],
    dates: [
      "2023-01-01",
      "2023-02-01",
      "2023-03-01",
      "2023-04-01",
      "2023-05-01",
      "2023-06-01",
      "2023-07-01",
    ],
  };

  const [selectedRange, setSelectedRange] = useState([
    dayjs().add(-7, "d"),
    dayjs(),
  ]);
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
      text: "Thống kê doanh thu", // Vietnamese title
      align: "left",
    },
    labels: initialData.dates,
    xaxis: {
      type: "datetime",
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
      name: "STOCK ABC", // Vietnamese name for series
      data: initialData.prices,
    },
  ]);

  const onRangeChange = (dates, dateStrings) => {
    // Check if the user has selected a valid range
    if (dates && dates.length === 2) {
      setSelectedRange(dates);

      // Update the chart with filtered data based on the selected range
      const filteredData = filterDataByRange(dates[0], dates[1]);
      setOptions({
        ...options,
        labels: filteredData.dates,
      });
      setSeries([
        {
          name: "STOCK ABC", // Vietnamese name for series
          data: filteredData.prices,
        },
      ]);
    }
  };

  // Function to filter the data based on the selected range
  const filterDataByRange = (startDate, endDate) => {
    // Filter the dates and prices based on the range
    const start = startDate.format("YYYY-MM-DD");
    const end = endDate.format("YYYY-MM-DD");

    const filteredDates = initialData.dates.filter(
      (date) => date >= start && date <= end
    );

    const filteredPrices = initialData.prices.slice(
      initialData.dates.indexOf(filteredDates[0]),
      initialData.dates.indexOf(filteredDates[filteredDates.length - 1]) + 1
    );

    return { dates: filteredDates, prices: filteredPrices };
  };

  const rangePresets = [
    {
      label: "7 Ngày qua", // Vietnamese preset label
      value: [dayjs().add(-7, "d"), dayjs()],
    },
    {
      label: "14 Ngày qua", // Vietnamese preset label
      value: [dayjs().add(-14, "d"), dayjs()],
    },
    {
      label: "30 Ngày qua", // Vietnamese preset label
      value: [dayjs().add(-30, "d"), dayjs()],
    },
    {
      label: "90 Ngày qua", // Vietnamese preset label
      value: [dayjs().add(-90, "d"), dayjs()],
    },
  ];

  return (
    <ConfigProvider locale={viVN}>
      {/* Wrap with Vietnamese locale */}
      <div className="chart-wrapper">
        {/* Date Pickers Outside the Chart */}
        <div className="chart-filters">
          <Space direction="vertical" size={12}>
            <RangePicker
              value={selectedRange}
              presets={rangePresets}
              onChange={onRangeChange}
            />
          </Space>
        </div>

        <div className="apex-chart-container">
          <ReactApexChart
            options={options}
            series={series}
            type="area"
            height={350}
          />
        </div>
      </div>
    </ConfigProvider>
  );
};

export default ApexChart;
