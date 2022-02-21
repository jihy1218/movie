
function moviewrite(){
            //폼태그 가져오기
            var formData = new FormData(form);
            $.ajax({
                type:"post",
                url:"/admin/moviewritecontroller",
                data:
                    formData
                ,
                processData: false,
                contentType: false,
                success: function(data){
                    if(data==1){
                        alert("영화등록되었습니다.");
                        location.href = "/admin/adminmain";
                    }

                }
            })
}


function movieinfodiv(){
    var mvid = $("#mvid").val();
    $.ajax({
        type:"POST",
        url : "/admin/moviewriteinfo",
        data : {
            "mvid" : mvid
        },
        success: function(data){
           document.getElementById("movietable").innerHTML =
           "<tr><td width='200'>영화제목</td><td>"+data["movieNm"]+"<td></tr>"+
           "<tr><td>상영시간</td><td>"+data["showTm"]+"<td></tr>"+
           "<tr><td>개봉일</td><td>"+data["openDt"]+"<td></tr>"+
           "<tr><td>국가</td><td>"+data["nations"]+"<td></tr>"+
           "<tr><td>장르</td><td>"+data["genres"]+"<td></tr>"+
           "<tr><td>감독</td><td>"+data["directors"]+"<td></tr>"+
           "<tr><td>배우</td><td>"+data["actors"]+"<td></tr>"+
           "<tr><td>배급사</td><td>"+data["companyNm"]+"<td></tr>"+
           "<tr><td>등급</td><td>"+data["watchGradeNm"]+"<td></tr>";
        }
    })
}

    // 관 좌석 모음
    var cnemaJsonArray = new Array();

function cnemabtn(location){
    var cnemaJson = new Object();
    const style = document.getElementById(location).style;

    if(style.backgroundColor=='red'){
        document.getElementById(location).style.backgroundColor = 'white';
        //인덱스 번호 찾기
        const index = cnemaJsonArray.findIndex(x => x.location === location);
        //
        if (index !== undefined) cnemaJsonArray.splice(index, 1)
    }else {
        cnemaJson.location = location;
        document.getElementById(location).style.backgroundColor = 'red';
        cnemaJsonArray.push(cnemaJson);
    }
    var sJson = JSON.stringify(cnemaJsonArray);


}

// 관 등록
function cnemawrite(){
    var cnema= new Object();
    var cnematype = $("#cnematype").val();
    var cnemaname = $("#cnemaname").val();
    cnema.cnemalocation = cnemaJsonArray;
    $.ajax({
        url : "/admin/cnemawritecontroller" ,
        data : {
            "cnema" : JSON.stringify(cnema),
            "cnematype" : cnematype,
            "cnemaname" : cnemaname
        },
        contentType: "application/json" ,  //  ajax 타입
        dataType : 'JSON',
        success: function(data){
            if(data==1){
                alert("등록되었습니다.");
                location.href = "/admin/adminmain";
            }

        }

    })
}

function screenregister(){

    var ddate = $("#ddate").val();
    var dtime = $("#dtime").val();
    var endtime = $("#endtime").val();
    var dcnema = $("#dcnema").val();
    var dmovie = $("#dmovie").val();

    if(ddate==""){
        alert("날짜를 입력해주세요.")
        return;
    }else if(dtime==""){
        alert("시작시간을 입력해주세요.")
        return;
    }else if(endtime==""){
         alert("종료시간을 입력해주세요.")
         return;
    }else if(dcnema=="관선택"){
          alert("관을 선택해주세요.")
          return;
    }else if(dmovie=="영화선택"){
           alert("영화를 선택해주세요.")
           return;
    }
    var date1 = ddate.split('-')[0];
    var date2 = ddate.split('-')[1].replace(/(^0+)/, "");
    var date3 = ddate.split('-')[2].replace(/(^0+)/, "");
    ddate = date1 +'-'+date2+'-'+date3;
    $.ajax({
        url : "/admin/screenregister",
        data : {
            "ddate" : ddate,
            "dtime" : dtime,
            "cno" : dcnema,
            "mvno" : dmovie,
            "endtime" : endtime
        },
        success: function(data){
            if(data==1) {
                alert("상영시간이 성공적으로 등록되었습니다.");
                location.href = "/admin/adminmain";
            }else{
                alert("오류 발생 [관리자에게 문의]");
            }
        }
    })
}

function admindelete(type , no){
    if (confirm("정말 삭제하시겠습니까??") == true){
            $.ajax({
                url: "/admin/delete",
                data:{
                    "type" : type,
                    "no" : no
                },
                success: function(data){
                    if(data==1){
                        alert("삭제되었습니다");
                        location.reload();
                    }else{
                        alert("삭제에러");
                    }
                }

            })
    }else{
        return;
    }
}

function  typeupdate(pno,ptype) {
    $.ajax({
        url : "/admin/typeupdate",
        data : {"pno" : pno , "ptype" : ptype},
        success : function (result){
            if(result==1){
                alert("상태가 변경되었습니다.");
                location.reload();
            }else{
                $("#updatemsg").html("현재 상태와 동일한 상태입니다.");
            }
        }
    });

}



function datesearch(){
   var date1 = $("#date1").val();
   var date2 = $("#date2").val();

   document.getElementById("datasearch1").value = date1;
   document.getElementById("datasearch2").value = date2;
}

/*어드민 관 더보기*/
function movieadd(){

    const table = document.getElementById('spreadsheet1');   // '@@'에는 테이블이름 넣기
    const tbody = table.tBodies[0].rows.length-1;         //지금 갯수
      $.ajax({
        url: "/admin/cnemaadd" ,
        data : { "tbody":tbody } ,
        success : function( data ){               //데이타가  httml임
           $("#spreadsheet1>tbody").append(data);
        }
      });

}
/*어드민 영화 더보기 */
/*어드민 관 더보기*/
function movieadd2(){


    const table = document.getElementById('spreadsheet2');   // '@@'에는 테이블이름 넣기
    const tbody = table.tBodies[0].rows.length-1;         //지금 갯수
      $.ajax({
        url: "/admin/movieadd" ,
        data : {"tbody" : tbody } ,
        success : function( data ){               //데이타가  httml임
           $("#spreadsheet2>tbody").append(data);
        }
      });

}
function movieadd3(){

    const table = document.getElementById('spreadsheet3');   // '@@'에는 테이블이름 넣기
    const tbody = table.tBodies[0].rows.length-1;         //지금 갯수
      $.ajax({
        url: "/admin/movieinfoadd" ,
        data : {"tbody" : tbody } ,
        success : function( data ){               //데이타가  httml임
           $("#spreadsheet3>tbody").append(data);
        }
      });

}






















