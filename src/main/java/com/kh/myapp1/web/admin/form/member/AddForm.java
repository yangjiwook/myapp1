package com.kh.myapp1.web.admin.form.member;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AddForm {
  //https://hibernate.org/validator/
  @NotBlank
  @Email(regexp = ".+@.+\\..+")//정규표현식 //(message = "이메일형식이아닙니다 example@google.com")
  private String email;     // 이메일
  @NotBlank
  @Size(min = 0, max = 10)
  private String pw;        // 비밀번호
  @Size(min = 0, max = 10)
  private String nickname;  // 별칭
}
