package com.app.Math;

public class AddSubtractService extends BaseMath {

    public static final int MINIMUM_MAX_RANGE = 10;
    public static final int SHORT_MAXIMUM_MAX_RANGE = 100;



    public void shortAddAndSubtract(int level, double success, int questionType) {
        System.out.println("Level: " + level);
        System.out.println("Success Rate: " + success);

        this.maxRange =level * 10;

        if (level % 2 != 0) {
            if (success >= SUCCESS_BOOST_THRESHOLD) {
                this.maxRange += (int) (this.maxRange * success);
            } else if (success < FAILURE_REDUCE_THRESHOLD && this.maxRange > MINIMUM_MAX_RANGE) {
                this.maxRange -= (int) (this.maxRange * success);
            }
        }

        this.maxRange = Math.min(this.maxRange, SHORT_MAXIMUM_MAX_RANGE);

        this.operator = (level % 2 == 0) ? "-" : "+";
    }


    public void longAddAndSubtract(int level, double success, int questionType) {
        System.out.println("Level: " + level);
        System.out.println("Success Rate: " + success);

        this.maxRange = level * 100; // התחלה ב-100 כדי להרגיש "ארוך"

        // חיזוק/החלשה לפי הצלחה
        if (success >= SUCCESS_BOOST_THRESHOLD) {
            this.maxRange += (int)(this.maxRange * success * 0.5); // העלאה מתונה
        } else if (success < FAILURE_REDUCE_THRESHOLD) {
            this.maxRange -= (int)(this.maxRange * success * 0.5); // הקטנה קלה
        }

        this.maxRange = Math.min(this.maxRange, 1000); // תקרה כדי למנוע עומס

        // לסירוגין פלוס או מינוס, בהתאם לרמה
        this.operator = (level % 2 == 0) ? "-" : "+";
    }

}
