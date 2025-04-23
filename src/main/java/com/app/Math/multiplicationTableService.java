package com.app.Math;

public class multiplicationTableService extends BaseMath{


    public static final int MAXIMUM_MAX_RANGE = 10;



    public void shortAddAndSubtract(int level, double success, int questionType) {
        System.out.println(level);
        this.maxRange = level*2;
        this.maxRange = Math.min(this.maxRange, MAXIMUM_MAX_RANGE);

        this.operator =  "*" ;
    }



}
