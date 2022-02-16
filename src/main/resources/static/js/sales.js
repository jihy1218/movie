$(function() {
    var ctx = document.getElementById('chart').getContext('2d');
    var chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
            datasets: [{
                label: '매출',
                type : 'bar', // 'bar' type, 전체 타입과 같다면 생략가능
                backgroundColor: 'rgb(255, 204, 102)',
                borderColor: 'rgb(255, 204, 102)',
                data: [10, 20, 30, 40, 50, 60,70,80,90,100,110,120]
            }, {
                label: '티켓판매량',
                type : 'line',         // 'line' type
                fill : false,         // 채우기 없음
                lineTension : 0.4,  // 0이면 꺾은선 그래프, 숫자가 높을수록 둥글해짐
                pointRadius : 0,    // 각 지점에 포인트 주지 않음
                backgroundColor: 'rgb(255, 153, 0)',
                borderColor: 'rgb(255, 153, 0)',
                data: [40, 50, 60, 70, 80, 90,100,110,120,130,140,150]
            }]
        },

        // Configuration options
        options: {
            legend: {
                labels: {
                    fontColor: 'white' // label color
                }
            },
            scales: {
                // y축
                yAxes: [{
                    stacked: true,
                    ticks: {
                        fontColor:'white' // y축 폰트 color
                    }
                }],
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