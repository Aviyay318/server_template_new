package com.app.utils;



import com.app.entities.IslandsEntity;
import com.app.entities.LevelsEntity;
import com.app.entities.UserEntity;
import com.app.service.Persist;

import java.util.*;
import java.util.stream.Collectors;

public class IslandUtils {

    public static List<Map<String, Object>> getIslandsWithOpenStatus(Persist persist, String token) {
        List<Map<String, Object>> openIslands = new ArrayList<>();

        List<IslandsEntity> allIslands = persist.loadList(IslandsEntity.class);
        UserEntity user = persist.getUserByToken(token);
        List<LevelsEntity> islandOfUser = persist.getLevelsByUserId(user);

        Set<Integer> userIslandIds = islandOfUser.stream()
                .map(level -> level.getIsland().getId())
                .collect(Collectors.toSet());

        for (IslandsEntity island : allIslands) {
            Map<String, Object> islandData = new HashMap<>();
            islandData.put("id", island.getId());
            islandData.put("name", island.getName());
            islandData.put("isOpen", userIslandIds.contains(island.getId()));
            openIslands.add(islandData);
        }

        return openIslands;
    }
    public static List<IslandsEntity> getOpenIslandsForUser(Persist persist, UserEntity user) {
        List<LevelsEntity> islandOfUser = persist.getLevelsByUserId(user);
        return islandOfUser.stream()
                .map(LevelsEntity::getIsland)
                .distinct()
                .collect(Collectors.toList());
    }

}
