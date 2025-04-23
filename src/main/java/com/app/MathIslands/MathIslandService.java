package com.app.MathIslands;

import com.app.entities.IslandsEntity;
import com.app.entities.LevelsEntity;
import com.app.entities.UserEntity;

import java.util.Map;

public interface MathIslandService {
    Map<String, Object> generateExercise(UserEntity user, IslandsEntity island, LevelsEntity level, int questionType);
}

