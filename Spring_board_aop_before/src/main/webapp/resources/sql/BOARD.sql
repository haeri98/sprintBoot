drop table board;
CREATE TABLE BOARD(
   BOARD_NUM		NUMBER,
   BOARD_NAME 		VARCHAR2(30),
   BOARD_PASS 		VARCHAR2(30),
   BOARD_SUBJECT 	VARCHAR2(300),
   BOARD_CONTENT 	VARCHAR2(4000),
   BOARD_FILE 		VARCHAR2(50),	--÷�Ή� ���� �� (����)
   BOARD_ORIGINAL 	VARCHAR2(50),	--÷�ε� ���� ��
   BOARD_RE_REF 	NUMBER,			--�亯 �� �ۼ��� �����Ǵ� ���� ��ȣ
   BOARD_RE_LEV 	NUMBER,			--�亯 ���� ����
   BOARD_RE_SEQ 	NUMBER,			--�亯 ���� ����
   BOARD_READCOUNT 	NUMBER,			--���� ��ȸ��
   BOARD_DATE 		DATE,			--���� �ۼ� ��¥
   PRIMARY KEY (BOARD_NUM)
);

delete board
delete COMMENTS
drop table comments
-----------------------------------------
--board���� page�� limit���� ���� �����͸� ���ؿɴϴ�.
--1�ܰ�
