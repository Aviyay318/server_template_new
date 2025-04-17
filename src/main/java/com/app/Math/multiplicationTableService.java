package com.app.Math;

public class multiplicationTableService extends BaseMath{


    public static final int MAXIMUM_MAX_RANGE = 100;



    public void shortAddAndSubtract(int level, double success, int questionType) {

        this.maxRange = ++level;
        this.maxRange = Math.min(this.maxRange, MAXIMUM_MAX_RANGE);

        this.operator =  "*" ;
    }



}
