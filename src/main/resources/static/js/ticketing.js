/* 결제 start*/
function payment(ctype,dno){

    var ageJson = new Object();
    ageJson.youth = ycount;
    ageJson.adult = acount;
    alert(dno);
    var price = 0;
    if(ctype=="IMAX"){
        price = (10000+imx)*acount+(8000+imx)*ycount;
    }else if(ctype == "3D"){
        price = (10000+threed)*acount+(8000+threed)*ycount;
    }else{
       price = 10000*acount+8000*ycount;
    }

    var IMP = window.IMP; // 생략 가능
    IMP.init("imp96520987"); // 예: imp00000000
    // IMP.request_pay(param, callback) 결제창 호출
    IMP.request_pay({ // param
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "ORD20180131-0000011",
        name: "영화관좌석비",
        amount: price,
        buyer_email: "gildong@gmail.com",
        buyer_name: "가입시받아온이름",
        buyer_tel: "가입시받아온폰",
        buyer_addr: "주소",
        buyer_postcode: "상세주소"
    }, function (rsp) {
        if(rsp.success) {

        }else{// 결제 실패했을시
            // 테스트 : 결제 성공이라고 생각
            $.ajax({
                url : "/movie/ticketingcontroller", // 결제시넘어갈 거 생각
                data : {
                    "tseat" : JSON.stringify(tsJson),
                    "tage" : JSON.stringify(ageJson),
                    "tprice" : price,
                    "dno":dno,
                    "count" : ycount+acount
                },
                success : function(result){
                    if(result!=0){
                        alert("결제완료");
                        location.href="/movie/reservation/"+result;
                    }else{
                        alert("결제오류");
                    }
                }
            });
        }
    });
}
/*결제 end*/



function btnpcount(age,type){
    var adultcount = document.getElementById("adultcount").innerHTML;
    var youthcount = document.getElementById("youthcount").innerHTML;
    var ageJson =new Object();

    if(age=='adult'&&type=='-'){
        if(adultcount!=0){
            document.getElementById("adultcount").innerHTML=Number(adultcount)-1;
            acount--;
        }
    }else if(age=='adult'&&type=='+'){
        document.getElementById("adultcount").innerHTML= Number(adultcount)+1;
        acount++;
    }else if(age=='youth'&&type=='-'){
        if(youthcount!=0){
            document.getElementById("youthcount").innerHTML=Number(youthcount)-1;
            ycount--;
        }
    }else{
        document.getElementById("youthcount").innerHTML= Number(youthcount)+1;
        ycount++;
    }
    if(tseatJsonArray!=""){
        $("#ticketbox").load(location.href+" #ticketbox");
        tseatJsonArray = new Array();
        acount = 0;
        ycount = 0;
        count = 0;
    }

}

var acount = 0;
var ycount = 0;

var tageJson =new Object();
var tageJsonArray = new Array();

var tsJson = new Object();
var tseatJsonArray = new Array();

var adult = 10000;
var youth = 8000;
var imx = 4000;
var threed  = 2000;



var count = 0;
function btntcount(place){

    var totalcount = Number(acount)+Number(ycount);

        if(totalcount==0){
            alert("인원을 선택해 주십시오.");
            return;
        }

    var tageJson = new Object();
    var tseatJson = new Object();
    //alert(totalcount);

    const style = document.getElementById(place).style;

    if(style.backgroundColor=='red'){
        document.getElementById(place).style.backgroundColor = 'silver';

        count--;

        const index = tseatJsonArray.findIndex(x => x.seat === place);

        if (index !== undefined) tseatJsonArray.splice(index, 1)
    }else if(totalcount>count) {
        count++;
        tseatJson.seat = place;
        document.getElementById(place).style.backgroundColor = 'red';
        tseatJsonArray.push(tseatJson);
        tsJson.tseat = tseatJsonArray;
    }
    //alert(count);
    //alert(JSON.stringify(tseatJsonArray));
    //alert(JSON.stringify(tsJson));
    //alert(tseatJsonArray)

        if(totalcount==count&&totalcount!=0){
            document.getElementById("paymentbtn").style.display = '';
        }else{
            document.getElementById("paymentbtn").style.display = 'none';
        }
}

function ticketing(ctype){
    var totalcount = Number(acount)+Number(ycount);
    var moviename = document.getElementById("moviename").innerHTML;
    var tdate = document.getElementById("tdate").innerHTML;
    var cnema = document.getElementById("cnema").innerHTML;
    var seatlist = "선택한 좌석 :";


    tsJson.tseat.forEach((item)=>{
      seatlist += " "+item.seat;
    })
    var price = 0;
    if(ctype=="IMAX"){
        price = (10000+imx)*acount+(8000+imx)*ycount;
    }else if(ctype == "3D"){
        price = (10000+threed)*acount+(8000+threed)*ycount;
    }else{
       price = 10000*acount+8000*ycount;
    }


    document.getElementById("resultmname").innerHTML = '선택한 영화 : '+moviename;
    document.getElementById("resultdate").innerHTML = '선택한 날짜 : '+tdate;
    document.getElementById("resulttime").innerHTML = '관/종류 : '+cnema;
    document.getElementById("resultcount").innerHTML = '성인 : '+acount+' 청소년 : '+ycount;
    document.getElementById("resultplace").innerHTML = seatlist;
    document.getElementById("resultprice").innerHTML = '총가격 : '+price;

}


























