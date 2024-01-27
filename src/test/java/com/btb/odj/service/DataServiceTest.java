package com.btb.odj.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class DataServiceTest {

    @Test
    void split() {
        int size = 31;
        int blocks = 4;
        final List<Integer> range = IntStream.range(0, size).boxed().toList();
        Collection<List<Integer>> split = DataService.split(range, blocks);

        split.forEach(e -> assertTrue(e.size() <= blocks));

        Set<Integer> input = Set.copyOf(range);
        Set<Integer> output =
                Set.copyOf(split.stream().flatMap(Collection::stream).toList());
        assertEquals(input, output);
    }
}
