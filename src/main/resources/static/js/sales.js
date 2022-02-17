function salesdata(){
    var year = $("#years").val();
    $.ajax({
        url : "/admin/salesdata",
        data : {"year" : year},
        success : function (result){
            $(function() {
                var ctx = document.getElementById('chart').getContext('2d');
                var chart = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
                        datasets: [{
                            label: '매출',
                            yAxisID:'y-left',
                            order : 2,
                            type : 'bar', // 'bar' type, 전체 타입과 같다면 생략가능
                            backgroundColor: 'rgb(255, 204, 102)',
                            borderColor: 'rgb(255, 204, 102)',
                            data: [result[0].split('@')[0],
                                result[1].split('@')[0],
                                result[2].split('@')[0],
                                result[3].split('@')[0],
                                result[4].split('@')[0],
                                result[5].split('@')[0],
                                result[6].split('@')[0],
                                result[7].split('@')[0],
                                result[8].split('@')[0],
                                result[9].split('@')[0],
                                result[10].split('@')[0],
                                result[11].split('@')[0]]
                        }, {
                            label: '티켓판매량',
                            type : 'line',         // 'line' type
                            order : 1,
                            yAxisID: 'y-right',
                            Color : 'black',
                            fill : false,         // 채우기 없음
                            lineTension : 0,  // 0이면 꺾은선 그래프, 숫자가 높을수록 둥글해짐
                            pointRadius : 0,    // 각 지점에 포인트 주지 않음
                            backgroundColor: 'rgb(142, 188, 199)',
                            borderColor: 'rgb(142, 188, 199)',
                            data: [result[0].split('@')[1],
                                result[1].split('@')[1],
                                result[2].split('@')[1],
                                result[3].split('@')[1],
                                result[4].split('@')[1],
                                result[5].split('@')[1],
                                result[6].split('@')[1],
                                result[7].split('@')[1],
                                result[8].split('@')[1],
                                result[9].split('@')[1],
                                result[10].split('@')[1],
                                result[11].split('@')[1]]
                        }]
                    },
                    // Configuration options
                    options: {
                        legend: {
                            labels: {
                                Color: 'black' // label color
                            }
                        },
                        scales: {
                            // y축
                            'y-left' : {
                                min : 0,
                                max : 3000000,
                                stacked: true,
                                ticks: {
                                    Color:'black', // y축 폰트 color
                                }
                            },
                            'y-right' : {
                                position: 'right',
                                ticks: {
                                }
                            },
                            // x축
                            xAxes: [{
                                stacked: true,
                                ticks: {
                                    fontColor:'white' // x축 폰트 color
                                }
                            }]
                        }
                    }
                });
            });
        }
    });
}
