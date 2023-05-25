package com.gngsn.map.search.model.model;

import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.model.PlaceListZipper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

class PlaceListZipperTest {

    @Test
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
    void testcase2__FullSizeList() {
        final List<String> list1 = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
        final List<String> list2 = List.of("Z", "A", "D", "X", "F");

        final List<PlaceSearchResult.Place> result = new PlaceListZipper()
                .apply(list1.stream().map(PlaceSearchResult.Place::new).toList(),
                        list2.stream().map(PlaceSearchResult.Place::new).toList());

        System.out.println("result : " + result);

        Assertions.assertEquals(10, result.size());
        Assertions.assertLinesMatch(
                List.of("A", "D", "B", "C", "E", "F", "X", "Z", "G", "H"),
                result.stream().map(PlaceSearchResult.Place::getTitle).toList()
        );
    }

    @Test
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