package com.app.utils;


public class Constants {
    public static final String SCHEMA = "kids_learning";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "1234";
    public static final Integer EMAIL_EXIST = 1001;
    public static final String ADMIN_EMAIL="admin12345@gmail.com";
    public static final String ADMIN_PASSWORD = "admin12345";

    public static final int ARITHMETIC_OPERATIONS = 1;            // פעולות חשבון
    public static final int LITERAL_PROBLEMS = 2;      // בעיות מילוליות
    public static final int EQUATIONS = 3;          // משוואות
    public static final int MULTIPLICATION_TABLE = 4; // לוח הכפל
    public static final int COMPLETE_TABLE = 5;     // השלם לוח
    public static final int MULTIPLE_CHOICE = 6;    // שאלה אמריקאית

    public static final int POINTS_TO_NEXT_LEVEL = 5;
    public static final int STREAK = 3;
    public static final int FAST_COMPLETION_TIME = 60 ;
    public static final String[] QUESTION_TYPE = {
            "פעולות חשבון",
            "בעיות מילוליות",
            "משוואות",
            "לוח הכפל",
            "השלם לוח",
            "שאלה אמריקאית"
    };
}
