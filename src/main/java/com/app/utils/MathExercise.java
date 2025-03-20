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
    public static int count = 0;

    public MathExercise() {}

    public MathExercise(int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be at least 1");
        }

        this.operandEqual = "=";
        count++;
        setId(count);

        // רשימת הטווחים לפי סדר
        int[][] numberRanges = {
                {1, 10}, {1, 30}, {1, 50}, {50, 100}, {100, 200}, {200, 500}, {500, 1000}
        };

        // חישוב שלב הפעולה והטווח
        int difficulty = (level - 1) / 12; // כל טווח יש 12 רמות (4 פעולות * 3 חסרים)
        int operationStage = (level - 1) % 12 / 3; // 0=חיבור, 1=חיסור, 2=כפל, 3=חילוק
        int missingIndex = (level - 1) % 3 + 1; // 1 = num3 חסר, 2 = num2 חסר, 3 = num1 חסר

        int min = numberRanges[difficulty][0];
        int max = numberRanges[difficulty][1];

        // בחירת הפעולה לפי הסדר שביקשת
        String operand;
        switch (operationStage) {
            case 0 -> operand = "+"; // חיבור
            case 1 -> operand = "-"; // חיסור
            case 2 -> operand = "*"; // כפל
            case 3 -> operand = "/"; // חילוק
            default -> throw new IllegalArgumentException("Invalid operation stage: " + operationStage);
        }

        generateExercise(operand, min, max, missingIndex);
    }

    private void generateExercise(String operand, int min, int max, int missingIndex) {
        this.num1 = randomNumber(min, max);
        this.num2 = randomNumber(min, max);
        this.num3 = randomNumber(min, max);
        this.operand1 = operand;

        // תיקון חיסור - למנוע תוצאה שלילית
        if (this.operand1.equals("-") && this.num1 < this.num2) {
            swapNumbers();
        }

        // תיקון חילוק - מבטיח תוצאה שלמה
        if (this.operand1.equals("/")) {
            this.solution = randomNumber(1, max);
            this.num1 = this.solution * this.num2; // כך num1 תמיד יתחלק ב-num2 ללא שארית
        }

        int tempSolution = calculate(this.num1, this.num2, this.operand1);
        this.solution = tempSolution;

        // קביעת איזה מספר יהיה חסר (תמיד רק אחד)
        if (missingIndex == 1) {
            this.num3 = null;
        } else if (missingIndex == 2) {
            this.num2 = null;
        } else if (missingIndex == 3) {
            this.num1 = null;
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

    @Override
    public String toString() {
        return Objects.toString(num1, "?") + " " +
                Objects.toString(operand1, "") + " " +
                Objects.toString(num2, "?") + " " +
                Objects.toString(operandEqual, "") + " " +
                Objects.toString(num3, "?") + " " ;
    }
}
