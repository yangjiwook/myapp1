package com.kh.myapp1.domain;

import lombok.Data;

@Data
public class Product {
  private Integer productId;    // 상품번호   PRODUCT_ID	NUMBER(10,0)	No		1
  private String  pname;        // 상품명     PNAME	VARCHAR2(30 BYTE)	Yes		2
  private Integer quantity;     // 상품수량   QUANTITY	NUMBER(10,0)	Yes		3
  private Integer price;        // 상품가격   PRICE	NUMBER(10,0)	Yes		4
}
