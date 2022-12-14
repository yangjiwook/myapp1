package com.kh.myapp1.domain.dao;

import com.kh.myapp1.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO{
  // implement 생성 시 알트 엔터 누르고 생성할때
  // copy java doc 체크하면 주석도 따라옴

  //@RequiredArgsConstructor : 멤버필드 중에서 final 객체에 대한 생성자 생성
  private final JdbcTemplate jt;

  /**
   * 가입
   *
   * @param member 가입정보
   * @return 회원아이디
   */
  @Override
  public int insert(Member member) {
    int result = 0;
    StringBuffer sql = new StringBuffer();
    sql.append("insert into member (member_id,email,pw,nickname) ");
    sql.append("values(?,?,?,?) ");

    //데이터 변경이 일어나면 update
    result = jt.update(sql.toString(), member.getMemberId(), member.getEmail(), member.getPw(), member.getNickname());

    return result;
  }

  /**
   * 신규 회원아이디(내부관리용) 생성
   * @return 회원아이디
   */
  public Long generateMemberId(){
    String sql = "select member_member_id_seq.nextval from dual ";
    //단일 레코드, 단일 컬럼 -> queryForObject()
    Long memberId = jt.queryForObject(sql, Long.class);
    return memberId;
  }

  /**
   * 조회 by 회원아이디
   *
   * @param memberId 회원아이디
   * @return 회원정보
   */
  @Override
  public Member findById(Long memberId) {
    StringBuffer sql = new StringBuffer();

    sql.append("select member_id,email,pw,nickname,cdate,udate ");
    sql.append("  from member ");
    sql.append(" where member_id = ? ");

    //레코드는 하나, 컬럼은 여러개
    Member findedMember = null;
    try {
      //BeanPropertyRowMapper는 매핑되는 자바클래스에 디폴트생성자 필수!
      findedMember = jt.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Member.class),memberId);
    } catch (DataAccessException e) {
      log.info("찾고자하는 회원이 없습니다!={}",memberId);
    }

    return findedMember;
  }

  /**
   * 수정
   *
   * @param memberId 아이디
   * @param member   수정할 정보
   */
  @Override
  public int update(Long memberId, Member member) {
    int result = 0;
    StringBuffer sql = new StringBuffer();
    sql.append("update member ");
    sql.append("   set nickname = ?, ");
    sql.append("       pw = ?, ");
    sql.append("       udate = systimestamp ");
    sql.append(" where member_id = ? ");

    // 변경은 update
    result = jt.update(sql.toString(),member.getNickname(),member.getPw(),memberId);
    return result;
  }

  /**
   * 탈퇴
   *
   * @param memberId 아이디
   */
  @Override
  public int del(Long memberId, String pw) {
    int result = 0;
    String sql = "delete from member where member_id = ? and pw = ? ";

    result = jt.update(sql, memberId, pw);
    return result;
  }

}
