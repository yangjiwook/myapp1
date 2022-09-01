package com.kh.myapp1.web.form;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor // 멤버필드에 대한 모든 생성자
@NoArgsConstructor
public class EditForm {
  private Long productId;   // 상품아이디
  private String pname;     // 상품명 PNAME VARCHAR2 30
  private Integer quantity; // 수량  QUANTITY NUMBER 10
  private Integer price;    // 가격 PRICE NUMBER 10
}
