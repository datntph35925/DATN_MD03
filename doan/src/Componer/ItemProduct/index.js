import React from "react";
import { Card, Row, Col } from "antd";
import "./index.scss";

const Index = () => {
  return (
    <div className="container-product">
      <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
        <Col className="gutter-row" xs={24} sm={12} md={8} lg={4}>
          <Card size="small" className="product-card">
            <div className="price-sold">
              <p className="price">₫</p>
              <p className="sold-count">Đã bán: </p>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Index;
