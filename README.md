# movie
## contents
[1. 개요](#1-개요)  
[2. 개발 환경](#2-개발-환경)  
[3. 개발 일정](#3-개발-일정)  
[4. 주요 기능](#4-주요-기능)  
[5. 패키지 구조도](#5-DB명-컬럼명)  


## 1. 개요
### 1.1. 선정이유
영화예매를 쉽게 할 수 있도록 영화에 대한 정보를 제공하여 사용자에게 영화예매 시 편리함을 제공하고 관리자에게는 영화 등록, 상영일정 등록과 같은 영화 관리 시에 편의를 제공합니다.

## 2. 개발 환경
- 운영체제 : Windows10
- Front-end : html, CSS, Bootstrap
- Back-end : JDK 11, IntelliJ IDEA Ultimate Edition 2021.3.1
- Database : MySQL
- Server : Apache Tomcat 9.0
- Version Control : Git
- API : [영화진흥위원회 오픈API](https://www.kobis.or.kr/kobisopenapi/homepg/apiservice/searchServiceInfo.do#;)
- 화면 크기 :

## 3. 개발 일정
- 기간 : 2022.01.17 ~ 2022.02.21  
- History

|날짜|내용|
|----|----|
|2022.01.17~2022.01.20|주제 및 API 선정|
|2022.01.21|Controller 초안 작성, db 설계|
|2022.01.24|패키지 구성및 db 구현|
|2022.01.25|페이지별 레이아웃|
|2022.01.26|페이지별 레이아웃|
|2022.01.27|페이지별 레이아웃|

## 4. 주요 기능

### 1. 관리자
     - 영화 관 구분(일반관, 특별관)
     - 영화관 좌석 배치 
     - 상영 영화 등록 및 삭제 수정
### 2. 사용자
     - 영화 예매(날짜,시간,관,영화) ,상세확인, 수정, 삭제
     - 영화 리뷰 및 평점 비교분석(차트)

## 5. DB명/컬럼명

Member

| 회원번호 오토키 OntoMany | mno |
| --- | --- |
| 아이디(String) | mid |
| 비밀번호(String) | mpassword |
| 이름(String) | mname |
| 이메일(String) | memail |
| 폰번호(String) | mphone |
| 주소(String) | maddress |
| 성별(String) | msex |
| 나이(String) | mage |

cnema

| 관번호 오토키 (OnetoMany) | cno |
| --- | --- |
| 좌석활성화여부(String) | cactive |
| 시네마타입(String) | ctype |
| 시네마가격(String) | cprice |
| 시간 (OnetoMany) | tno |

movie

| 영화번호 오토키 (OnetoMany) | mno |
| --- | --- |
| 영화아이디(API) | mid |
| 영화이미지(String) | mimg |
| 리플번호 (ManytoOne) | rno |

date

| 시간번호 오토키 (OnetoMany) | dno |
| --- | --- |
| 날짜(String) | ddate |
| 시간(String)  | dtime |
| 영화번호(ManytoOne) | mno |
| 관번호(ManytoOne) | cno |
| 예매번호(OnetoMany) | tno |

ticketing

| 예매번호 오토키 (OnetoMany) | tno |
| --- | --- |
| 예약좌석(String) | tseat |
| 시간(ManytoOne) | dno |
| 맴버번호(ManytoOne) | mno |

reply

| 리플번호 (OnetoMany) | rno |
| --- | --- |
| 리플내용(String) | rcontents |
| 평점(int) 0~5  | rgrade |
| 맴버번호(ManytoOne) | mno |
## 6. 컨트롤러 구조도
![영화관프로젝트](https://user-images.githubusercontent.com/91528966/150746516-3a9f3987-b746-484f-a003-1332d6bcb860.jpg)


