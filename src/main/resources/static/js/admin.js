

function moviewrite(){
            //폼태그 가져오기
            var formData = new FormData(form);
            alert(formData);
            alert($("mvid").val());
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
    alert(mvid);
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
        document.getElementById(location).style.backgroundColor = '';
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
    alert(sJson);

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
            if(data==1){
                alert("성공");
                location.href = "/admin/adminmain";
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



