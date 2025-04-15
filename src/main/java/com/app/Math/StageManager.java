package com.app.Math;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

    public static final double SUCCESS_BOOST_THRESHOLD = 0.80;
    public static final double FAILURE_REDUCE_THRESHOLD = 0.50;
    public static final int BASE_RANGE_MULTIPLIER = 10;
    public static final int MINIMUM_MAX_RANGE = 10;

    public static Map<String,Object> stageValuesGenerator(int level, double success) {
        Map<String, Object> values = new HashMap<>();
        int maxRate = level * BASE_RANGE_MULTIPLIER;
        String operand = "";
        if (success >= SUCCESS_BOOST_THRESHOLD) {
            maxRate += (int) (maxRate * success);
        } else if (success < FAILURE_REDUCE_THRESHOLD && maxRate > MINIMUM_MAX_RANGE) {
            maxRate -= (int) (maxRate * success);
        }
        if (level<=10){
             operand = level%2==0?"-":"+";
        } else if (level<=20) {
           operand = level%2==0?"*":"/";
           maxRate = 10;
        }


        values.put("max", maxRate);
        values.put("operand",operand);
        return values;
    }

    public static void main(String[] args) {
        System.out.println(stageValuesGenerator(3, 0.85)); // תוספת
        System.out.println(stageValuesGenerator(3, 0.45)); // ירידה
        System.out.println(stageValuesGenerator(12, 0.55)); // נשאר רגיל
    }
}
