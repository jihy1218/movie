# movie
## contents
[1. 개요](#1-개요)   <br>
[2. 개발 환경](#2-개발-환경) <br>
[3. 역할](#3-역할)  <br>
[4. 개발 일정](#4-개발-일정)   <br>
[5. 주요 기능](#5-주요-기능)   <br>
[6. 패키지 구조도](#6-DB명-컬럼명)   <br>
[7. 컨트롤러 구조도](#7-컨트롤러-구조도) 

## 1. 개요

[![캡처](https://user-images.githubusercontent.com/91528966/155261167-90b81d94-0c09-40e1-86cd-5d621cdb0ab4.png)](https://www.youtube.com/watch?v=9qKqo242rbg)

### 1.1. 선정이유
영화예매를 쉽게 할 수 있도록 영화에 대한 정보를 제공하여 사용자에게 영화예매 시 편리함을 제공하고 관리자에게는 영화 등록, 상영일정 등록과 같은 영화 관리 시에 편의를 제공합니다.

## 2. 개발 환경
- 운영체제 : Windows10
- Front-end : html, CSS, Bootstrap
- Back-end : JDK 1.8.0 , IntelliJ IDEA Ultimate Edition 2021.3.1
- Database : MySQL
- Version Control : Git
- API : [영화진흥위원회 오픈API](https://www.kobis.or.kr/kobisopenapi/homepg/apiservice/searchServiceInfo.do#;)

## 3. 역할
-민욱 : admin 구현
-지형 : 예매, 매출, 영화상세페이지 구현
-동진 : member 구현

## 4. 개발 일정
- 기간 : 2022.01.17 ~ 2022.02.21  
- History

|날짜|내용|
|----|----|
|2022.01.17~2022.01.20|주제 및 API 선정|
|2022.01.21|Controller 초안 작성, db 설계|
|2022.01.24|패키지 구성및 db 구현|
|2022.01.25~2022.01.27|페이지별 레이아웃|
|2022.01.28|컨트롤러설계|
|2022.01.29|문서화(화면구조, controller 구조, DB구조)|
|2021.01.30~2021.02.03|프론트 구현|
|2021.02.04~2021.02.05|프론트구현(페이지전환완료)|
|2021.02.06|프론트구현 테스트|
|2021.02.07|Dto,Entity작성|
|2021.02.08|Controller,Service작성|
|2021.02.09~2021.02.20|기능 구현|
|2021.02.21|프로젝트 테스트|
|2021.02.22|프로젝트 병합 및 디버깅|
|2021.02.23|프로젝트발표|

## 5. 주요 기능

### 1. 관리자
     - 영화 관 구분(일반관, 특별관)
     - 영화관 좌석 배치 
     - 상영 영화 등록 및 삭제 수정
### 2. 사용자
     - 영화 예매(날짜,시간,관,영화) ,상세확인, 수정, 삭제
     - 영화 리뷰 및 평점 비교분석(차트)

## 6. DB명/컬럼명

![image](https://user-images.githubusercontent.com/91528966/155260907-34a64c99-689b-4912-9048-b2bfdcd1cf8d.png)

## 7. 컨트롤러 구조도
![영화관프로젝트](https://user-images.githubusercontent.com/91528966/150746516-3a9f3987-b746-484f-a003-1332d6bcb860.jpg)


