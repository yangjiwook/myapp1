package com.kh.myapp1.web.form.member;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class MemberForm {
  private Long memberId;        // 회원 아이디 (관리용 number)
  private String email;         // 이메일
  private String pw;            // 비밀번호
  private String nickname;      // 별칭

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime cdate;  // 가입일
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime udate;  // 회원정보 수정일
}
