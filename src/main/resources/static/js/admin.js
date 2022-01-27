

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
                    alert(data);
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
