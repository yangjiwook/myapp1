package com.kh.myapp1.web;

import com.kh.myapp1.domain.Member;
import com.kh.myapp1.domain.svc.MemberSVC;
import com.kh.myapp1.web.form.member.AddForm;
import com.kh.myapp1.web.form.member.EditForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/*
* 매핑 : 주소창에 입력할 URL
* return : html 파일 경로
* redirect : URL 경로로 최종 연결 (URL주소)
* */

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberSVC memberSVC;

  //가입화면
  @GetMapping("/add")
  public String addForm() {
    return "member/addForm";
  }

  //가입처리 post /members/add
  @PostMapping("/add")
  public String add(AddForm addForm) {
    //검증
    log.info("addForm {}", addForm);

    Member member = new Member();

    member.setEmail(addForm.getEmail());
    member.setPw(addForm.getPw());
    member.setNickname(addForm.getNickname());

    memberSVC.insert(member);

    return "login/loginForm";
  }

  //조회화면 get  /members/{id}
  @GetMapping("/{id}")
  public String findById(){

    return "member/memberForm"; //회원 상세 화면
  }

  //수정화면 get  /members/{id}/edit
  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable("id") Long id, Model model) {

    Member findedMember = memberSVC.findById(id);
    model.addAttribute("member", findedMember);

    return "member/editForm"+findedMember; // 회원 수정화면
  }

  //수정처리 post /members/{id}/edit
  @PostMapping("/{id}/edit")
  public String edit(@PathVariable("id") Long id, EditForm editForm) {

    Member member = new Member();
    // 수정할 권한이 있는지 확인하기 위한
    member.setPw(editForm.getPw());
    member.setNickname(editForm.getNickname());

    int updateRow = memberSVC.update(id,member);
    if(updateRow == 0){
      return "member/editForm";
    }

    return "redirect:/members/{id}"; //수정하고 나면 회원 상세화면으로
  }

  //탈퇴화면
  @GetMapping("/{id}/del")
  public String delForm() {
    return "member/delForm"; //회원 탈퇴 화면
  }

  //탈퇴처리 post  /members/{id}/del
  @PostMapping("/{id}/del")
  public String del(@PathVariable("id") Long id, @RequestParam("pw") String pw) {
    int deleteRow = memberSVC.del(id, pw);
    if(deleteRow == 0){
      return "member/delForm";
    }

    return "redirect:/";
  }

  //목록화면 get  /members/
  @GetMapping("/all")
  public String all() {

    return "member/all";
  }



  /*
  //가입양식
  @GetMapping("/join")
  public String joinForm(Model model){
    return "member/joinForm";
  }
  //가입처리
  @PostMapping("/join")
  public String join(JoinForm_old joinForm){
    log.info("joinForm : {} ", joinForm);

    Member member = new Member();

//    member.setMemberId(joinForm.getMember_id());
    member.setEmail(joinForm.getEmail());
    member.setPw(joinForm.getPw());
    member.setNickname(joinForm.getNickname());

    Member joinMember = memberSVC.insert(member);

    return "redirect:/members/"+joinMember.getMemberId();
  }
  //회원개별조회
  @GetMapping("/{member_id}")
  public String findByMemberId(
      @PathVariable("member_id") Long member_id,
      Model model
  ) {
    Member findedMember = memberSVC.findById(member_id);

    MemberForm_old memberForm = new MemberForm_old();
    memberForm.setMember_id(findedMember.getMemberId());
    memberForm.setEmail(findedMember.getEmail());
    memberForm.setPw(findedMember.getPw());
    memberForm.setNickname(findedMember.getNickname());
    memberForm.setCdate(findedMember.getCdate());
    memberForm.setUdate(findedMember.getUdate());

    model.addAttribute("memberForm", memberForm);

    return "member/memberForm";
  }

  //수정양식
  @GetMapping("/{member_id}/edit")
  public String editForm(@PathVariable("user_id") Long user_id, Model model){
    Member findedMember = memberSVC.findById(user_id);

    EditMemForm_old editMemForm = new EditMemForm_old();
    editMemForm.setMember_id(findedMember.getMemberId());
    editMemForm.setEmail(findedMember.getEmail());
    editMemForm.setPw(findedMember.getPw());
    editMemForm.setNickname(findedMember.getNickname());
    editMemForm.setCdate(findedMember.getCdate());
    editMemForm.setUdate(findedMember.getUdate());

    model.addAttribute("editMemForm", editMemForm);

    return "member/editMemForm";
  }

  @PostMapping("{member_id}/edit")
  public String update(@PathVariable("member_id")Long member_id, EditMemForm_old editMemForm) {
    Member member = new Member();
    member.setMemberId(member_id);
    member.setEmail(editMemForm.getEmail());
    member.setPw(editMemForm.getPw());
    member.setNickname(editMemForm.getNickname());

    memberSVC.update(member_id, member);

    return "redirect:/members"+member_id;
  }
*/
}
