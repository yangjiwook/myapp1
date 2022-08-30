package com.kh.myapp1.domain.dao;

import com.kh.myapp1.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
  public Integer save(Product product) {

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

    Integer product_id = Integer.valueOf(keyHolder.getKeys().get("product_id").toString());

    return product_id;
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


}
