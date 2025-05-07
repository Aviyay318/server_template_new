package com.app.utils;

public class InstructionGenerator {

    public static String getUserInstructions() {
        return String.format("""
                🧮 הוראות שימוש במערכת הלמידה:

                🎯 ניקוד:
                - תשובה נכונה רגילה: %d נקודות.
                - תשובה נכונה ללא שימוש ברמז בשאלה מילולית: %d נקודות.
                - תשובה נכונה מהירה (פחות מ-2 דקות בשאלות טבלה): %d נקודות.
                - שימוש ברמז: %d נקודות בלבד.
                - תשובה שגויה: הפחתה של %d נקודות.

                ⬆️ קידום רמה:
                - הצלחה של מעל %d%% מהשאלות + רצף של %d תשובות נכונות + %d תשובות מהירות – קידום מהיר.
                - הצלחה רגילה (%d%% ומעלה) – קידום רגיל.
                - הצלחה חלשה (פחות מ-%d%%) + רצף טעויות – ירידה ברמה.

                🧠 טיפים:
                - נסה לענות מהר (פחות מ-%d שניות) כדי לקבל בונוס!
                - שימוש ברמז מקטין ניקוד – השתמש בו רק כשצריך.
                - טעות זה חלק מהלמידה! המערכ0ת תתאים את הרמה עבורך.

                בהצלחה! 💪
                """,
                Constants.BASE_SCORE,
                Constants.LITERAL_SCORE_NO_CLUE,
                Constants.COMPLETE_TABLE_FAST_SCORE,
                Constants.SCORE_WITH_CLUE,
                Math.abs(Constants.WRONG_ANSWER_PENALTY),
                (int)(LevelUp.EXCELLENT_SUCCESS_RATE * 100),
                LevelUp.EXCELLENT_STREAK,
                LevelUp.EXCELLENT_FAST_ANSWERS,
                (int)(LevelUp.AVERAGE_SUCCESS_RATE * 100),
                (int)(LevelUp.POOR_SUCCESS_RATE * 100),
                LevelUp.FAST_COMPLETION_TIME
        );
    }
}
