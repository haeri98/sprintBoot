drop table board;
CREATE TABLE BOARD(
   BOARD_NUM		NUMBER,
   BOARD_NAME 		VARCHAR2(30),
   BOARD_PASS 		VARCHAR2(30),
   BOARD_SUBJECT 	VARCHAR2(300),
   BOARD_CONTENT 	VARCHAR2(4000),
   BOARD_FILE 		VARCHAR2(50),	--첨부됭 파일 명 (가공)
   BOARD_ORIGINAL 	VARCHAR2(50),	--첨부될 파일 명
   BOARD_RE_REF 	NUMBER,			--답변 글 작성시 참조되는 글의 번호
   BOARD_RE_LEV 	NUMBER,			--답변 글의 깊이
   BOARD_RE_SEQ 	NUMBER,			--답변 글의 순서
   BOARD_READCOUNT 	NUMBER,			--글의 조회수
   BOARD_DATE 		DATE,			--글의 작성 날짜
   PRIMARY KEY (BOARD_NUM)
);

delete board
delete COMMENTS
drop table comments
-----------------------------------------
--board에서 page와 limit값에 따른 데이터를 구해옵니다.
--1단계
