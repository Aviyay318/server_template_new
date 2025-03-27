package com.app.utils;

import com.app.entities.BaseEntity;
import com.app.entities.QuestionTypeEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MathExerciseNew extends BaseEntity {
  private Map<String,Object> exercise;

    private static int count=0;
    public MathExerciseNew(int level, QuestionTypeEntity type,boolean isFastGrowth){
        this.exercise = new HashMap<>();
        if (level<1){
            throw new IllegalArgumentException("Level must be at least 1");
        }else {
           count++;
           setId(count);

           switch (type.getId()){
               case Constants.ARITHMETIC_OPERATIONS ->generateLevel(level,isFastGrowth);
           }
        }
    }
    private void generateLevel(int level,boolean isFastGrowth) {

        String operandEqual = "=";
        int difficulty = (level - 1) / 12;
        int operationStage = (level - 1) % 12 / 3;

        int min = 1;
        int max;
        if (isFastGrowth) {
            max = 10 * (int) Math.pow(2, difficulty);
        } else {
            max = 10 + difficulty * 10;
        }

        int missingIndex;
        if (max <= 20) {
            missingIndex = 1; // num3 חסר בלבד ברמות נמוכות
        } else {
            missingIndex = (level - 1) % 3 + 1;
        }

        String operand;
        switch (operationStage) {
            case 0 -> operand = "+";
            case 1 -> operand = "-";
            case 2 -> operand = "*";
            case 3 -> operand = "/";
            default -> throw new IllegalArgumentException("Invalid operation stage: " + operationStage);
        }

        generateExercise(operand, min, max, missingIndex);

    }

    private void generateExercise(String operand, int min, int max, int missingIndex) {
        Integer num1 = null;
        Integer num2 = null;
        Integer num3 = null;
        String operand1 = null;
        Integer solution = null;
        String operandEqual = "=";

        num1 = randomNumber(min, max);
        num2 = randomNumber(min, max);
        num3 = randomNumber(min, max);
        operand1 = operand;

        if (operand1.equals("-") && num1 < num2) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }

        if (operand1.equals("/")) {
            solution = randomNumber(1, max);
            num2 = randomNumber(1, max);
            num1 = solution * num2;
        }

        int tempSolution = calculate(num1, num2, operand1);
        solution = tempSolution;

        switch (missingIndex) {
            case 1 -> num3 = null;
            case 2 -> num2 = null;
            case 3 -> num1 = null;
        }

        this.exercise.put("id", getId());
        this.exercise.put("num1", num1);
        this.exercise.put("operand1", operand1);
        this.exercise.put("num2", num2);
        this.exercise.put("num3", num3);
        this.exercise.put("operandEqual", operandEqual);
        this.exercise.put("solution", solution);
    }


    private int calculate(int num1, int num2, String operator) {
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> (num2 != 0) ? num1 / num2 : num1;
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }

    private static int randomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public Map<String, Object> getExercise() {
        return exercise;
    }

    public void setExercise(Map<String, Object> exercise) {
        this.exercise = exercise;
    }
}
