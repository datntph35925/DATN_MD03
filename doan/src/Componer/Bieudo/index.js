import React, { useState } from "react";
import ReactApexChart from "react-apexcharts";
import "./index.scss";

const ApexChart = () => {
  const monthDataSeries1 = {
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

  const [options] = useState({
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
      text: "Fundamental Analysis of Stocks",
      align: "left",
    },
    subtitle: {
      text: "Price Movements",
      align: "left",
    },
    labels: monthDataSeries1.dates,
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

  const [series] = useState([
    {
      name: "STOCK ABC",
      data: monthDataSeries1.prices,
    },
  ]);

  return (
    <div className="apex-chart-container">
      <ReactApexChart
        options={options}
        series={series}
        type="area"
        height={350}
      />
    </div>
  );
};

export default ApexChart;
