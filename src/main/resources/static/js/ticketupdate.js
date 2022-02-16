//
//var tageJson =new Object();
//var tageJsonArray = new Array();
//
//var tsJson = new Object();
//var tseatJsonArray = new Array();
//
//var totalcount = 2;
//var count = 2;
//function btnseatup(place){
//
//        var tageJson = new Object();
//        var tseatJson = new Object();
//
//        const style = document.getElementById(place).style;
//
//        if(style.backgroundColor=='red'){
//            document.getElementById(place).style.backgroundColor = 'silver';
//
//            count--;
//
//            const index = tseatJsonArray.findIndex(x => x.seat === place);
//
//            if (index !== undefined) tseatJsonArray.splice(index, 1)
//        }else if(totalcount!=count){
//            count++;
//            tseatJson.seat = place;
//            document.getElementById(place).style.backgroundColor = 'red';
//            tseatJsonArray.push(tseatJson);
//            tsJson.tseat = tseatJsonArray;
//        }
//        alert(JSON.stringify(tsJson));
//}

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



















