package com.kh.myapp1.web;

import com.kh.myapp1.domain.Product;
import com.kh.myapp1.domain.svc.ProductSVC;
import com.kh.myapp1.web.form.SaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  // 그냥 쓰면 오류 -> @RequiredArgsConstructor 사용
  private  final ProductSVC productSVC;



  //등록양식
  @GetMapping
  public String saveForm(Model model){

    return "product/saveForm"; // 상품등록 view
  }

  //등록처리
  @PostMapping
  public String saver(SaveForm saveForm){
    log.info("saveForm : {}", saveForm);

    Product product = new Product();

    product.setPname(saveForm.getPname());
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());

    Integer productId = productSVC.save(product);

    return "redirect:/product/"+productId; //상품상세 view
  }

  //상품개별조회
  @GetMapping("/{pid}")
  public String findByProductId(@PathVariable("pid") String pid) {
    // db에서 상품 조회
    return "product/detailForm";
  }

  //수정 양식
  @GetMapping("/{pid}/edit")
  public String updateForm(){

    return "product/updateForm"; // 상품 수정 view
  }

  // 수정 처리
  @PostMapping("/{pid}/edit")
  public  String update() {

    return "redirect:/product/1"; // 상품 상세 view
  }

  //삭제 처리
  @GetMapping("/{pid}/del")
  public String delete() {

    return "redirect:/product/all"; // 전체 목록 view
  }

  //목록 화면
  @GetMapping("/all")
  public String list() {

    return "product/all"; // 전체 목록 view
  }
}
