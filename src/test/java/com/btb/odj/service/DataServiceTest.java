package com.btb.odj.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataServiceTest {

    @Test
    void split() {
        int size = 31;
        int blocks = 4;
        IntStream range = IntStream.range(0, size);
        List<List<Integer>> split = DataService.split(range.boxed().toList(), blocks);
        assertEquals(Math.ceil(size * 1f / blocks), split.size());
        assertEquals(List.of(0, 1, 2, 3), split.get(0));
        assertEquals(List.of(4, 5, 6, 7), split.get(1));
        assertEquals(List.of(8, 9, 10, 11), split.get(2));
    }
}