/* 결제 start*/
function payment(){
    var IMP = window.IMP; // 생략 가능
    IMP.init("imp96520987"); // 예: imp00000000
    // IMP.request_pay(param, callback) 결제창 호출
    IMP.request_pay({ // param
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "ORD20180131-0000011",
        name: "영화관좌석비",
        amount: 7000,
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
                url : "", // 결제시넘어갈 거 생각
                success : function(result){
                    alert("결제완료");
                    // 결제완료 페이지
                    location.href="/";
                }
            });
        }
    });
}
/*결제 end*/