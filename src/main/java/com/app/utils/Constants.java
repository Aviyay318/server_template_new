package com.app.utils;


public class Constants {
    public static final String SCHEMA = "kids_learning";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "1234";
    public static final Integer EMAIL_EXIST = 1001;
    public static final String ADMIN_EMAIL="admin12345@gmail.com";
    public static final String ADMIN_PASSWORD = "admin12345";
public static final String ADMIN_OTP = "123456";
    public static final int ARITHMETIC_OPERATIONS = 1;            // פעולות חשבון
    public static final int LITERAL_PROBLEMS = 2;      // בעיות מילוליות
    public static final int EQUATIONS = 6;          // משוואות
    public static final int MULTIPLICATION_TABLE = 5; // לוח הכפל
    public static final int COMPLETE_TABLE = 4;     // השלם לוח
    public static final int MULTIPLE_CHOICE = 3;    // שאלה אמריקאית

    public static final int POINTS_TO_NEXT_LEVEL = 5;
    public static final int STREAK = 3;
    public static final int FAST_COMPLETION_TIME = 60 ;

    public static final int ADD_SUB_ISLAND = 1;
    public static final int MULTIPLICATION_ISLAND = 2;
    public static final int DIVISION_ISLAND = 3;
    public static final int ISLAND_DECIMALS_AND_FRACTIONS = 4;
    public static final int ISLAND_LONG_ADDITION_AND_SUBTRACTION = 5;
    public static final int ISLAND_LONG_MULTIPLICATION_AND_DIVISION = 6;
    public static final int ISLAND_MIXED_CHALLENGE = 7; // The Nightmare Island
    public static final int ISLAND_EQUATIONS = 8;


    public static final String[] QUESTION_TYPE = {
            "פעולות חשבון",
            "בעיות מילוליות",
            "משוואות",
            "לוח הכפל",
            "השלם לוח",
            "שאלה אמריקאית"
    };
    public static final int BASE_SCORE = 1;
    public static final int LITERAL_SCORE_NO_CLUE = 5;
    public static final int COMPLETE_TABLE_FAST_SCORE = 7;
    public static final int SCORE_WITH_CLUE = 2;
    public static final int WRONG_ANSWER_PENALTY = -2;
    public class IslandConstants {
        public static final int ISLAND_ADDITION_SUBTRACTION = 0;
        public static final int ISLAND_MULTIPLICATION = 50;
        public static final int ISLAND_DIVISION_FRACTIONS = 100;
        public static final int ISLAND_EVEN_ODD = 150;
        public static final int ISLAND_LONG_ADD_SUB = 200;
        public static final int ISLAND_LONG_MUL_DIV = 250;
        public static final int ISLAND_NIGHTMARE_MIXED = 300;
        public static final int ISLAND_EQUATIONS = 350;
    }

}
