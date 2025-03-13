package com.app.utils;

import com.app.entities.BaseEntity;

import java.util.Random;

public class MathExercise extends BaseEntity {
    private Integer num1;
    private String operand;
    private Integer num2;
    private String operandEqual;
    private Integer num3;
    public static int count = 0;
public MathExercise(){}


    public MathExercise(int level){
    this.operandEqual = "=";
    count++;
    setId(count);
    switch (level){
        case Constants.LEVEL_ONE -> level1();
    }
}
    private static int randomNumber(int num){
        return new Random().nextInt(num)+1;
    }

    private void level1(){
    this.num1 = randomNumber(9);
    this.operand = "+";
    this.num2 = randomNumber(9);
    }


    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    public String getOperandEqual() {
        return operandEqual;
    }

    public void setOperandEqual(String operandEqual) {
        this.operandEqual = operandEqual;
    }

    public Integer getNum3() {
        return num3;
    }

    public void setNum3(Integer num3) {
        this.num3 = num3;
    }

    @Override
    public String toString() {
        return "MathEntity{" +
                "num1=" + num1 +
                ", operand='" + operand + '\'' +
                ", num2=" + num2 +
                ", operandEqual='" + operandEqual + '\'' +
                ", num3=" + num3 +
                '}';
    }

}
