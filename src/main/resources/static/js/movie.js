function replywrite(){

 var r_contents =$("#rcontents").val();
 var mvno =$("#mvno").val();
 alert(r_contents);
 alert(mvno);

 //댓글내용 미 입력시 댓글 저장 막기
    if(r_contents==""){alert("댓글 내용을 입력해주세요!");return;}
     $.ajax({
        url: "/movie/replywrite/",
        data:{"mvno" : mvno ,"rcontents" : r_contents},
        success : function(data){
        if(data ==1){
        $('#replytable').load( location.href+' #replytable');
        //댓글 입력창 공백
        $("#r_contents").val("");
        }else if(data ==2){
            alert("로그인후 사용 가능합니다");return;
        }
        }
    });

}
