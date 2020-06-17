$(function(){
   $("#comment table").hide(); //1
   var page =1; //더 보기에서 보여줄 페이지를 기억할 변수
   var count=0; //전체 댓글 개수
   var maxPage=getMaxPage(); // 댓글의 총 페이지수를 구합니다.
   if(count != 0) //댓글 갯수가 0이 아니면
      getList(1); //첫 페이지의 댓글을 구해 옵니다. (한 페이지에 3개씩 가져옵니다.)
   else {   //댓글이 없는 경우
      $("#message").text("등록된 댓글이 없습니다.");
   };
   
   //최대 몇 페이지를 가질 수 있는지 계산합니다.
   function getMaxPage(){
      //글의 총 갯수 - 등록 또는 삭제 시 댓글의 총 갯수 변화가 있기 때문에 갯수를 구합니다.
      count = parseInt($("#count").text());
      
      //예) 총 리스트가 5개이면 최대 페이지는 2입니다.
      //int maxpage = (listcount + limit -1) / limit;
      return parseInt((count + 3 - 1) / 3);
   }
   
   function getList(currentPage){
      $.ajax({
         type : "post",
         url : "CommentList.bo",
         data : {
            "board_num" : $("#board_num").val(),
            "page" : currentPage
         },
         dataType : "json",
         success : function(rdata){
            if(rdata.length > 0){
               $("#comment table").show(); //문서가 로딩될 때 hide() 했던 부분을 보이게 합니다.(1)
               $("#comment thead").show(); //글이 없을 때 hide() 부분을 보이게 합니다.(2)
               output = '';
               $(rdata).each(function(){
                     img = '';
                     if ($("#loginid").val() == this.id){
                        img = "&nbsp;&nbsp;<img src='resources/image/pencil.png' width='15px' class='update btnh'>"
                           +"&nbsp;&nbsp;<img src='resources/image/remove.png' width='15px' class='remove btnh'>"
                           +"<input type='hidden' value='" + this.num  + "' >";
                     }
                     output += "<tr><td>" + this.id + "</td>";
                     output += "<td>" + this.content + "</td>";
                     output += "<td>" + this.reg_date + img + "</td></tr>";
               }); //each end
               $("#comment tbody").append(output);
               
               console.log("현재:"+currentPage);
               console.log("max:"+maxPage);
               //현재 페이지가 마지막 페이지면 "더보기"는 나타나지 않습니다.
               if(currentPage == maxPage){
                  $("#message").text("");
               } else {
                  $("#message").text("더보기");
               }
               
               //더보기를 클릭할 경우 현재 페이지에서 1증가된 페이지를 보여주기 위해 값을 설정합니다.
               page = currentPage+1;
            }else{
               $("#message").text("등록된 댓글이 없습니다.");
               $("#comment thead").hide();      // 2
               $("#comment tbody").empty();   //데이터가 한 개인 경우 삭제하면서 tbody를 비웁니다.
            }
         }
      });//ajax end
   }//function end
   
   //더 보기를 클릭하면 page 내용이 추가로 보여집니다.
   $("#message").click(function(){
      getList(page);
   }); //click end
   
   //글자 수 50개 제한하는 이벤트
   $("#content").on('input', function(){
      length = $(this).val().length;
      if(length > 50){
         length = 50;
         content = content.substring(0, length);
      }
      $(".float-left").text(length + "/50")
   });
   
   //등록 또는 수정 완료 버튼을 클릭한 경우
   //버튼의 라벨이 '등록'인 경우는 댓글을 추가하는 경우
   //버튼의 라벨이 '수정완료'인 경우는 댓글을 수정하는 경우
   $("#write").click(function(){
      buttonText = $("#write").text(); //버튼의 라벨로 add할지 update할지 결정
      content = $("#content").val();
      $(".float-left").text('총 50자까지 가능합니다.');
      
      if(buttonText == "등록"){   //댓글을 추가하는 경우
         url = "CommentAdd.bo";
         data = {
               "content" : content,
               "id" : $("#loginid").val(),
               "board_num" : $("#board_num").val()
         };
      } else {   //댓글을 수정하는 경우
         url = "CommentUpdate.bo";
         data = {
               "num" : num,
               "content" : content
         };
         $("#write").text("등록"); //다시 등록으로 변경
      }
      
      $.ajax({
         type : "post",
         url : url,
         data : data,
         success : function(result){
            $("#content").val('');
            if(result == 1){
               $("#comment tbody").empty();
               if(url == "CommentAdd.bo"){
                  $("#count").text(++count); //등록을 클릭하면 전체 댓글 갯수 증가합니다.
                  maxPage = getMaxPage(); //maxPage 다시 구합니다.
               }
               getList(1); //등록, 수정완료 후 첫 페이지 보여줍니다.
            }
         }
      }); //ajax end
   }); //$("#write") end
   
   //ajax로 생겨난 태그들을 on 매개변수 2개로 하면 인식하지 못한다.
   //ajax로 생겨났으면 3개짜리 매개변수를 무조건 사용해야한다.
   
   //pencil2.png 클릭
   $('#comment').on('click', '.update', function(){
      before = $(this).parent().prev().text(); //선택한 내용을 가져옵니다.
      $("#content").focus().val(before); //textarea에 수정전 내용을 보여줍니다.
      num = $(this).next().next().val(); //수정할 댓글번호를 저장합니다.
      $("#write").text("수정완료"); //등록버튼의 라벨을 '수정완료'로 변경합니다.
      $(this).parent().parent().css('background', 'lightgrey'); //수정할 행의 배경색을 변경합니다.
   });

   //remove.png를 클릭하는 경우
   $('#comment').on('click', '.remove', function(){
      var num = $(this).next().val(); //댓글 번호
      $.ajax({
         type : "post",
         url : "CommentDelete.bo",
         data : {
            "num" : num
         },
         success : function(result){
            if(result == 1){
               $("#comment tbody").empty();
               $("#count").text(--count); //삭제하면 전체 댓글 갯수 한 개 줄어듭니다.
               maxPage=getMaxPage();//maxPage 다시 구합니다.
               getList(1);
            }
         }
      })
   })
});

