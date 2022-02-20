/* 다음주소 api */
function sample4_execDaumPostcode() {
    new daum.Postcode({
           oncomplete: function(data) {
               // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

               // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
               // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
               var roadAddr = data.roadAddress; // 도로명 주소 변수
               var extraRoadAddr = ''; // 참고 항목 변수

               // 법정동명이 있을 경우 추가한다. (법정리는 제외)
               // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
               if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                   extraRoadAddr += data.bname;
               }
               // 건물명이 있고, 공동주택일 경우 추가한다.
               if(data.buildingName !== '' && data.apartment === 'Y'){
                  extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
               }
               // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
               if(extraRoadAddr !== ''){
                   extraRoadAddr = ' (' + extraRoadAddr + ')';
               }

               // 우편번호와 주소 정보를 해당 필드에 넣는다.
               document.getElementById('sample4_postcode').value = data.zonecode;
               document.getElementById("sample4_roadAddress").value = roadAddr;
               document.getElementById("sample4_jibunAddress").value = data.jibunAddress;


               var guideTextBox = document.getElementById("guide");
               // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
               if(data.autoRoadAddress) {
                   var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                   guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                   guideTextBox.style.display = 'block';

               } else if(data.autoJibunAddress) {
                   var expJibunAddr = data.autoJibunAddress;
                   guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                   guideTextBox.style.display = 'block';
               } else {
                   guideTextBox.innerHTML = '';
                   guideTextBox.style.display = 'none';
               }
           }
       }).open();
}
/* 다음주소 api end */

//일정시간이후부터 실행되는
$( document ).ready(function() {

    $.ajax({
        url:"/member/reviewtime",
        success: function(data){
          for (var i = 0; i < data.length; i++) {
            if (new Date() >= new Date(data[i])) {
              document.getElementById("rvbtn"+data[i]).style.display ="";

            }
          }
        }
    })

        $.ajax({
            url:"/member/reviewtime2",
            success: function(data){
              for (var i = 0; i < data.length; i++) {

              }
            }
        })

});
        var tno = 0;

        function ontno(tno1){
            tno = tno1;
        }


        //리뷰작성
        function review(){
            var reviewcontents = document.getElementById("reviewcontents").value;
            var temp = $(':radio[name="rating"]:checked').val();
            alert(tno);
            if(reviewcontents==""){
                alert("리뷰를 작성해주세요");
                return;
            }

            $.ajax({
                url : "/member/reviewwrite",
                data:{
                    "tno" : tno,
                    "grade" : temp,
                    "reviewcontents" : reviewcontents
                },
                success: function(data){
                   if(data==1){
                    alert("감사합니다");
                    location.reload();
                   }

                }
            })
        }



/*회원가입 유효성 검사*/

$(function(){
    //아이디 유효성 검사
    $("#mid").keyup(function(){
    var mid = $("#mid").val();
    var idj = /^[a-z0-9]{5,15}$/  // 정규 표현식  영소문자 5~15 글자마나 허용

    if(!idj.test(mid)){
        $("#idcheck").html("영소문자 5~15 글자만 가능합니다");
    }else{
       //아이디 중복체크 비동기 통신~!
       $.ajax({
        url :"/member/idcheck",
        data:{"mid":mid},
        success : function(result){
            if(result==1){
                $("#idcheck").html("현재 사용중인 아이디 입니다.");
            }else{
                $("#idcheck").html("사용가능");
            }
        }
       });
    }
    });//키보드 이벤트 함수end


   //패스워드 유효성검사
    $("#mpassword").keyup(function(){
        var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,15}$/;
        // 영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함
        var mpassword = $("#mpassword").val();
        if(!pwj.test(mpassword)){
            $("#passwordcheck").html("영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함")
        }else {
            $("#passwordcheck").html("사용가능");
        }
    });//키보드 이벤트 함수end

    //패스워드 확인 유효성 검사
    $("#mpasswordconfirm").keyup(function(){
        var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/;
               // 숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
               var mpassword = $("#mpassword").val();
               var mpasswordconfirm = $("#mpasswordconfirm").val();
                if(!pwj.test(mpasswordconfirm)){
                    $("#passwordconfirmcheck").html("숫자', '문자', '특수문자' 포함 , '최소 8문자~16글자 허용.");
                }else if(mpassword != mpasswordconfirm){
                    $("#passwordconfirmcheck").html("서로 패스워드가 다릅니다");
                }else{
                    $("#passwordconfirmcheck").html("사용가능");
                }
    });//키보드 이벤트 함수 end

    //이름 유효성검사
    $("#mname").keyup(function(){
        var namej = /^[A-Za-z가-힣]{1,15}$/;
        var mname = $("#mname").val();
        if(!namej.test(mname)){
            $("#namecheck").html("영대문자/한글 1~15 허용");
        }else{
            $("#namecheck").html("사용가능");
        }
    });//키보드 이벤트 함수 end

    //연락처 유효성검사
    $("#mphone").keyup(function(){
         var phonej = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/; // 연락처
         var mphone = $("#mphone").val();
         if(!phonej.test(mphone)){
            $("#phonecheck").html("01x-xxxx-xxxx 형식으로 입력해주세요");
         }else{
            $("#phonecheck").html("사용가능");
         }
    });//키보드 이벤트 함수 end

    //이메일 유효성 검사
    $("#memail").keyup(function(){
         var emailj = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
         var memail = $("#memail").val();
         if(!emailj.test(memail)){
            $("#emailcheck").html("이메일 형식으로 입력해주세요");

         }else{
            //이메일 중복 체크 비동기 통신
            $.ajax({
            url : "/member/emailcheck",
            data : {"memail" : memail},
            success :  function(result){
                if(result==1){
                    $("#emailcheck").html("현재 사용중인 이메일 입니다")
                }else{
                    $("#emailcheck").html("사용가능");
                }
            }
            });// ajax함수 end
         }
    });//키보드 이벤트 함수 end

  // 주소에 / 입력 제한
          $("#sample4_postcode").keyup( function(){
                 var address1 = $("#sample4_postcode").val();
                 if(  address1.indexOf("/") != -1 ){ $("#addresscheck").html(" 주소에 '/' 특수문자 포함 불가 "); return false;   }
                 if( address1 != null ) {  $("#addresscheck").html("사용가능"); }
          });
          $("#sample4_roadAddress").keyup( function(){
                   var address2 = $("#sample4_roadAddress").val();
                     if(  address2.indexOf("/") != -1 ){  $("#addresscheck").html("주소에 '/' 특수문자 포함 불가 "); return false;     }
                     if( address2 != null ) {  $("#addresscheck").html("사용가능"); }
          });
          $("#sample4_jibunAddress").keyup( function(){
                   var address3 = $("#sample4_jibunAddress").val();
                     if(  address3.indexOf("/") != -1 ){  $("#addresscheck").html(" 주소에  '/' 특수문자 포함 불가 "); return false;     }
                     if( address3 != null ) {  $("#addresscheck").html("사용가능"); }
            });
          $("#sample4_detailAddress").keyup( function(){
                    var address4 = $("#sample4_detailAddress").val();
                      if(  address4.indexOf("/") != -1 ){ $("#addresscheck").html("주소에  '/' 특수문자 포함 불가 "); return false;     }
                     if( address4 != null ) {  $("#addresscheck").html("사용가능"); }
           });

    //전 부 체크
    $("#submit1").click(function(){
     if( ! $('input[name=signupsign]').is(":checked") ) {
       alert(" 회원가입 약관 동의시 회원가입이 가능합니다 . ");
     }else if( ! $('input[name=infosign]').is(":checked") ) {
       alert(" 개인정보처리방침 동의시 회원가입이 가능합니다 . ");
    }else if($("#idcheck").html() !="사용가능"){
        alert("아이디가 불가능합니다 .");
    }else if($("#passwordcheck").html() !="사용가능"){
        alert("패스워드 불가능합니다 .");
    }else if($("#namecheck").html() !="사용가능"){
             alert("이름 불가능합니다 .");
    }else if($("#phonecheck").html() !="사용가능"){
             alert("연락처가 불가능합니다 .");
    }else if($("#emailcheck").html() !="사용가능"){
             alert("이메일 불가능합니다 .");
    }else if($("#addresscheck").html() !="사용가능"){
             alert("주소 불가능합니다 .");
         }else{
            $("form").submit(); //모든 유효성 검사 통과시전송
         }


    });
});
/*회원가입 유효성 검사끝*/
/*로그인*/
/*function login(){

       var mid = $("#login_mid").val();
        var mpassword = $("#login_mpassword").val();
        var memberdto = { "mid" : mid  , "mpassword" : mpassword  };
        $.ajax({
        url: "/member/logincontroller",             // 보내는곳
        data : JSON.stringify(memberdto)  ,   //  전송 데이터 값
           //  JSON.stringify( JSON 자료형 -> 문자열 )
        method: "post",        //  Get , Post 방식중 선택
        contentType: "application/json" ,  //  ajax 타입
        success: function(result){
       if(result==1){
       alert(result);
       }
          location.href="/"
        }else{
            $("#loginfailmsg").html("아이디 혹은 비밀번호가 다릅니다.");
        }
     }

   });
}*/
/*로그인 끝*/
// 아이디찾기
function findid(){
    var name = $("#findidname").val();
    var email = $("#findidemail").val();
    $.ajax({
        url : "/member/findid",
        data : {"name" : name , "email" : email},
        success : function (result){
            if(result.length>0){  // result의 길이가 0보다 크면
                alert("회원님의 아이디는[ "+result+" ]입니다.");
            }else{
                alert("존재하지않는 회원정보 입니다.");
            }
        }
    });
}
// 아이디 찾기 끝
// 비밀번호 찾기
function findpassword(){
    var id = $("#findpasswordid").val();
    var email = $("#findpasswordemail").val();
    $.ajax({
        url : "/member/findpassword",
        data : {"id" : id,"email": email},
        success : function (result) {
            if(result==1) {
                alert("입력하신 메일로 임시 비밀번호를 전송하였습니다.");
            }else{
                alert("오류 발생 관리자에게 문의 하세요.");
            }
        }
    });
}
var pwcount=0;
var infopassword = null;
// 비밀번호 변경하기
function passwordchange(){
    if(pwcount==0){
        alert("비밀번호 수정후 수정 버튼을 눌러주셔야 저장됩니다.");
        document.getElementById("tdpassword").innerHTML="<input type='password' id='infopassword' class='form-control' placeholder='영대소문자+숫자+특수문자[ !@#$%^&*()+|=]포함 8~15자 입력해주세요.'>";
        pwcount++;
    }else if(pwcount==1){
        infopassword = $("#infopassword").val();
        var mno = $("#infomno").val();
        $.ajax({
            url : "/member/passwordchange",
            data : {"mno" : mno,"password" : infopassword, "type": 1},
            success : function (result) {
                if(result==1){
                    alert("비밀번호가 변경되었습니다.");
                    location.reload();
                    pwcount=0;
                }
            }
        });
    }
}
// 핸드폰 번호 수정
var phcount=0;
var infophone = null;
function phonechange(){
    if(phcount==0){
        alert("핸드폰 번호 수정후 수정버튼을 눌러주셔야 저장됩니다.");
        document.getElementById("tdphone").innerHTML="<input type='text' id='infophone' class='form-control' placeholder='01x-xxxx-xxxx 형식으로 입력해주세요'>";
        phcount++;
    }else if(phcount==1){
        infophone= $("#infophone").val();
        var mno = $("#infomno").val();
        $.ajax({
            url : "/member/phonechange",
            data : {"mno" : mno , "phone" : infophone, "type" : 2},
            success : function (result) {
                if(result==1){
                    alert("핸드폰 번호가 변경되었습니다.");
                    location.reload();
                    phcount=0;
                }
            }
        });
    }
}
// 주소 수정
var adcount=0;
function addresschange(){
    if(adcount==0){
        alert("주소 수정후 수정버튼을 눌러주셔야 저장됩니다.");
        document.getElementById("tdaddress").style.display="";
        adcount++;
    }else if(adcount==1){
        var mno = $("#infomno").val();
        var address1 = document.getElementById("sample4_postcode").value;
        var address2 = document.getElementById("sample4_roadAddress").value;
        var address3= document.getElementById("sample4_jibunAddress").value;
        var address4= document.getElementById("sample4_detailAddress").value;
        $.ajax({
            url : "/member/addresschange",
            data : {"mno" : mno ,
                            "address1" : address1,
                            "address2" : address2,
                            "address3" : address3,
                            "address4" : address4,
                            "type" : 3
            },
            success : function (result) {
                if(result==1) {
                    alert("주소가 변경되었습니다.");
                    location.reload();
                    adcount=0;
                }
            }
        });
    }
}
//////////////////////회원 탈퇴
function mdelete(){
    var passwordconfirm = $("#passwordconfirm").val();
    $.ajax({
        url:"/member/delete",
        data:{"passwordconfirm" : passwordconfirm},
        success : function(data){
        if(data==1){
            alert("회원 탈퇴 성공")
            location.href="/member/logout"
        }else{
            $("#deleteresult").html("[회원탈퇴실패]비밀번호가 다릅니다")
        }

        }

    });

}



function replyadd1( ){


   const table = document.getElementById('spreadsheet1');
    const tbody = table.tBodies[0].rows.length;
      $.ajax({
        url: "/member/infoadd" ,
        data : {  "tbody" : tbody } ,
        success : function( data ){
        alert(data);
         /*  $("#spreadsheet>tbody").append(data);*/
        }
      });

}
