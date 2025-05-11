package com.app.MathIslands;

import com.app.Math.AddSubtractService;
import com.app.Math.BaseMath;
import com.app.Math.LiteralProblem;
import com.app.entities.*;
import com.app.service.Persist;
import com.app.utils.Constants;
import com.app.utils.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class BaseIslandService implements MathIslandService {

    @Autowired
    protected Persist persist;
    protected abstract BaseMath createService(int level, double success, int questionType);

    protected int saveExerciseHistory(UserEntity user, IslandsEntity island, int level, int questionType, String question, String answer) {
        ExerciseHistoryEntity history = new ExerciseHistoryEntity();
        persist.save(history);
        history.setUserId(user);
        history.setLevel(level);
        history.setIslands(island);
        history.setQuestionType(persist.loadObject(QuestionTypeEntity.class, questionType));
        history.setExercise(question);
        history.setAnswer(answer);
        history.setExerciseId(history.getId());
        history.setCreatedAt(new Date());
        persist.save(history);
        return history.getId();
    }

    @Override
    public Map<String, Object> generateExercise(UserEntity user, IslandsEntity island, LevelsEntity level, int questionType) {
        double successRate = LevelUp.getSuccessRate(persist.getExercisesByUserId(user));
        BaseMath mathService = createService(level.getLevel(), successRate, questionType);

        return switch (questionType) {
            case Constants.ARITHMETIC_OPERATIONS -> generateArithmetic(user, island, level, mathService);
            case Constants.MULTIPLE_CHOICE -> generateMultipleChoice(user, island, level, mathService);
            case Constants.COMPLETE_TABLE -> generateTable(user, island, level, mathService);
            case Constants.LITERAL_PROBLEMS -> generateLiteralProblem(user, island, level, mathService);
            default -> throw new IllegalArgumentException("Invalid questionType: " + questionType);
        };
    }

    protected Map<String, Object> generateArithmetic(UserEntity user, IslandsEntity island, LevelsEntity level, BaseMath mathService) {
        Map<String, Object> exercise = mathService.getExercise();
        String question = exercise.get("num1") + " " + exercise.get("operator") + " " + exercise.get("num2") + " = ";
        String answer = String.valueOf(exercise.get("solution"));
        int exerciseId = saveExerciseHistory(user, island, level.getLevel(), Constants.ARITHMETIC_OPERATIONS, question, answer);
        exercise.put("id", exerciseId);
        return exercise;
    }

    protected Map<String, Object> generateMultipleChoice(UserEntity user, IslandsEntity island, LevelsEntity level, BaseMath mathService) {
        Map<String, Object> questionData = mathService.getExercise();
        Integer solution = (Integer) questionData.get("solution");
        Map<String, Object> options = BaseMath.generateOptions(solution, 5, mathService.getRandom());
        questionData.putAll(options);
        String question = questionData.get("num1") + " " + questionData.get("operator") + " " + questionData.get("num2") + " = ?";
        int exerciseId = saveExerciseHistory(user, island, level.getLevel(), Constants.MULTIPLE_CHOICE, question, String.valueOf(solution));
        questionData.put("id", exerciseId);
        return questionData;
    }

    protected Map<String, Object> generateTable(UserEntity user, IslandsEntity island, LevelsEntity level, BaseMath mathService) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> exercises = new ArrayList<>();
        for (int i = 0; i < AddAndSubIslandService.NUMBER_OF_MULTIPLE_QUESTION; i++) {
            Map<String, Object> exercise = mathService.getExercise();
            exercises.add(new HashMap<>(exercise));
        }
        resultMap.put("exercises", exercises);
        int exerciseId = saveExerciseHistory(user, island, level.getLevel(), Constants.COMPLETE_TABLE, "Table of 16 exercises", "N/A");
        resultMap.put("id", exerciseId);
        return resultMap;
    }

    protected Map<String, Object> generateLiteralProblem(UserEntity user, IslandsEntity island, LevelsEntity level, BaseMath service) {
        if (!(service.getOperator().equals("+") || service.getOperator().equals("-") || service.getOperator().equals("*") || service.getOperator().equals("/"))) {
            throw new IllegalArgumentException("Literal problems only supported for +, -, * or /");
        }


        List<ObjectsEntity> objects = persist.loadList(ObjectsEntity.class);
        List<ChildrenNameEntity> childrenList = persist.loadList(ChildrenNameEntity.class);
        Map<String, Object> result = LiteralProblem.literalProblem(
                objects,
                childrenList,
                service.getMaxRange(),
                service.getRandom(),
                service.getOperator()
        );

        int exerciseId = saveExerciseHistory(
                user,
                island,
                level.getLevel(),
                Constants.LITERAL_PROBLEMS,
                (String) result.get("question"),
                String.valueOf(result.get("answer"))
        );

        result.put("id", exerciseId);
        return result;
    }

}
