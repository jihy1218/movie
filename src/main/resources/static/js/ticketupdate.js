function ticketcancel(tno){

    $.ajax({
        url : "/admin/ticketcancel",
        data:{
            "tno" : tno
        },
        success : function(data){
            if(data==1){
                alert("취소되었습니다");
                location.reload();
            }else{
                alert("에러");
            }

        }


    })
}



















