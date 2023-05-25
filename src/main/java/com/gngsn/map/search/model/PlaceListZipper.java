package com.gngsn.map.search.model;

import com.gngsn.map.search.dto.PlaceSearchResult;

import java.util.*;
import java.util.function.BiFunction;

import static com.gngsn.map.search.Constants.DEFAULT_QUERY_SIZE;
import static com.gngsn.map.search.Constants.TOTAL_QUERY_SIZE;


/**
 * 다중 서비스에서 받은 서로 다른 API 응답 값 Place 객체 리스트로 결합
 */
public class PlaceListZipper implements BiFunction<List<PlaceSearchResult.Place>, List<PlaceSearchResult.Place>, List<PlaceSearchResult.Place>> {

    /**
     * @param source larger list of Place instance
     * @param target smaller list of Place instance
     */
    @Override
    public List<PlaceSearchResult.Place> apply(final List<PlaceSearchResult.Place> source, final List<PlaceSearchResult.Place> target) {

        final LinkedHashSet<PlaceSearchResult.Place> result = new LinkedHashSet<>(TOTAL_QUERY_SIZE);
        final TreeSet<PlaceSearchResult.Place> sortedSource = new TreeSet<>(source.subList(0, Math.min(DEFAULT_QUERY_SIZE, source.size())));
        final List<PlaceSearchResult.Place> sortedTarget = new ArrayList<>(target.stream().sorted().toList());

        final Iterator<PlaceSearchResult.Place> sourceIterator = sortedSource.iterator();

        final int nextSeekIdx = 0;
        while (sourceIterator.hasNext()) {
            final PlaceSearchResult.Place s = sourceIterator.next();
            boolean duplicated = false;

            for (int t = nextSeekIdx; t < sortedTarget.size(); t++) {
                if (s.isSame(sortedTarget.get(t))) {
                    duplicated = true;
                    result.add(s);
                    sortedTarget.remove(t);
                    break;
                }
            }

            if (duplicated) {
                sourceIterator.remove();
            }
        }

        result.addAll(sortedSource);
        result.addAll(sortedTarget);

        if (source.size() >= DEFAULT_QUERY_SIZE) {
            result.addAll(
                    source.subList(DEFAULT_QUERY_SIZE, source.size())
                            .stream()
                            .filter(s -> !result.contains(s))
                            .toList());
        }

        return result.stream().limit(TOTAL_QUERY_SIZE).toList();
    }
}
