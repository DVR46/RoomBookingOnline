package com.example.roombookingonline.convertor;

import com.example.roombookingonline.entity.RoomTypeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class SortMap {
    public static Map<RoomTypeEntity, Integer> sortRoomTypeMap(Map<RoomTypeEntity, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Comparator.comparingLong(e -> e.getKey().getId()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new));

    }
    public static Map<RoomTypeEntity, List<Integer>> sortBookingCart(Map<RoomTypeEntity, List<Integer>> map) {
        return map.entrySet().stream()
                .sorted(Comparator.comparingLong(e -> e.getKey().getId()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left,right) -> left,
                        LinkedHashMap::new));
    }
}
