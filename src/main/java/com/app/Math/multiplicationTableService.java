package com.app.Math;

import java.util.Random;

public class multiplicationTableService extends BaseMath{


    public static final int MAXIMUM_MAX_RANGE = 10;



    public void shortAddAndSubtract(int level, double success, int questionType) {
        System.out.println(level);
        this.maxRange = MAXIMUM_MAX_RANGE;
        this.minRange = level<11?level:new Random().nextInt(1,MAXIMUM_MAX_RANGE);

        this.operator =  "*" ;
    }



}
