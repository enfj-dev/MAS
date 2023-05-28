package com.gngsn.map.search.model;

import com.gngsn.map.search.dto.PlaceSearchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

@DisplayName("여러 검색 API의 Place 리스트 응답 결과 결합 테스트")
class PlaceListZipperTest {

    @Test
    @DisplayName("Kakao와 Naver '농협은행' 검색 결과 리스트의 중복 데이터 4 건은 상위 정렬된 4건")
    void testcase1__RealSet() {
        final List<PlaceSearchResult.Place> result = new PlaceListZipper().apply(kakaoSet, naverSet);
        System.out.println(result);

        Assertions.assertEquals(10, result.size());
        Assertions.assertLinesMatch(
                List.of("NH농협은행 수지지점", "NH농협은행 신갈지점", "NH농협은행 용인동백역지점", "NH농협은행 용인시지부"),
                result.subList(0, 4).stream().map(PlaceSearchResult.Place::getTitle).sorted().toList()
        );
    }

    @Test
    @DisplayName("Kakao의 5건 이외의 10건을 채우기 위해 여분 데이터를 추가할 때에도 Naver 응답 결과와 중복되지 않음")
    void testcase2__FullSizeList() {
        final List<String> list1 = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
        final List<String> list2 = List.of("Z", "A", "D", "X", "F");

        /**
         * - A, D 결과가 중복 -> 상위 2 건
         * - (F,) G, H -> 10건의 검색 결과를 채우기 위해, 여분 검색 결과로 가져온 Kakao 응답 리스트를 채움
         * - F는 Naver 응답 값과 중복되기 때문에 이를 제외한 G, H를 채움
         */
        final List<PlaceSearchResult.Place> result = new PlaceListZipper()
                .apply(list1.stream().map(PlaceSearchResult.Place::new).toList(),
                        list2.stream().map(PlaceSearchResult.Place::new).toList());

        Assertions.assertEquals(10, result.size());
        Assertions.assertLinesMatch(
                List.of("A", "D", "B", "C", "E", "F", "X", "Z", "G", "H"),
                result.stream().map(PlaceSearchResult.Place::getTitle).toList()
        );
    }

    @Test
    @DisplayName("모든 검색 결과가 10건이 넘지 않을 수 있음")
    void testcase3__List_LessThan_DEFAULT_SIZE() {
        final List<PlaceSearchResult.Place> list1 = Stream.of("A", "B", "C", "D").map(PlaceSearchResult.Place::new).toList();
        final List<PlaceSearchResult.Place> list2 = Stream.of("Z", "C").map(PlaceSearchResult.Place::new).toList();

        final List<PlaceSearchResult.Place> result = new PlaceListZipper().apply(list1, list2);

        Assertions.assertLinesMatch(
                List.of("C", "A", "B", "D", "Z"),
                result.stream().map(PlaceSearchResult.Place::getTitle).toList()
        );
    }

    @Test
    @DisplayName("데이터 셋의 기준인 Kakao 검색 결과가 적을 수 있음")
    void testcase4__Source_LessThan_Target() {
        final List<PlaceSearchResult.Place> list1 = Stream.of("Z", "C").map(PlaceSearchResult.Place::new).toList();
        final List<PlaceSearchResult.Place> list2 = Stream.of("A", "B", "C", "D").map(PlaceSearchResult.Place::new).toList();

        final List<PlaceSearchResult.Place> result = new PlaceListZipper().apply(list1, list2);

        Assertions.assertLinesMatch(
                List.of("C", "Z", "A", "B", "D"),
                result.stream().map(PlaceSearchResult.Place::getTitle).toList()
        );
    }

    @Test
    @DisplayName("데이터 셋의 기준인 Kakao 검색 결과가 없을 수 있음")
    void testcase5__source_isEmpty() {
        final List<PlaceSearchResult.Place> list1 = List.of();
        final List<PlaceSearchResult.Place> list2 = Stream.of("A", "B", "C", "D").map(PlaceSearchResult.Place::new).toList();

        final List<PlaceSearchResult.Place> result = new PlaceListZipper().apply(list1, list2);

        Assertions.assertLinesMatch(
                List.of("A", "B", "C", "D"),
                result.stream().map(PlaceSearchResult.Place::getTitle).toList()
        );
    }

    @Test
    @DisplayName("데이터 셋이 모두 없을 수 있음")
    void testcase6__AllList_isEmpty() {
        final List<PlaceSearchResult.Place> list1 = List.of();
        final List<PlaceSearchResult.Place> list2 = List.of();

        final List<PlaceSearchResult.Place> result = new PlaceListZipper().apply(list1, list2);

        Assertions.assertLinesMatch(
                List.of(),
                result.stream().map(PlaceSearchResult.Place::getTitle).toList()
        );
    }

    // ======================== Test set ============================
    private final List<PlaceSearchResult.Place> kakaoSet = List.of(
            new PlaceSearchResult.Place("NH농협은행 신갈지점"),
            new PlaceSearchResult.Place("NH농협은행 용인시지부"),
            new PlaceSearchResult.Place("NH농협은행 용인동백역지점"),
            new PlaceSearchResult.Place("NH농협은행 수지만현지점"),
            new PlaceSearchResult.Place("NH농협은행 수지지점"),

            new PlaceSearchResult.Place("NH농협은행 죽전보정지점"),
            new PlaceSearchResult.Place("NH농협은행 수지구청출장소"),
            new PlaceSearchResult.Place("NH농협은행 기흥구청출장소"),
            new PlaceSearchResult.Place("NH농협은행 용인시청출장소"),
            new PlaceSearchResult.Place("NH농협은행 처인구청출장소"));

    private final List<PlaceSearchResult.Place> naverSet = List.of(
            new PlaceSearchResult.Place("NH농협은행 용인동백역지점"),
            new PlaceSearchResult.Place("NH농협은행 수지지점"),
            new PlaceSearchResult.Place("NH농협은행 신갈지점"),
            new PlaceSearchResult.Place("NH농협은행365"),
            new PlaceSearchResult.Place("NH농협은행 용인시지부"));
}