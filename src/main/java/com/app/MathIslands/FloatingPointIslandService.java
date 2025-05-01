package com.app.MathIslands;

import com.app.Math.BaseMath;
import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.IslandsEntity;
import com.app.entities.LevelsEntity;
import com.app.entities.QuestionTypeEntity;
import com.app.entities.UserEntity;
import com.app.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class FloatingPointIslandService extends BaseIslandService {

    @Override
    protected BaseMath createService(int level, double success, int questionType) {
        BaseMath service = new BaseMath() {
            @Override
            public Map<String, Object> getExercise() {
                double num1 = Math.round((getRandom().nextDouble() * maxRange + minRange) * 10.0) / 10.0;
                double num2 = Math.round((getRandom().nextDouble() * maxRange + minRange) * 10.0) / 10.0;

                double solution = switch (operator) {
                    case "+" -> num1 + num2;
                    case "-" -> num1 - num2;
                    case "*" -> num1 * num2;
                    case "/" -> num2 == 0 ? 0 : num1 / num2;
                    default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
                };

                Map<String, Object> ex = new HashMap<>();
                ex.put("num1", num1);
                ex.put("num2", num2);
                ex.put("operator", operator);
                ex.put("solution", Math.round(solution * 100.0) / 100.0); // עיגול ל-2 ספרות
                return ex;
            }
        };

        service.setMinRange(1);
        service.setMaxRange(level + 5);
        service.setOperator("/"); // אפשר לשנות לפי סוג תרגיל
        service.setRandom(new Random());

        return service;
    }

    }
