drop table member;
create table member (
	member_id 	number(8),
	email		varchar2(40),
	pw 			varchar2(10),
	nickname 	varchar2(30),
	cdate 		timestamp default systimestamp,
	udate 		timestamp default systimestamp
);

-- 기본키 [ member_id ]
alter table member add constraint member_member_id_pk primary key(member_id);

-- 유니크 [ email ]
alter table member add constraint member_email_unique unique (email);

-- NOT NULL [ nickname, cdate, udate ]
alter table member modify nickname constraint member_nickname_nn not null;
alter table member modify cdate constraint member_cdate_nn not null;
alter table member modify udate constraint member_udate_nn not null;

-- CHECK (패스워드 8~10자리) [ pw ]
-- alter table member add constraint member_pw_chk check (check >= 8 and check <= 10);

drop sequence member_member_id_seq;
create sequence member_member_id_seq;

-- 생성
insert into member (member_id, email, pw, nickname)
    values (member_member_id_seq.nextval, 'test1@test.com', '1234', '별칭1');

insert into member (member_id, email, pw, nickname)
    values (member_member_id_seq.nextval, 'test2@test.com', '1234', '별칭2');

insert into member (member_id, email, pw, nickname)
    values (member_member_id_seq.nextval, 'test3@test.com', '1234', '별칭3');

-- 조회
select * from member;

select * from member where email = 'test@test.com';


-- 수정
update member
    set pw = 5678,
        nickname = '별칭4',
        udate = systimestamp
    where email = 'test1@test.com';
-- 수정 시간(udate)를 systimestamp를 사용하여 업데이트

commit;

select member_id, email, pw, nickname, round(cdate), round(udate) from member;

-- 삭제
delete from member
    where email = 'test1@test.com';

rollback;










