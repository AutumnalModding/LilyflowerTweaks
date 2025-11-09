package xyz.lilyflower.lilytweaks.util;

import java.util.List;
import java.util.Map;

public class CollectionUtils {
    /**
     * Put And Return Key
     */
    public static <K, V> K PARK(K key, V value, Map<K, V> map) {
        map.put(key, value);
        return key;
    }

    /**
     * Put And Return Value
     */
    public static <K, V> V PARV(K key, V value, Map<K, V> map) {
        map.put(key, value);
        return value;
    }

    /**
     * Put And Return Map
     */
    public static <K, V> Map<K, V> PARM(K key, V value, Map<K, V> map) {
        map.put(key, value);
        return map;
    }

    /**
     * Add And Return List
     */
    public static <T> List<T> AARL(List<T> list, T object) {
        list.add(object);
        return list;
    }

    /**
     * Add And Return Object
     */
    public static <T> T AARO(List<T> list, T object) {
        list.add(object);
        return object;
    }
}
