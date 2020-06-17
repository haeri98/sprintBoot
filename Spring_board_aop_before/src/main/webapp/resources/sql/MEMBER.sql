drop table member;

create table MEMBER(
	id			varchar2(15),
	password	varchar2(10),
	name		varchar2(15),
	age			NUMBER,
	gender		varchar2(5),
	email		varchar2(30),
	PRIMARY KEY (id)
);

alter table Member modify (password varchar2(60));

select * from member;
update member set password=1 where id='admin';
insert into member values ('jsp', 1, 'jsp', 21, '³²', 'jsp@naver.com');
insert into member values ('java', 1, 'java', 25, '¿©', 'java@naver.com');
select * from member;

delete from member;