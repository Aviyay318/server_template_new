package com.app.Math;

import com.app.utils.Constants;
import java.util.Map;

public class AddSubtractService extends BaseMath {

    public static final int MINIMUM_MAX_RANGE = 10;
    public static final int MAXIMUM_MAX_RANGE = 100;



    public void shortAddAndSubtract(int level, double success, int questionType) {
        System.out.println("Level: " + level);
        System.out.println("Success Rate: " + success);

        this.maxRange = Math.max(level * 10, MINIMUM_MAX_RANGE); // הבטחה לערך מינימלי

        if (level % 2 != 0) {
            if (success >= SUCCESS_BOOST_THRESHOLD) {
                this.maxRange += (int) (this.maxRange * success);
            } else if (success < FAILURE_REDUCE_THRESHOLD && this.maxRange > MINIMUM_MAX_RANGE) {
                this.maxRange -= (int) (this.maxRange * success);
            }
        }

        this.maxRange = Math.min(this.maxRange, MAXIMUM_MAX_RANGE);

        this.operator = (level % 2 == 0) ? "-" : "+";
    }




}
