package com.kh.myapp1.domain.svc;

import com.kh.myapp1.domain.Product;

// 상품관련 인터페이스
public interface ProductSVC {


  /**
   * 상품등록
   * @param product 상품 정보
   * @return 상품아이디
   */
  Integer save(Product product);
}
