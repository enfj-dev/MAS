# Map Search Service (MAS)

🔗 <a target="_blank" href="http://mas.kyeongsun.com"> &nbsp; mas.kyeongsun.com </a>

**Objective**

- 지속적 유지 보수 및 확장에 용이한 아키텍처에 대한 설계
   - Scalability & Encapsulation: 높은 응집도와 낮은 결합도가 유지되도록 책임 할당
- API 제공자의 “다양한” 장애 및 연동 오류 발생 상황에 대한 고려
   - 새로운 검색 API 제공자의 추가 시 변경 영역 최소화 고려
   - 외부 HTTP 요청 시 Error Handling
- 테스트 코드를 통한 프로그램 검증 및 테스트 용이성(Testability)을 높이기 위한 코드 설계
- 성능을 위한 Caffeine Cache 설정
- 유연한 AWS 백엔드 아키텍처 설계
   - 사용자 편의성을 위한 Domain 설정 및 Fault Tolerant
   - AWS ECS, ALB, AutoScaling Group 설정
   - AWS CloudWatch - CPU, Memory Utilization Monitoring

<br/> 
<small>Table of Contents</small>

0. [Introduction](#1-introduction)
   1. [Backend Architecture](#11-backend-architecture)
   2. [Skill Set](#12-skill-set)
1. [장소 검색 서비스](#2-장소-검색-서비스)
   1. [Specification](#21-specification)
   2. [Diagram](#22-diagram)
   3. [Request](#23-request)
   4. [Response](#24-response)
2. [검색 키워드 목록](#3-검색-키워드-목록)
   1. [Specification](#31-specification)
   2. [Diagram](#32-diagram)
   3. [Request](#33-request)
   4. [Response](#34-response)
3. [How to Get Started](#4-how-to-get-started)
   1. [Docker](#41-docker)
   2. [Gradle](#42-gradle)

<br/>


## 1. Introduction
### 1.1. Backend Architecture

![BackendArchitecture.png](diagram%2FBackendArchitecture.png)

🔗 <a target="_blank" href="https://hub.docker.com/repository/docker/gngsn/mas/general"> &nbsp; Docker hub Repository </a>

<br/>

### 1.2. Skill Set

- JDK 17
- Spring Boot 3, Spring Webflux, Caffeine Cache, MySQL, JPA
- Gradle 8, Junit 5, Docker / Docker hub
- AWS ECS · Fargate, AWS Application Load Balancer, AWS RDS

<br/>

## 2. 장소 검색 서비스

_Place Search Service, 여러 외부 장소 검색 API 호출 결과를 결합하여 장소 검색_


🔗 <a target="_blank" href="http://mas.kyeongsun.com/v1/map/search/place/keyword?query=카카오">http://mas.kyeongsun.com/v1/map/search/place/keyword?query=카카오</a>

<br/>

### 2.1. Specification

**장소 검색 서비스 - 카카오 검색 API, 네이버 검색 API**

- 각각 최대 5개씩, 총 10개의 키워드 관련 장소를 검색
- 특정 서비스 검색 결과가 5개 이하면 최대한 총 10개에 맞게 적용
- 카카오 장소 검색 API의 결과를 기준으로 두 API 검색 결과에 동일하게 나타나는 문서(장소)가 상위에 올 수 있도록 정렬)

<br/>
<details>
<summary><b>API 검색 소스 명세 문서</b></summary>

- 카카오의 로컬API
    - https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword
- 네이버 장소 검색 API
    - https://developers.naver.com/docs/serviceapi/search/local/local.md#%EC%A7%80%EC%97%AD
  
<br/>
</details>

<br/>

### 2.2. Diagram

![KeywordPlaceSearchClassUml_v1.png](diagram%2FKeywordPlaceSearchClassUml_v1.png)

<br/>

### 2.3. Request

| HTTP Verbs | Endpoints                             | Action            |
|------------|---------------------------------------|-------------------|
| GET        | `/v1/map/search/place/keyword?query=` | 키워드 검색으로 장소 목록 조회 |


<br/>

#### Request cURL Sample

```bash
curl -G "http://mas.kyeongsun.com/v1/map/search/place/keyword" --data-urlencode "query=카카오"
```

<br/>

### 2.4. Response

#### EX. Result - Success
```json
{
  "places" : [ {
        "title" : "카카오판교아지트"
      }, {
        "title" : "카카오"
      }, {
        "title" : "카카오 스페이스닷원"
      }, {
        "title" : "카카오 스페이스닷투"
      }, {
        "title" : "카카오프렌즈 판교아지트점"
      }, {
        "title" : "카카오 멜론"
      }, {
        "title" : "카카오고객센터"
      }, {
        "title" : "카카오톡 대화내용 데이터복구센터"
      }, {
        "title" : "카카오톡대화내용복구"
      }, {
        "title" : "카카오뱅크"
      }
]
```

#### EX. Result - Bad Request
```bash
{"message":"[query] 크기가 1에서 10 사이여야 합니다."}
```

#### EX. Result - Bad Request
```bash
{"message":"400 BAD_REQUEST \"Required query parameter 'query' is not present.\""}
```


<br/><br/>

## 3. 검색 키워드 목록

_Search Keyword Rank List_

🔗 <a target="_blank" href="http://mas.kyeongsun.com/v1/map/rank/search/keyword">http://mas.kyeongsun.com/v1/map/rank/search/keyword </a>

<br/>

### 3.1. Specification

<details>
<summary><b>Specification</b></summary>

- 사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드 목록을 제공
- 키워드 별로 검색된 횟수도 함께 표기
- 비즈니스 로직은 모두 서버에서 구현
- 요청/응답 URL, JSON Object는 요건에 맞게 자유롭게 작성

<br/>
</details>
<br/>

### 3.2. Diagram

![SearchKeywordRankClassUml_v1.png](diagram%2FSearchKeywordRankClassUml_v1.png)


<br/>

### 3.3. Request

| HTTP Verbs | Endpoints                     | Action           |
|------------|-------------------------------|------------------|
| GET        | `/v1/map/rank/search/keyword` | 조회수 높은 검색 키워드 목록 |


<br/>

#### Request cURL Sample

```bash
curl http://mas.kyeongsun.com/v1/map/rank/search/keyword
```

<br/>

### 3.4. Response

#### EX. Result - Success

```json
{
  "ranking" : [ {
    "keyword" : "본죽&비빔밥",
    "count" : 994
  }, {
    "keyword" : "이비가짬뽕",
    "count" : 984
  }, {
    "keyword" : "노브랜드 버거(No Brand Burger)",
    "count" : 952
  }, {
    "keyword" : "세광양대창",
    "count" : 928
  }, {
    "keyword" : "설동궁찜닭",
    "count" : 908
  }, {
    "keyword" : "네네치킨",
    "count" : 826
  }, {
    "keyword" : "맛찬들왕소금구이",
    "count" : 823
  }, {
    "keyword" : "청년다방",
    "count" : 820
  }, {
    "keyword" : "미소야",
    "count" : 817
  }, {
    "keyword" : "오복오봉집",
    "count" : 806
  } ]
```

<br/>


## 4. How to Get Started

### 4.1. Docker

#### Step 1: 

Pull the docker image from docker hub.

```text
docker pull gngsn/mas:latest
```

#### Step 2:

Run docker container.

```text
docker run -d -p 8811:8811 gngsn/mas:latest
```


#### Step 3:

Request using curl.

```text
$ curl -G "http://localhost:8811/v1/map/search/place/keyword" --data-urlencode "query=카카오"
$ curl http://localhost:8811/v1/map/rank/search/keyword
```

<br/>

### 4.2. Gradle

#### Step 1:

Clone the MAS repository and navigate to the project directory

```bash
git clone https://github.com/gngsn/MAS.git
cd ./MAS
```

#### Step 2:

Add a `secret.properties` file.

```bash
vi src/main/resources/secret.properties
```

#### Step 3:

Run the application.

**GNU/Linux:**

```bash
$ ./gradlew bootRun
```

**Windows:**

```powershell
C:\Users\YourUserName\MAS> gradlew.bat bootRun
```

#### Step 4:

Request using curl.

```text
$ curl -G "http://localhost:8811/v1/map/search/place/keyword" --data-urlencode "query=카카오"
$ curl http://localhost:8811/v1/map/rank/search/keyword
```

