package com.kh.myapp1.domain.dao;

import com.kh.myapp1.domain.Member;
import java.util.List;

/*
가입화면
가입처리 Member MemberDAO.join
상세화면 Member MemberDAO.findById
수정화면
수정처리 Member MemberDAO.update
삭제처리 Member MemberDAO.del
목록화면 Member MemberDAO.all
*/
public interface MemberDAO {

  /**
   * 가입
   * @param member 가입정보
   * @return 가입건수
   */
  int insert(Member member);


  /**
   * 조회 by 회원아이디
   * @param memberId 회원아이디
   * @return 회원정보
   */
  Member findById(Long memberId);


  /**
   * 수정
   * @param memberId 회원아이디
   * @param member 수정할 정보
   * @return 수정건수(int)
   */
  int update(Long memberId, Member member);


  /**
   * 탈퇴
   * @param memberId 회원아이디
   * @param pw 비밀번호
   * @return 삭제건수(int)
   */
  int del(Long memberId, String pw);


  /**
   * 목록
   * @return 회원전체
   */
  List<Member> all();

  /**
   * 신규 회원아이디(내부관리용) 생성
   * @return 회원아이디
   */
  Long generateMemberId();

}
