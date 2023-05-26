## 1. 장소 검색 서비스

_Place Search Service, 여러 외부 장소 검색 API 호출 결과를 결합하여 장소 검색_

<details>
<summary><b>Specification</b></summary>

**장소 검색 서비스 - 카카오 검색 API, 네이버 검색 API**

- 각각 최대 5개씩, 총 10개의 키워드 관련 장소를 검색
- 특정 서비스 검색 결과가 5개 이하면 최대한 총 10개에 맞게 적용
- 카카오 장소 검색 API의 결과를 기준으로 두 API 검색 결과에 동일하게 나타나는 문서(장소)가 상위에 올 수 있도록 정렬)

<br/>

**검색 소스: 아래 API 활용**

- 카카오의 로컬API
    - https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword
- 네이버 장소 검색 API
    - https://developers.naver.com/docs/serviceapi/search/local/local.md#%EC%A7%80%EC%97%AD

</details>

| HTTP Verbs | Endpoints                             | Action            |
|------------|---------------------------------------|-------------------|
| GET        | `/v1/map/search/place/keyword?query=` | 키워드 검색으로 장소 목록 조회 |

<br/>

### Request cURL Sample

```cURL
$ curl -G "http://127.0.0.1:8811/v1/map/search/place/keyword" --data-urlencode "query=한글" -v

*   Trying 127.0.0.1:8811...
* Connected to 127.0.0.1 (127.0.0.1) port 8811 (#0)
> GET /v1/map/search/place/keyword?query=%ED%95%9C%EA%B8%80 HTTP/1.1
> Host: 127.0.0.1:8811
> User-Agent: curl/7.79.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Thu, 25 May 2023 17:36:17 GMT
< 
{
  "places" : [ {
    "title" : "갈미한글공원"
  }, {
    "title" : "국립한글박물관"
  }, {
    "title" : "여주한글시장"
  }, {
    "title" : "한글"
  }, {
    "title" : "한글시장 주차장"
  }, {
    "title" : "광화문광장한글분수"
  }, {
    "title" : "세종한글서예큰뜻모임"
  }, {
    "title" : "한글가온길"
  }, {
    "title" : "한글학회"
  }, {
    "title" : "한글회관"
  } ]
```

<br/>

## 2. 검색 키워드 목록

_Search Keyword Rank List_

<details>
<summary><b>Specification</b></summary>

- 사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드 목록을 제공
- 키워드 별로 검색된 횟수도 함께 표기
- 비즈니스 로직은 모두 서버에서 구현
- 요청/응답 URL, JSON Object는 요건에 맞게 자유롭게 작성

</details>

| HTTP Verbs | Endpoints                     | Action           |
|------------|-------------------------------|------------------|
| GET        | `/v1/map/rank/search/keyword` | 조회수 높은 검색 키워드 목록 |



