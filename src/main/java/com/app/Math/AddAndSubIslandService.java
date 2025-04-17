package com.app.Math;

import com.app.entities.*;
import com.app.service.Persist;
import com.app.utils.Constants;
import com.app.utils.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AddAndSubIslandService {

    @Autowired
    private Persist persist;
public static final int NUMBER_OF_MULTIPLE_QUESTION = 25;


    public Map<String, Object> generateExercise(String token, int questionType, int islandId) {
        UserEntity user = this.persist.getUserByToken(token);
        if (user == null) return null;

        IslandsEntity island = this.persist.loadObject(IslandsEntity.class, islandId);
        LevelsEntity level = this.persist.getLevelByUserIdAndIslandId(user, island);
        if (level == null) return null;

        double successRate = LevelUp.getSuccessRate(persist.getExercisesByUserId(user));
        AddSubtractService addSubtractService = new AddSubtractService();
        addSubtractService.shortAddAndSubtract(level.getLevel(), successRate, questionType);

        return switch (questionType) {
            case Constants.ARITHMETIC_OPERATIONS -> generateArithmeticExercise(user, island, level, addSubtractService);
            case Constants.LITERAL_PROBLEMS -> generateLiteralProblem(user, island, level, addSubtractService);
            case Constants.MULTIPLE_CHOICE -> generateMultipleChoice(user, island, level, addSubtractService);
            case Constants.COMPLETE_TABLE -> generateTableExercise(user, island, level, addSubtractService);
            default -> throw new IllegalStateException("Unexpected question type: " + questionType);
        };
    }

    private Map<String, Object> generateArithmeticExercise(UserEntity user, IslandsEntity island, LevelsEntity level, AddSubtractService service) {
        Map<String, Object> exercise = service.getExercise();
        String question = exercise.get("num1") + " " + exercise.get("operator") + " " + exercise.get("num2") + " = ";
        String answer = String.valueOf(exercise.get("solution"));
        saveExerciseHistory(user, island, level.getLevel(), Constants.ARITHMETIC_OPERATIONS, question, answer, service.getId());
        return exercise;
    }

    private Map<String, Object> generateLiteralProblem(UserEntity user, IslandsEntity island, LevelsEntity level, AddSubtractService service) {
        List<ObjectsEntity> objects = persist.loadList(ObjectsEntity.class);
        List<ChildrenNameEntity> childrenList = persist.loadList(ChildrenNameEntity.class);
        Map<String, Object> result = LiteralProblem.literalProblem(objects, childrenList, service.getMaxRange(), service.getRandom(), service.getOperator());
        saveExerciseHistory(user, island, level.getLevel(), Constants.LITERAL_PROBLEMS,
                (String) result.get("question"), String.valueOf(result.get("answer")), service.getId());
        return result;
    }

    private Map<String, Object> generateMultipleChoice(UserEntity user, IslandsEntity island, LevelsEntity level, AddSubtractService service) {
        Map<String, Object> questionData = service.getExercise();
        Integer solution = (Integer) questionData.get("solution");
        Map<String, Object> options = BaseMath.generateOptions(solution, 5, service.getRandom());
        questionData.putAll(options);
        String question = questionData.get("num1") + " " + questionData.get("operator") + " " + questionData.get("num2") + " = ?";
        saveExerciseHistory(user, island, level.getLevel(), Constants.MULTIPLE_CHOICE, question, String.valueOf(solution), service.getId());
        return questionData;
    }

    private Map<String, Object> generateTableExercise(UserEntity user, IslandsEntity island, LevelsEntity level, AddSubtractService service) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> exercises = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_MULTIPLE_QUESTION; i++) {
            service.getExercise();
            exercises.add(service.getValues());
        }
        resultMap.put("exercises", exercises);
        saveExerciseHistory(user, island, level.getLevel(), Constants.COMPLETE_TABLE, "Complete Table of 25 exercises", "N/A", service.getId());
        return resultMap;
    }

    private void saveExerciseHistory(UserEntity user, IslandsEntity island, int level, int questionType,
                                     String question, String answer, int exerciseId) {
        ExerciseHistoryEntity history = new ExerciseHistoryEntity();
        history.setUserId(user);
        history.setLevel(level);
        history.setIslands(island);
        history.setQuestionType(persist.loadObject(QuestionTypeEntity.class, questionType));
        history.setExercise(question);
        history.setAnswer(answer);
        history.setExerciseId(exerciseId);
        persist.save(history);
    }
}
