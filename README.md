# movie
## contents
[1. 개요](#1-개요)  
[2. 개발 환경](#2-개발-환경)  
[3. 개발 일정](#3-개발-일정)  
[4. 주요 기능](#4-주요-기능)  
[5. 패키지 구조도](#5-패키지-구조도)  


## 1. 개요
### 1.1. 선정이유
지하철을 자주 이용하는 사람들을 위해 만들었습니다.

## 2. 개발 환경
- 운영체제 : Windows10
- Front-end : html, CSS, Bootstrap
- Back-end : JDK 11, Eclipse EE 2021-09
- Database : MySQL
- Server : Apache Tomcat 9.0
- Version Control : Git
- API : [서울시 지하철 실시간 도착정보](https://data.seoul.go.kr/dataList/OA-12764/F/1/datasetView.do)
- 화면 크기 : .container-sm( 540px )

## 3. 개발 일정
- 기간 : 2022.01.19 ~ 2022.01.22  
- History

|날짜|내용|
|----|----|
|2022.01.19|주제 및 API 선정, 테스트|
|2022.01.20|Controller 초안 작성|
|2022.01.21 ~ 2022.01.22|기능 구현 및 디버깅|
|2022.01.23|프론트 제작|

## 4. 주요 기능

![image](https://user-images.githubusercontent.com/88884623/150366714-103f6fe5-737a-4cca-840a-be748b792324.png)

### 1. 역 검색
     - 자동완성 기능 가능할까??
     - select로 검색하기
### 2. 이전역 - 현재역 - 다음역
     - 이전역이나 다음역 클릭시 해당 역으로 이동(페이지 이동)
### 3. 운행 정보 표시
     - 상행선, 하행선 각각 도착 예정 시간, 급행
     - 도착 코드 이용해서 전역, 전역 출발, 진입 등 표시
### 4. 새로 고침 기능
     - 오전 12:05 기준 (새로고침 버튼)

## 5. 패키지 구조도
     

  ![image](https://user-images.githubusercontent.com/88884623/150363083-24dc64e3-2d05-465c-9ff1-de5c42133a48.png)



