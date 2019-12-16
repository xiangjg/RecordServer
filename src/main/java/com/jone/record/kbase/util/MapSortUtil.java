package com.jone.record.kbase.util;

import java.util.*;
import java.util.stream.Collectors;

public class MapSortUtil {

    public MapSortUtil() {
        super();
    }

    private static Comparator<Map.Entry> comparatorByKeyAsc = (Map.Entry map1, Map.Entry map2) -> {
        if (map1.getKey() instanceof Comparable) {
            return ((Comparable) map1.getKey()).compareTo(map2.getKey());
        }
        throw new UnsupportedOperationException("键的类型尚未实现Comparable接口");
    };


    private static Comparator<Map.Entry> comparatorByKeyDesc = (Map.Entry map1, Map.Entry map2) -> {
        if (map1.getKey() instanceof Comparable) {
            return ((Comparable) map2.getKey()).compareTo(map1.getKey());
        }
        throw new UnsupportedOperationException("键的类型尚未实现Comparable接口");
    };


    private static Comparator<Map.Entry> comparatorByValueAsc = (Map.Entry map1, Map.Entry map2) -> {
        if (map1.getValue() instanceof Comparable) {
            return ((Comparable) map1.getValue()).compareTo(map2.getValue());
        }
        throw new UnsupportedOperationException("值的类型尚未实现Comparable接口");
    };


    private static Comparator<Map.Entry> comparatorByValueDesc = (Map.Entry map1, Map.Entry map2) -> {
        if (map1.getValue() instanceof Comparable) {
            return ((Comparable) map2.getValue()).compareTo(map1.getValue());
        }
        throw new UnsupportedOperationException("值的类型尚未实现Comparable接口");
    };

    /**
     * 按键升序排列
     */
    public static <K, V> Map<K, V> sortByKeyAsc(Map<K, V> originMap) {
        if (originMap == null) {
            return null;
        }
        return sort(originMap, comparatorByKeyAsc);
    }

    /**
     * 按键降序排列
     */
    public static <K, V> Map<K, V> sortByKeyDesc(Map<K, V> originMap) {
        if (originMap == null) {
            return null;
        }
        return sort(originMap, comparatorByKeyDesc);
    }


    /**
     * 按值升序排列
     */
    public static <K, V> Map<K, V> sortByValueAsc(Map<K, V> originMap) {
        if (originMap == null) {
            return null;
        }
        return sort(originMap, comparatorByValueAsc);
    }

    /**
     * 按值降序排列
     */
    public static <K, V> Map<K, V> sortByValueDesc(Map<K, V> originMap) {
        if (originMap == null) {
            return null;
        }
        return sort(originMap, comparatorByValueDesc);
    }

    private static <K, V> Map<K, V> sort(Map<K, V> originMap, Comparator<Map.Entry> comparator) {
        return originMap.entrySet()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }


    public static Map<String, String> sortByValue(Map<String, String> hm) {
        // HashMap的entry放到List中
        List<Map.Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(hm.entrySet());

        //  对List按entry的value排序
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // 将排序后的元素放到LinkedHashMap中
        HashMap<String, String> temp = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
