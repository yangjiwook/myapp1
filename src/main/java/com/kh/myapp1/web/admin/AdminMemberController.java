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
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

  private final AdminMemberSVC adminMemberSVC;

  //등록화면
  @GetMapping("/add")
  public String addForm(){

    return "admin/member/addForm";  //가입 화면
  }
  //등록처리	POST	/members/add
  @PostMapping("/add")
  public String add(AddForm addForm){
    //검증
    log.info("addForm={}",addForm);

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

    model.addAttribute("memberForm",memberForm);

    return "admin/member/memberForm"; //회원 상세화면
  }
  //수정화면
  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable("id") Long id, Model model){

    Member findedMember = adminMemberSVC.findById(id);
    model.addAttribute("member", findedMember);
    return "admin/member/editForm"; //회원 수정화면
  }
  //수정처리	POST	/members/{id}/edit
  @PostMapping("/{id}/edit")
  public String edit(@PathVariable("id") Long id, EditForm editForm){

    Member member = new Member();
    member.setPw(editForm.getPw());
    member.setNickname(editForm.getNickname());

    int updatedRow = adminMemberSVC.update(id,member);
    if(updatedRow == 0) {
      return "admin/member/editForm";
    }
    return "redirect:/members/{id}"; //회원 상세화면
  }
  //탈퇴화면
  @GetMapping("/{id}/del")
  public String delForm(){
    return "admin/member/delForm"; //회원 탈퇴 화면
  }
  //탈퇴처리	GET	/members/{id}/del
  @PostMapping("/{id}/del")
  public String del(@PathVariable("id") Long id, @RequestParam("pw") String pw){
    int deletedRow = adminMemberSVC.del(id,pw);
    if(deletedRow == 0){
      return "admin/member/delForm";
    }
    return "redirect:/";
  }
  //목록화면	GET	/members
  @GetMapping("/all")
  public String all(){

    return "admin/member/all";
  }
}
