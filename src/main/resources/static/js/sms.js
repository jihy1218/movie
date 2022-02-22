function sendsms(){
    var movieNm = $("#movieNm").html();
    var cinema = $("#cinema").html();
    var movieTime = $("#movieTime").html();
    var movieSeat = $("#movieSeat").html();
    var phoneNumber = $("#phoneNumber").val();
    alert(phoneNumber);
    $.ajax({
        url : "/movie/sms",
        data: {"movieNm" : movieNm, "cinema" : cinema , "movieTime" : movieTime, "movieSeat" : movieSeat, "phoneNumber" : phoneNumber},
        success : function (result){
            if(result==1){
                alert("SMS로 전송되었습니다.");
            }
        }
    });
}

