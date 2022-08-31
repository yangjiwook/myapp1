package com.kh.myapp1.test;

import com.kh.myapp1.domain.Product;
import com.kh.myapp1.domain.dao.ProductDAO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

// beforeAll -> before -> test1 -> after -> before -> test2 -> after -> afterAll
// 실행순서는 함수명의 문자, 숫자의 오름차순으로 진행 1 3 4 2 순으로 코드 작성해도 1 2 3 4 순으로 실행
// 클래스 레벨에 @TestMethodOrder, 메소드 레벨 @Order 로 결정

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JunitTest {
  @Autowired
  private ProductDAO productDAO;
  private static Product testProduct;



  @BeforeAll
  static void beforeAll() {
    //log.info("beforeAll() called");
//    for(int i=0; i<3; i++){
//      Product product = new Product();
//      product.setPname("상품"+(i+1));
//      product.setQuantity(10 + i);
//      product.setPrice(10000 + (i*1000));
//
//      productDAO.save(product);
//    }

  }

  @Test
  @Order(1)
  @DisplayName("상품등록")
  void test1() {
    log.info("test1() called");

    Product product = new Product(null,"의자",5,300000);
    testProduct = productDAO.save(product);
    Assertions.assertThat(testProduct).isNotNull();
  }

  @Test
  @Order(2)
  @DisplayName("상품조회")
  void test3() {
    log.info("test3() called");
    Product findedProduct = productDAO.findById(testProduct.getProductId());
    Assertions.assertThat(findedProduct).isEqualTo(testProduct);

  }

  @Test
  @Order(3)
  @DisplayName("상품수정")
  void test2() {
    log.info("test2() called");
    Long productId = testProduct.getProductId();
    Product product = new Product(testProduct.getProductId(), "책상", 7, 700000);
    productDAO.update(productId, product);

    //수정되었는지 확인 위한 상품조회 코드
    Product findedProduct = productDAO.findById(productId); // Id를 가져와서
    Assertions.assertThat(findedProduct).isEqualTo(product); // 수정한 product와 같은지 확인
  }

  @Test
  @Order(4)
  @DisplayName("목록")
  @Disabled // 테스트에서 제외
  void test4() {
    log.info("test4() called");
    List<Product> products = productDAO.findAll();
    Assertions.assertThat(products.size()).isEqualTo(1);
  }

  @Test
  @Order(5)
  @DisplayName("삭제")
  void test5() {
    log.info("test5() called");
    productDAO.delete(testProduct.getProductId());

    // 상품조회 코드를 통해 삭제되었는지 확인
    Product findedProduct = productDAO.findById(testProduct.getProductId());
    Assertions.assertThat(findedProduct).isNull(); // 조회한 상품이 null 이면 삭제 완료
  }



  @BeforeEach
  void beforeEach() {
    log.info("beforeEach() called");
  }

  @AfterEach
  void afterEach() {
    log.info("afterEach() called");
    List<Product> products = productDAO.findAll();
    products.stream().forEach(product -> {
      log.info(" {}", product);
    });
    //log.info("testProduct={}", testProduct);
  }


  @AfterAll
  static void afterAll() {
    log.info("afterAll() called");
  }
}
