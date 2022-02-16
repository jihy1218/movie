
$(document).ready(function(){
    var mvno = document.getElementById("movieno").value;
    // 우선 컨텍스트를 가져옵니다.
    var ctx = document.getElementById("sexChart").getContext('2d');
    /*
    - Chart를 생성하면서,
    - ctx를 첫번째 argument로 넘겨주고,
    - 두번째 argument로 그림을 그릴때 필요한 요소들을 모두 넘겨줍니다.
    */

    $.ajax({
        url: '/movie/getsexpercent',
        data:{
            'mvno' : mvno
        },
        success: function(data){

            var myChart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: ["남성", "여성"],
                    datasets: [{
                        label: '예매율:',
                        data: [data["mpercent"], data["wpercent"]],
                        backgroundColor: [
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 99, 132, 0.2)'
                        ],
                        borderColor: [
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 99, 132, 0.2)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    maintainAspectRatio: false, // default value. false일 경우 포함된 div의 크기에 맞춰서 그려짐.
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero:true
                            }
                        }]
                    }
                }
            });
        }

    });

    var ctx2 = document.getElementById("ageChart").getContext('2d');
    /*
    - Chart를 생성하면서,
    - ctx를 첫번째 argument로 넘겨주고,
    - 두번째 argument로 그림을 그릴때 필요한 요소들을 모두 넘겨줍니다.
    */

    $.ajax({
        url: '/movie/getagepercent',
        data:{
            'mvno' : mvno
        },
        success: function(data){

            var myChart = new Chart(ctx2, {
                type: 'bar',
                data: {
                    labels: ["10대","20대", "30대" ,"40대이상"],
                    datasets: [{
                        label: '예매율:',
                        data: [data["teen"], data["twenties"] ,data["thirties"],data["older"]],
                        backgroundColor: [
                            '#3e95cd',
                            '#8e5ea2',
                            "#e8c3b9",
                            "#c45850"
                        ],
                        borderColor: [
                            '#3e95cd',
                            '#8e5ea2',
                            "#e8c3b9",
                            "#c45850"
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    maintainAspectRatio: false, // default value. false일 경우 포함된 div의 크기에 맞춰서 그려짐.
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero:true
                            }
                        }]
                    }
                }
            });
        }

    })

});

// 댓글 등록


    function replywrite(){

     var rcontents =$("#rcontents").val();
     var mvno =$("#mvno").val();

     //댓글내용 미 입력시 댓글 저장 막기
        if(rcontents==""){alert("댓글 내용을 입력해주세요!");return;}
         $.ajax({
            url: "/movie/replywrite/",
            data:{"mvno" : mvno ,"rcontents" : rcontents},
            success : function( data ){

           if(data==1){
            window.location.reload();
            //댓글 입력창 공백
            $("#rcontents").val("");
            }else if(data ==2){
                alert("로그인후 사용 가능합니다");return;
            }
            }
        });

    }

    //댓글등록 끝
    //댓글 삭제
    function replydelete(rno){

    $.ajax({
        url:"/movie/replydelete/",
        data:{"rno":rno},
        success: function(result){
            if(result==1){
            alert("삭제 되었습니다")
            window.location.reload();
            }else{
            alert("삭제 실패되었습니다")
            }

        }

    });

}
