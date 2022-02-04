function monthselect() {

    const today = new Date();

    const setCalendarData = (year, month) => {
        //빈 문자열을 만들어줍니다.
        let calHtml = "";

        const month1 = document.getElementById("monthselect1").value;
        month = parseInt(month1.split('-')[1]);
        $("#calendar").load();
        //var monthtest = document.getElementById("monthselect").value;

        //getMonth(): Get the month as a number (0-11)
        //month 인자는 getMonth로 구한 결과 값에 1을 더한 상태이므로
        //다시 1을 뺀 값을 Date 객체의 인자로 넘겨줍니다.
        //그러면 오늘 날짜의 Date 객체가 반환됩니다.
        const setDate = new Date(year, month - 1, 1);
        //getDate(): Get the day as a number (1-31)
        //이번 달의 첫째 날을 구합니다
        const firstDay = setDate.getDate();
        //getDay(): Get the weekday as a number (0-6)
        //이번 달의 처음 요일을 구합니다.
        const firstDayName = setDate.getDay();
        //new Date(today.getFullYear(), today.getMonth(), 0);
        //Date객체의 day 인자에 0을 넘기면 지난달의 마지막 날이 반환됩니다.
        //new Date(today.getFullYear(), today.getMonth(), 1);
        //Date객체의 day 인자에 1을 넘기면 이번달 첫째 날이 반환됩니다.
        //이번 달의 마지막 날을 구합니다.
        const lastDay = new Date(
            today.getFullYear(),
            setDate.getMonth() + 1,
            0
        ).getDate();

        //지난 달의 마지막 날을 구합니다.
        const prevLastDay = new Date(
            today.getFullYear(),
            today.getMonth(),
            0
        ).getDate();
        //매월 일수가 달라지므로 이번 달 날짜 개수를 세기 위한 변수를 만들고 초기화 합니다.
        let startDayCount = 1;
        let lastDayCount = 1;
        //1~6주차를 위해 6번 반복합니다.
        for (let i = 0; i < 6; i++) {
            //1~6주차를 위해 6번 반복합니다.
            for (let j = 0; j < 7; j++) {
                // i == 0: 1주차일 때
                // j < firstDayName: 이번 달 시작 요일 이전 일 때
                if (i == 0 && j < firstDayName) {
                    //일요일일 때, 토요일일 때, 나머지 요일 일 때
                    if (j == 0) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<div style='background-color:#FFB3BB;' class='calendar__day horizontalGutter'><span>${(prevLastDay - (firstDayName - 1) + j)}</span><span></span></div>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<div style='background-color:#FFB3BB;' class='calendar__day'><span>${(prevLastDay - (firstDayName - 1) + j)}</span><span></span></div>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<div style='background-color:#FFB3BB;' class='calendar__day horizontalGutter'><span>${(prevLastDay - (firstDayName - 1) + j)}</span><span></span></div>`;
                    }
                }
                    // i == 0: 1주차일 때
                // j == firstDayName: 이번 달 시작 요일일 때
                else if (i == 0 && j == firstDayName) {
                    if (j == 0) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button  onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span></button>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    }
                }
                    // i == 0: 1주차일 때
                // j > firstDayName: 이번 달 시작 요일 이후 일 때
                else if (i == 0 && j > firstDayName) {
                    if (j == 0) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    }
                }
                //일요일일 때, 토요일일 때, 나머지 요일 일 때
                else if (i > 0 && startDayCount <= lastDay) {
                    if (j == 0) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)})" id="${year}/${month}/${setFixDayCount(startDayCount)}" style='background-color:#BBFFC9;' class='calendar__day verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'/'+${month}+'/'+${setFixDayCount(startDayCount)}+'/'+0)" id="${year}/${month}/${setFixDayCount(startDayCount)}/0"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    }
                }
                // startDayCount > lastDay: 이번 달의 마지막 날 이후일 때
                else if (startDayCount > lastDay) {
                    if (j == 0) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<div style='background-color:#B9E1FF;' class='calendar__day horizontalGutter verticalGutter'><span>${lastDayCount++}</span><span></span></div>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<div style='background-color:#B9E1FF;' class='calendar__day verticalGutter'><span>${lastDayCount++}</span><span></span></div>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<div style='background-color:#B9E1FF;' class='calendar__day horizontalGutter verticalGutter'><span>${lastDayCount++}</span><span></span></div>`;
                    }
                }
            }
        }
        //캘린더 div 태그에 내용 붙임
        document.getElementById("calendar").innerHTML=calHtml;
    };

    const setFixDayCount = number => {
        //캘린더 하루마다 아이디를 만들어주기 위해 사용
        let fixNum = "";
        if (number < 10) {
            fixNum = "" + number;
        } else {
            fixNum = number;
        }
        return fixNum;
    };


    if (today.getMonth() + 1 < 10) {
        setCalendarData(today.getFullYear(), "" + (today.getMonth() + 1));
    } else {
        setCalendarData(today.getFullYear(), "" + (today.getMonth() + 1));
    }
}
var count=0;
var day2=null;
function dateclick(day) {
        if(count==0){ // 날짜를 처음 클릭했을때
            document.getElementById(day).style.backgroundColor="red"; // 빨간색으로 만들어줌
            if(day2!=null){
                document.getElementById(day2).style.backgroundColor="#BBFFC9";
            }
            count++; // 카운트 추가
            day2=day; // day2라는 빈 객체에 day를 넣음
            $("#datediv").load(location.href+" #datediv");
        }else {
            document.getElementById(day2).style.backgroundColor="#BBFFC9"; // day2를 원래 색(평일)으로 변경
            document.getElementById(day).style.backgroundColor="red"; // 선택한 날을 빨간색으로바꿈
            count=0; // 카운트는 0으로
            day2=day; // day2에 다시 day를 넣어줌
            $("#datediv").load(location.href+" #datediv");
        }
    }