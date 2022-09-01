package com.kh.myapp1.domain.dao;

import com.kh.myapp1.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j //lombok
@Repository //framework
@RequiredArgsConstructor // final 필드를 생성자의 매개변수로 해서 생성자를 만들어줌
public class ProductDAOImpl implements ProductDAO{
  // 생성자 주입 (?)
  private final JdbcTemplate jt;

//  ProductDAOImpl(JdbcTemplate jt){
//    this.jt = jt;
//  } // => @RequiredArgsConstructor


//  //등록
//  @Override
//  public Integer save(Product product) {
//    //DB 연결을 위한 쿼리문이 필요
//    StringBuffer sql = new StringBuffer();
//    //바뀌는 값들은 ? 로 변경
//    sql.append("insert into product values(product_product_id_seq.nextval, ?, ?, ?) ");
//    //sql.append("insert into product (product_id, pname, quantity, price) ");
//    //sql.append("values (product_product_id_seq.nextval, '모니터', 20, 900000) ");
//
//    class PreparedStatementCreatorImpl implements PreparedStatementCreator {
//      @Override
//      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"product_id"});
//        // ? 에 들어갈 값
//        pstmt.setString(1, product.getPname());
//        pstmt.setInt(2, product.getQuantity());
//        pstmt.setInt(3,product.getPrice());
//
//        return pstmt;
//      }
//    }
//
//    KeyHolder keyHolder = new GeneratedKeyHolder();
//    jt.update(new PreparedStatementCreatorImpl(), keyHolder);
//    //keyholder 에서 key 가 product_id인 값을 가져옴
//    Integer product_id = Integer.valueOf(keyHolder.getKeys().get("product_id").toString());
//
//    return product_id;
//  }

////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public Product save(Product product) {

    StringBuffer sql = new StringBuffer();
    sql.append("insert into product values(product_product_id_seq.nextval, ?, ?, ?) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jt.update(new PreparedStatementCreator(){

      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"product_id"});

        pstmt.setString(1, product.getPname());
        pstmt.setInt(2, product.getQuantity());
        pstmt.setInt(3,product.getPrice());

        return pstmt;
      }
    }, keyHolder);

    Long product_id = Long.valueOf(keyHolder.getKeys().get("product_id").toString());

    product.setProductId(product_id);
    return product;
  }


  // 조회
  @Override
  public Product findById(Long productId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select product_id, pname, quantity, price ");
    sql.append("from product ");
    sql.append("where product_id = ? ");

    //productId 자리는 고정되지않은 변수가 들어가는 자리
    Product product = null;
    try {
      product = jt.queryForObject(
          sql.toString(), new BeanPropertyRowMapper<>(Product.class), productId);
    } catch (DataAccessException e) {
      log.info("삭제대상 상품이 없습니다. 상품아이디={}", productId);
    }

    return product;
  }

  // 수정
  @Override
  public void update(Long productId, Product product) {
    StringBuffer sql = new StringBuffer();
    sql.append("update product ");
    sql.append("   set pname = ?, ");
    sql.append("       quantity = ?, ");
    sql.append("       price = ? ");
    sql.append(" where product_id = ? ");
    // ? 는 가변 변수

    jt.update(sql.toString(), product.getPname(), product.getQuantity(), product.getPrice(), product.getProductId());

  }

  // 삭제
  @Override
  public void delete(Long productId) {
    String sql = "delete from product where product_id = ? ";

    jt.update(sql, productId);
  }

  // 전체 조회
  @Override
  public List<Product> findAll() {
    StringBuffer sql = new StringBuffer();

    sql.append("select product_id, pname, quantity, price ");
    sql.append(" from product ");

    // case 1) [자동 매핑]  sql 결과 레코드와 동일한 구조의 java 객체가 존재할 경우
    // Bean(자바가 관리하는객체)PropertyRowMapper<> => 레코드 결과가 BeanProperty에 자동 매핑
    // jt.query(sql.toString(), new BeanPropertyRowMapper<Product>());
    List<Product> result = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Product.class));

    // case 2) [수동 매핑] sql 결과 레코드의 컬럼명과 java 객체의 멤버이름이 다른 경우 or 타입이 다른 경우
//    List<Product> result =
//        jt.query(sql.toString(), new RowMapper<Product>() {
//          @Override
//          public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
//            Product product = new Product();
//
//            product.setProductId(rs.getLong("product_id"));
//            product.setQuantity(rs.getInt("quantity"));
//            product.setPrice(rs.getInt("price"));
//
//            return product;
//          }
//        });


    return result;
  }


////////////////////////////////////////////////////////////////////////////////////////

//  @Override
//  public Integer save(Product product) {
//
//    StringBuffer sql = new StringBuffer();
//
//    sql.append("insert into product values(product_product_id_seq.nextval, ?, ?, ?) ");
//
//
//    KeyHolder keyHolder = new GeneratedKeyHolder();
//    jt.update(
//      con -> {
//        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"product_id"});
//
//        pstmt.setString(1, product.getPname());
//        pstmt.setInt(2, product.getQuantity());
//        pstmt.setInt(3,product.getPrice());
//
//        return pstmt;
//      }
//    , keyHolder);
//
//    Integer product_id = Integer.valueOf(keyHolder.getKeys().get("product_id").toString());
//
//    return product_id;
//  }

  // 전체 삭제
  @Override
  public void deleteAll() {
    String sql = "delete from product ";
    int rows = jt.update(sql);
    log.info("삭제건수 : {}", rows);
  }

  // 상품 아이디 생성
  @Override
  public Long generatePid() {
    String sql = "select product_product_id_seq.nextval from dual ";
    Long newProductId = jt.queryForObject(sql, Long.class); //단일 레코드 단일 컬럼일때
    return newProductId;
  }
}
