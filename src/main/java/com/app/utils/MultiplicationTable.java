package com.app.utils;

import com.app.entities.BaseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultiplicationTable extends BaseEntity {
    private Integer num1;
    private String operand1;
    private Integer num2;
    private Integer num3;
    private String operandEqual;
    public static int count = 0;

    public MultiplicationTable() {
        this.operandEqual="=";
        count++;
        setId(count);
    }

    public void getExercise(int min, int max, String operand){
      this.num1 = min;
      this.num2 = new Random().nextInt(min,max+1);

    }

}
