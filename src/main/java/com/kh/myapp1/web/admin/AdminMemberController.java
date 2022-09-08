package com.kh.myapp1.web.admin;

import com.kh.myapp1.domain.Member;
import com.kh.myapp1.domain.admin.AdminMemberSVC;
import com.kh.myapp1.web.admin.form.member.AddForm;
import com.kh.myapp1.web.admin.form.member.EditForm;
import com.kh.myapp1.web.admin.form.member.MemberForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

  private final AdminMemberSVC adminMemberSVC;

  //등록화면
  @GetMapping("/add")
  public String addForm(Model model){
    // addform (이후 form로 바뀜) 빈 객체를 던져줌으로 html에서 thymeleaf를 이용한 ${} *{} 사용 가능
    model.addAttribute("form", new AddForm());

    return "admin/member/addForm";  //가입 화면
  }

  //등록처리	POST	/members/add
  // @Valid 유효성 체크. AddForm객체에서 @NotBlank 사용
  // @ModelAttribute AddForm addForm       : 모델의 속성으로 추가 (타입은 AddForm)
  // BindingResult bindingResult           : 오류 정보 체크
  // RedirectAttributes redirectAttributes : 리다이렉트 시 정보를 유지하기 위해 사용
  @PostMapping("/add")
  public String add(
      @Valid @ModelAttribute("form") AddForm addForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes){

    log.info("addForm={}",addForm);
    //검증
    if(bindingResult.hasErrors()){
      log.info("erros={}",bindingResult);
      return "admin/member/addForm";
    }
    //회원아이디 중복 체크
    Boolean isExist = adminMemberSVC.dupChkOfMemberEmail(addForm.getEmail());
    if(isExist) {
      //필드 오류
      bindingResult.rejectValue("email","dup.email","동일한 이메일이 존재합니다.");
      return "admin/member/addForm";
    }

    //회원 등록
    Member member = new Member();
    member.setEmail(addForm.getEmail());
    member.setPw(addForm.getPw());
    member.setNickname(addForm.getNickname());
    Member insertedMember = adminMemberSVC.insert(member);

    Long id = insertedMember.getMemberId();
    redirectAttributes.addAttribute("id",id);

    return "redirect:/admin/members/{id}"; //회원 상세
  }



  // @Valid 유효성 체크. AddForm객체에서 @NotBlank 사용
  // @ModelAttribute AddForm addForm : 모델의 속성으로 추가 (타입은 AddForm)
  // BindingResult bindingResult : 오류 정보 체크
  public String add2(
      @Valid @ModelAttribute AddForm addForm, BindingResult bindingResult){
    //검증
    //model.addAttribute("addForm", addForm);
    log.info("addForm={}",addForm);

    // 이메일 길이가 null 이거나 0이면 addForm으로 다시
    //if(addForm.getEmail() == null || addForm.getEmail().trim().length() == 0){
    //  return "admin/member/addForm_old";
    //}


    if(bindingResult.hasErrors()){
      log.info("erros={}",bindingResult);
      return "admin/member/addForm";
    }

    // 비즈니스 규칙
    // 1) 이메일에 @가 없으면 오류
    if(!addForm.getEmail().contains("@")){

      bindingResult.rejectValue("email","emailChk1","이메일형식에 맞지 않습니다.");
      return "admin/member/addForm";
    }
    if(addForm.getEmail().length() > 5){

      bindingResult.rejectValue("email","emailChk2",
          new String[]{"0","5"}/*0,5 이메일 자리수*/, "이메일 길이가 초과!");
      return "admin/member/addForm";
    }

    //2) objectError 2개이상의 필드 분석을 통해 오류검증
    //   비밀번호,별칭의 자리수가 모두가 5미만인경우
    if(addForm.getPw().trim().length() < 5 && addForm.getNickname().trim().length() < 5){
      bindingResult.reject("memberChk",new String[]{"5"},"비밀번호,별칭의 자리수가 모두 5 미만입니다.");
      return "admin/member/addForm";
    }


    //회원 등록
    Member member = new Member();
    member.setEmail(addForm.getEmail());
    member.setPw(addForm.getPw());
    member.setNickname(addForm.getNickname());
    Member insertedMember = adminMemberSVC.insert(member);

    return "redirect:/admin/members/"+insertedMember.getMemberId(); //회원 상세
  }



  //조회화면
  @GetMapping("/{id}")
  public String findById(@PathVariable("id") Long id, Model model) {

    Member findedMember = adminMemberSVC.findById(id);

    MemberForm memberForm = new MemberForm();
    memberForm.setMemberId(findedMember.getMemberId());
    memberForm.setEmail(findedMember.getEmail());
    memberForm.setPw(findedMember.getPw());
    memberForm.setNickname(findedMember.getNickname());
    memberForm.setCdate(findedMember.getCdate());
    memberForm.setUdate(findedMember.getUdate());

    //memberForm -> form 으로 바꾸어 일괄적으로 사용
    model.addAttribute("form",memberForm);

    return "admin/member/memberForm"; //회원 상세화면
  }
  //수정화면
  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable("id") Long id, Model model){

    Member findedMember = adminMemberSVC.findById(id);

    EditForm editForm = new EditForm();
    editForm.setMemberId(findedMember.getMemberId());
    editForm.setEmail(findedMember.getEmail());
    editForm.setPw(findedMember.getPw());
    editForm.setNickname(findedMember.getNickname());

    model.addAttribute("form", editForm);
    return "admin/member/editForm"; //회원 수정화면
  }
  //수정처리	POST	/members/{id}/edit
  @PostMapping("/{id}/edit")
  public String edit(
      @PathVariable("id") Long id,
      @Valid @ModelAttribute("form") EditForm editForm,
      BindingResult bindingResult){

    //검증
    if(bindingResult.hasErrors()) {
      return "admin/member/editForm";
    }
    Member member = new Member();
    member.setPw(editForm.getPw());
    member.setNickname(editForm.getNickname());

    int updatedRow = adminMemberSVC.update(id,member);
    if(updatedRow == 0) {
      return "admin/member/editForm";
    }
    return "redirect:/admin/members/{id}"; //회원 상세화면
  }

  //삭제처리
  @GetMapping("/{id}/del")
  public String del(@PathVariable("id") Long id){
    int deletedRow = adminMemberSVC.del(id);
    if(deletedRow == 0){
      return "redirect:/admin/members/"+id;
    }
    return "redirect:/admin/members/all"; //삭제 후 목록 화면으로
  }


  //목록화면	GET	/members
  @GetMapping("/all")
  public String all(Model model){

    List<Member> list = adminMemberSVC.all();
    model.addAttribute("list", list);

    return "admin/member/all";
  }
}
