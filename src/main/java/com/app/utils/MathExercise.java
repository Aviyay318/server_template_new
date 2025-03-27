package com.app.utils;

import com.app.entities.BaseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MathExercise extends BaseEntity {
    private Integer num1;
    private String operand1;
    private Integer num2;
    private Integer num3;
    private String operandEqual;
    private Integer solution;
    private boolean isFastGrowth = false;
    public static int count = 0;

    public MathExercise() {}

    public MathExercise(int level) {
        this();
        generateLevel(level);
    }

    public MathExercise(int level, boolean isFastGrowth) {
        this();
        this.isFastGrowth = isFastGrowth;
        generateLevel(level);
    }

    private void generateLevel(int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be at least 1");
        }

        this.operandEqual = "=";
        count++;
        setId(count);

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
        this.num1 = randomNumber(min, max);
        this.num2 = randomNumber(min, max);
        this.num3 = randomNumber(min, max);
        this.operand1 = operand;

        if (this.operand1.equals("-") && this.num1 < this.num2) {
            swapNumbers();
        }

        if (this.operand1.equals("/")) {
            this.solution = randomNumber(1, max);
            this.num2 = randomNumber(1, max);
            this.num1 = this.solution * this.num2;
        }

        int tempSolution = calculate(this.num1, this.num2, this.operand1);
        this.solution = tempSolution;

        switch (missingIndex) {
            case 1 -> this.num3 = null;
            case 2 -> this.num2 = null;
            case 3 -> this.num1 = null;
        }
    }

    private void swapNumbers() {
        int temp = this.num1;
        this.num1 = this.num2;
        this.num2 = temp;
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

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", getId());
        json.put("num1", num1);
        json.put("operand1", operand1);
        json.put("num2", num2);
        json.put("num3", num3);
        json.put("operandEqual", operandEqual);
        json.put("solution", solution);
        return json;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public String getOperand1() {
        return operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    public Integer getNum3() {
        return num3;
    }

    public void setNum3(Integer num3) {
        this.num3 = num3;
    }

    public Integer getSolution() {
        return solution;
    }

    public void setSolution(Integer solution) {
        this.solution = solution;
    }

    public String getOperandEqual() {
        return operandEqual;
    }

    public void setOperandEqual(String operandEqual) {
        this.operandEqual = operandEqual;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        MathExercise.count = count;
    }

    public boolean isFastGrowth() {
        return isFastGrowth;
    }

    public void setFastGrowth(boolean fastGrowth) {
        isFastGrowth = fastGrowth;
    }

    @Override
    public String toString() {
        return Objects.toString(num1, "?") + " " +
                Objects.toString(operand1, "") + " " +
                Objects.toString(num2, "?") + " " +
                Objects.toString(operandEqual, "") + " " +
                Objects.toString(num3, "?") + " ";
    }
}
