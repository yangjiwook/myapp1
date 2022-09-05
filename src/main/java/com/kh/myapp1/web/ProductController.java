package com.kh.myapp1.web;

import com.kh.myapp1.domain.Product;
import com.kh.myapp1.domain.svc.ProductSVC;
import com.kh.myapp1.web.form.product.EditForm;
import com.kh.myapp1.web.form.product.ItemForm;
import com.kh.myapp1.web.form.product.SaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  // 그냥 쓰면 오류 -> @RequiredArgsConstructor 사용
  private  final ProductSVC productSVC;



  //등록양식
  @GetMapping("/add")
  public String saveForm(Model model){

    return "product/addForm"; // 상품등록 view
  }

  //등록처리
  @PostMapping("/add")
  public String saver(SaveForm saveForm){
    log.info("saveForm : {}", saveForm);

    Product product = new Product();

    product.setPname(saveForm.getPname());
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());

    Product savedProduct = productSVC.save(product);

    // redirect:/ => url에 대한 경로 요청
    return "redirect:/products/"+savedProduct.getProductId(); //상품상세 view

  }


  //상품개별조회
  @GetMapping("/{pid}")
  public String findByProductId(
      @PathVariable("pid") Long pid,
      Model model
  ) {
    // db에서 상품 조회
    Product findedProduct = productSVC.findById(pid);

    // Product => ItemForm 복사
    ItemForm itemForm = new ItemForm();
    itemForm.setProductId(findedProduct.getProductId());
    itemForm.setPname(findedProduct.getPname());
    itemForm.setQuantity(findedProduct.getQuantity());
    itemForm.setPrice(findedProduct.getPrice());

    //view 에서 참조하기위해 model 객체에 저장
    model.addAttribute("itemForm", itemForm);


    //view에 대한 요청
    return "product/itemForm";
  }



  //수정 양식
  @GetMapping("/{pid}/edit")
  public String updateForm(@PathVariable("pid") Long pid, Model model){

    Product findedProduct = productSVC.findById(pid);

    //Product => EditForm 변환
    EditForm editForm = new EditForm();
    editForm.setProductId(findedProduct.getProductId());
    editForm.setPname(findedProduct.getPname());
    editForm.setQuantity(findedProduct.getQuantity());
    editForm.setPrice(findedProduct.getPrice());

    model.addAttribute("editForm", editForm);

    return "product/editForm"; // 상품 수정 view
  }

  // 수정 처리
  @PostMapping("/{pid}/edit")
  public  String update(@PathVariable("pid") Long pid, EditForm editForm) {

    Product product = new Product();
    product.setProductId(pid);
    product.setPname(editForm.getPname());
    product.setQuantity(editForm.getQuantity());
    product.setPrice(editForm.getPrice());

    productSVC.update(pid, product);

    return "redirect:/products/"+pid; // 상품 상세 url
  }



  //삭제 처리
  @GetMapping("/{pid}/del")
  public String delete(@PathVariable("pid") Long pid) {

    productSVC.delete(pid);

    return "redirect:/products"; // 전체 목록 view
  }



  //목록 화면
  @GetMapping
  public String list(Model model) {

    List<Product> list = productSVC.findAll();
    model.addAttribute("list", list);

    return "product/all"; // 전체 목록 view
  }
}
