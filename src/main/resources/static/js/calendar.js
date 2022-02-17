// 기본 달력 세팅
    const today = new Date();

    const setCalendarData = (year, month) => {
        //빈 문자열을 만들어줍니다.
        let calHtml = "";
        const month1 = document.getElementById("monthselect1").value;
        month = parseInt(month1.split('-')[1]);
        $("#calendar").load();
        //var monthtest = document.getElementById("monthselect").value;
        const setDate = new Date(year, month - 1, 1);
        //getDate(): Get the day as a number (1-31)
        /*month = "0"+month;*/
        //이번 달의 첫째 날을 구합니다
        const firstDay = setDate.getDate();
        const firstDayName = setDate.getDay();
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
                            `<button  onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span></button>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    }
                }
                    // i == 0: 1주차일 때
                // j > firstDayName: 이번 달 시작 요일 이후 일 때
                else if (i == 0 && j > firstDayName) {
                    if (j == 0) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    }
                }
                //일요일일 때, 토요일일 때, 나머지 요일 일 때
                else if (i > 0 && startDayCount <= lastDay) {
                    if (j == 0) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else if (j == 6) {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}" style='background-color:#BBFFC9;' class='calendar__day verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                    } else {
                        // 스타일링을 위한 클래스 추가
                        calHtml +=
                            `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
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
        // 캘린더 div 태그 내용 삽입
        document.getElementById("calendar").innerHTML=calHtml;
    };

    const setFixDayCount = number => {
        //캘린더 하루마다 아이디를 만들어주기 위해 사용
        let fixNum = "";
        if (number < 10) {
            fixNum = number;
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
// 달력 선택후 셋팅
function monthselect() {

const today = new Date();

const setCalendarData = (year, month) => {
    //빈 문자열을 만들어줍니다.
    let calHtml = "";
    const month1 = document.getElementById("monthselect1").value;
    month = parseInt(month1.split('-')[1]);
    $("#calendar").load();
    //var monthtest = document.getElementById("monthselect").value;
    const setDate = new Date(year, month - 1, 1);
    //getDate(): Get the day as a number (1-31)
    /*month = "0"+month;*/
    //이번 달의 첫째 날을 구합니다
    const firstDay = setDate.getDate();
    const firstDayName = setDate.getDay();
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
                        `<button  onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span></button>`;
                } else if (j == 6) {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                } else {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                }
            }
                // i == 0: 1주차일 때
            // j > firstDayName: 이번 달 시작 요일 이후 일 때
            else if (i == 0 && j > firstDayName) {
                if (j == 0) {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                } else if (j == 6) {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                } else {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"   style='background-color:#BBFFC9;' class='calendar__day horizontalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                }
            }
            //일요일일 때, 토요일일 때, 나머지 요일 일 때
            else if (i > 0 && startDayCount <= lastDay) {
                if (j == 0) {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                } else if (j == 6) {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}" style='background-color:#BBFFC9;' class='calendar__day verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
                } else {
                    // 스타일링을 위한 클래스 추가
                    calHtml +=
                        `<button onclick="dateclick(${year}+'-'+${month}+'-'+${setFixDayCount(startDayCount)})" disabled="disabled" id="${year}-${month}-${setFixDayCount(startDayCount)}"  style='background-color:#BBFFC9;' class='calendar__day horizontalGutter verticalGutter'><span>${startDayCount}</span><span id='${year}${month}${setFixDayCount(startDayCount++)}'></span></button>`;
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
    // 캘린더 div 태그 내용 삽입
    document.getElementById("calendar").innerHTML=calHtml;
};

const setFixDayCount = number => {
    //캘린더 하루마다 아이디를 만들어주기 위해 사용
    let fixNum = "";
    if (number < 10) {
        fixNum = number;
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

// 영화 선택했을때 달력 버튼 활성화용
var movieselectcount=0;
var moviedate2=null;
function movieselect(mvno,movieNm){
    $("#selectmovieresult").html(movieNm);
    $("#selectmvnoresult").html(mvno);
    $("#selectdateresult").html("날짜 선택");
    $("#selecttimeresult").html("");
    $("#finalDno").html("");
    $.ajax({
        url : "/movie/movieselect",
        data : {"mvno": mvno},
        success : function (result){
            $("#moviedate").html(result);
            var moviedate = $("#moviedate").html();
            var day = moviedate.split(',');
            if(moviedate2!=null){
                var day2 = moviedate2.split(',');
            }
            if(movieselectcount==0){
                if(moviedate2!=null){
                    for(var j=0;j<day2.length;j++){ // 기존에 있던 날짜
                        $("#"+day2[j]).attr("disabled",true); // 버튼을 못쓰게 해줌
                    }
                }
                for(var i=0 ; i<day.length; i++){ // 새로 선택받은 날짜

                    $("#"+day[i]).attr("disabled",false); // 버튼을 쓰게 해줌
                }
                moviedate2 = $("#moviedate").html();
                movieselectcount++;
            }else{
                for(var i=0 ; i<day2.length; i++){
                    $("#"+day2[i]).attr("disabled",true); // 버튼을 쓰게 해줌
                }
                for(var j=0;j<day.length;j++){
                    $("#"+day[j]).attr("disabled",false); // 버튼을 못쓰게 해줌
                }
                moviedate2 = $("#moviedate").html();
                movieselectcount=0;
            }
        }
    });
}

// 날짜 선택용
var count=0;
var day2=null;
function dateclick(day) {
    $("#selectdateresult").html("");
    $("#selecttimeresult").html("");
    $("#finalDno").html("");
    var mvno = $("#selectmvnoresult").html();
    $.ajax({
        url : "/movie/dateselect",
        data : {"day" : day,"mvno":mvno},
        success : function (result){
            var json = JSON.stringify(result["movie"]);           // 제이슨 가져오기
            var content = JSON.parse(json);                               // 제이슨 분리
            var dtimelist = "";
            for(var i=0; i<content.length;i++) {
                if (i == content.length - 1) {
                    dtimelist += JSON.stringify(content[i]["dtime"]).replace(/\"/gi, "");
                } else {
                    dtimelist += JSON.stringify(content[i]["dtime"]).replace(/\"/gi, "") + ",";
                }
            }
            if(count==0){ // 날짜를 처음 클릭했을때
                $("#selectdateresult").html(day);
                // 시간 버튼 출력
                for(var k=0;k<dtimelist.split(',').length;k++){
                    $("#time"+k).html("<span style='font-weight: bold;'>"+dtimelist.split(',')[k]+"</span><br>"+"<span style='margin-right: 20px;'>"+JSON.stringify(content[k]["dseat"])+"/"+JSON.stringify(content[k]["fullseat"])+"</span>"+"<span>"+JSON.stringify(content[k]["cno"])+"관</span>"+"<span style='display: none;'>@"+JSON.stringify(content[k]["dno"])+"</span>"); // 시간 적기
                    $("#time"+k).attr('style','background-color:#FCFCFC; border: #dddddd solid 2px; border-radius:3%;');
                }
                for(var j=dtimelist.split(',').length;j<10;j++){
                    $("#time"+j).html(""); // 시간 적기
                    $("#time"+j).attr('style','display:none;');
                }
                count++; // 카운트 추가
                day2=day; // day2라는 빈 객체에 day를 넣음
            }else if(count==1){
                $("#selectdateresult").html(day);
                // 시간 버튼 출력
                for(var k=0;k<dtimelist.split(',').length;k++){
                    $("#time"+k).html("<span style='font-weight: bold;'>"+dtimelist.split(',')[k]+"</span><br>"+"<span style='margin-right: 20px;'>"+JSON.stringify(content[k]["dseat"])+"/"+JSON.stringify(content[k]["fullseat"])+"</span>"+"<span>"+JSON.stringify(content[k]["cno"])+"관</span>"+"<span style='display: none;'>@"+JSON.stringify(content[k]["dno"])+"</span>"); // 시간 적기
                    $("#time"+k).attr('style','background-color:#FCFCFC; border: #dddddd solid 2px; border-radius:3%;');
                }
                for(var j=dtimelist.split(',').length;j<10;j++){
                    $("#time"+j).html(""); // 시간 적기
                    $("#time"+j).attr('style','display:none;');
                }
                count=0; // 카운트는 0으로
                day2=day; // day2에 다시 day를 넣어줌
            }
        }
    });
}
// 시간 선택
function  timeselect(num){
    var time = $("#time"+num).html();
    $("#selecttimeresult").html(time);
    $("#finalDno").html(time.split('@')[1]);
}
// 좌석 선택 화면으로 가기
function  seatselect(session){
    var dno = $("#finalDno").html();
    var movie = $("#selectmovieresult").html();
    var date = $("#selectdateresult").html();
    if(session==null){
        alert("로그인후 예약진행이 가능합니다.");
        location.href="/member/login";
    }else{
        if(movie==""||date==""||dno==""){
            alert("선택되지않은 항목이 있습니다.");
        }else if(movie!=null&&date!=null&&dno!=null){
            if(confirm("좌석을 선택하시겠습니까?")){
                location.href="/movie/ticketingseat/"+dno;
            }
        }
    }
}
