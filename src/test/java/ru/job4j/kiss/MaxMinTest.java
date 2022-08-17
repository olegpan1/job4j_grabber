package ru.job4j.kiss;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class MaxMinTest {

    @Test
    public void whenMax() {
        MaxMin maxMin = new MaxMin();
        Comparator<Integer> comparator = (o1, o2) -> Integer.compare(o1.compareTo(o2), 0);
        List<Integer> list = List.of(5, 8, 9, 10, 75, 12, -4);
        assertThat(maxMin.max(list, comparator), is(75));
    }

    @Test
    public void whenMin() {
        MaxMin maxMin = new MaxMin();
        Comparator<Integer> comparator = (o1, o2) -> Integer.compare(o1.compareTo(o2), 0);
        List<Integer> list = List.of(5, 8, 9, 10, 75, 12, -4);
        assertThat(maxMin.min(list, comparator), is(-4));
    }

    @Test
    public void whenEmptyList() {
        MaxMin maxMin = new MaxMin();
        Comparator<Integer> comparator = (o1, o2) -> Integer.compare(o1.compareTo(o2), 0);
        List<Integer> list = List.of();
        assertNull(maxMin.min(list, comparator));
        assertNull(maxMin.max(list, comparator));
    }

    @Test
    public void whenOneElement() {
        MaxMin maxMin = new MaxMin();
        Comparator<Integer> comparator = (o1, o2) -> Integer.compare(o1.compareTo(o2), 0);
        List<Integer> list = List.of(-4);
        assertThat(maxMin.min(list, comparator), is(-4));
        assertThat(maxMin.max(list, comparator), is(-4));
    }
}