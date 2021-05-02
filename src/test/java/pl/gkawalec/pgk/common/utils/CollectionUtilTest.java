package pl.gkawalec.pgk.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilTest {

    private final List<String> emptyList = Collections.emptyList();
    private final List<?> nullList = null;
    private final List<Integer> listWithData = List.of(1);

    private final Set<Integer> emptySet = Collections.emptySet();
    private final Set<?> nullSet = null;
    private final Set<String> setWithData = Set.of("String");

    private final Collection<String> emptyCollection = Collections.emptyList();
    private final Collection<?> nullCollection = null;
    private final Collection<String> collectionWithData = List.of("a");

    private final Map<String, Integer> emptyMap = Collections.emptyMap();
    private final Map<?, ?> nullMap = null;
    private final Map<Integer, String> mapWithData = Map.of(1, "String");

    @Test
    @DisplayName("Check if the given collections are not empty")
    void isNotEmpty() {
        //when
        boolean emptyList = CollectionUtil.isNotEmpty(this.emptyList);
        boolean nullList = CollectionUtil.isNotEmpty(this.nullList);
        boolean listWithData = CollectionUtil.isNotEmpty(this.listWithData);

        boolean emptySet = CollectionUtil.isNotEmpty(this.emptySet);
        boolean nullSet = CollectionUtil.isNotEmpty(this.nullSet);
        boolean setWithData = CollectionUtil.isNotEmpty(this.setWithData);

        boolean emptyCollection = CollectionUtil.isNotEmpty(this.emptyCollection);
        boolean nullCollection = CollectionUtil.isNotEmpty(this.nullCollection);
        boolean collectionWithData = CollectionUtil.isNotEmpty(this.collectionWithData);

        boolean emptyMap = CollectionUtil.isNotEmpty(this.emptyMap);
        boolean nullMap = CollectionUtil.isNotEmpty(this.nullMap);
        boolean mapWithData = CollectionUtil.isNotEmpty(this.mapWithData);

        //then
        assertFalse(emptyList, "It's empty list");
        assertFalse(nullList, "It's null list");
        assertTrue(listWithData, "It's not empty list");

        assertFalse(emptySet, "It's empty set");
        assertFalse(nullSet, "It's null set");
        assertTrue(setWithData, "It's not empty set");

        assertFalse(emptyCollection, "It's empty collection");
        assertFalse(nullCollection, "It's null collection");
        assertTrue(collectionWithData, "It's not empty collection");

        assertFalse(emptyMap, "It's empty map");
        assertFalse(nullMap, "It's null map");
        assertTrue(mapWithData, "It's not empty map");
    }

    @Test
    @DisplayName("Check if the given collections are empty")
    void isEmpty() {
        //when
        boolean emptyList = CollectionUtil.isEmpty(this.emptyList);
        boolean nullList = CollectionUtil.isEmpty(this.nullList);
        boolean listWithData = CollectionUtil.isEmpty(this.listWithData);

        boolean emptySet = CollectionUtil.isEmpty(this.emptySet);
        boolean nullSet = CollectionUtil.isEmpty(this.nullSet);
        boolean setWithData = CollectionUtil.isEmpty(this.setWithData);

        boolean emptyCollection = CollectionUtil.isEmpty(this.emptyCollection);
        boolean nullCollection = CollectionUtil.isEmpty(this.nullCollection);
        boolean collectionWithData = CollectionUtil.isEmpty(this.collectionWithData);

        boolean emptyMap = CollectionUtil.isEmpty(this.emptyMap);
        boolean nullMap = CollectionUtil.isEmpty(this.nullMap);
        boolean mapWithData = CollectionUtil.isEmpty(this.mapWithData);

        //then
        assertTrue(emptyList, "It's empty list");
        assertTrue(nullList, "It's null list");
        assertFalse(listWithData, "It's not empty list");

        assertTrue(emptySet, "It's empty set");
        assertTrue(nullSet, "It's null set");
        assertFalse(setWithData, "It's not empty set");

        assertTrue(emptyCollection, "It's empty collection");
        assertTrue(nullCollection, "It's null collection");
        assertFalse(collectionWithData, "It's not empty collection");

        assertTrue(emptyMap, "It's empty map");
        assertTrue(nullMap, "It's null map");
        assertFalse(mapWithData, "It's not empty map");
    }

}
