package com.app.utils;

import java.util.HashMap;
import java.util.Map;

public class InstructionGenerator {

    public static String getUserInstructions() {
        return String.format("""
                        🧮 <b>הוראות שימוש במערכת הלמידה:</b><br><br>
                        🎯 <b>ניקוד:</b><br>
                        - תשובה נכונה רגילה: %d נקודות.<br>
                        - תשובה נכונה ללא שימוש ברמז בשאלה מילולית: %d נקודות.<br>
                        - תשובה נכונה מהירה (פחות מ-2 דקות בשאלות טבלה): %d נקודות.<br>
                        - שימוש ברמז: %d נקודות בלבד.<br>
                        - תשובה שגויה: הפחתה של %d נקודות.<br><br>
                        ⬆️ <b>קידום רמה:</b><br>
                        - הצלחה של מעל %d%% מהשאלות + רצף של %d תשובות נכונות + %d תשובות מהירות – קידום מהיר.<br>
                        - הצלחה רגילה (%d%% ומעלה) – קידום רגיל.<br>
                        - הצלחה חלשה (פחות מ-%d%%) + רצף טעויות – ירידה ברמה.<br><br>
                        🧠 <b>טיפים:</b><br>
                        - נסה לענות מהר (פחות מ-%d שניות) כדי לקבל בונוס!<br>
                        - שימוש ברמז מקטין ניקוד – השתמש בו רק כשצריך.<br>
                        - טעות זה חלק מהלמידה! המערכת תתאים את הרמה עבורך.<br><br>
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


    private static Map<String, Object> createInstruction(
            String title, String description, String example,
            String explanation, String tip
    ) {
        Map<String, Object> instruction = new HashMap<>();
        instruction.put("title", title);
        instruction.put("description", description);
        instruction.put("example", example);
        if (explanation != null && !explanation.isEmpty()) {
            instruction.put("explanation", explanation);
        }
        instruction.put("tip", tip);
        return instruction;
    }

    public static Map<String, Object> createInstructionOf() {
        Map<String, Object> all = new HashMap<>();

        // אי החיבור והחיסור
        all.put("additionAndSubtractionIsland", createInstruction(
                "חיבור וחיסור",
                "חיבור עוזר לדעת כמה יש בסך הכול כשמצרפים מספרים. חיסור מראה כמה נשאר אחרי שמורידים.",
                "חיבור: 8 = 3 + 5\nחיסור: 5 = 9 - 4",
                null,
                "💡 בחיבור: התחל מהמספר הגדול וספור קדימה. בחיסור: אפשר לחשוב כמה חסר מהקטן לגדול."
        ));

        // אי החיבור והחיסור הארוך
        all.put("longAdditionAndSubtractionIsland", createInstruction(
                "חיבור וחיסור ארוך",
                "פתרון של חיבור וחיסור מספרים גדולים בעמודות – יחידות, עשרות, מאות.",
                "חיבור:  47\n       +36\n       ____\n        83\n\nחיסור:  72\n       -58\n       ____\n        14",
                "בחיבור: 6 + 7 = 13 → כותבים 3 ומעבירים 1. 4 + 3 + 1 = 8 → תוצאה: 83.\nבחיסור: 2−8 אי אפשר → שואלים 1 מה־7. 12−8=4, 6−5=1 → תוצאה: 14.",
                "💡 אל תשכח את ההעברות בחיבור ואת השאלות בחיסור!"
        ));

        // אי הכפל והחילוק הארוך
        all.put("longMultiplicationAndDivisionIsland", createInstruction(
                "כפל ארוך וחילוק ארוך",
                "כפל ארוך הוא כפל של מספרים גדולים בעמודות, וחילוק ארוך הוא חילוק של מספרים גדולים בשלבים.",
                "כפל:  23\n×   5\n_____\n  115\n\nחילוק:  728\n÷   7\n_____\n  104",
                "כפל: 5×3 = 15 → רושמים 5, מעבירים 1\n5×2 = 10 + 1 = 11 → תוצאה: 115\n\nחילוק: 7 נכנס ב־7 פעם אחת, מורידים ספרה וכו'…\nסך הכול: 104",
                "💡 תמיד שים לב להעברות ולשלב של כל מספר."
        ));

        //אי הכפל
        all.put("multiplicationIsland", createInstruction(
                "כפל",
                "כפל הוא חיבור של אותו מספר שוב ושוב. לדוגמה, 3 × 2 במקום 2 + 2 + 2.",
                "12 = 3 × 4 → כי 3 + 3 + 3 + 3 = 12",
                null,
                "💡 לוח הכפל עוזר לזכור תוצאות. דמיין קבוצות שוות."
        ));

        //אי החילוק
        all.put("divisionIsland", createInstruction(
                "חילוק",
                "חילוק מחלק כמות לחלקים שווים. בודקים כמה קבוצות שוות יש במספר.",
                "20 ÷ 5 = 4 כי 5 נכנס ב־20 ארבע פעמים.",
                null,
                "💡 אם אתה יודע שכפל של 4×5 = 20, אז 20÷5 = 4."
        ));

        //אי נקודה העשרונית
        all.put("floatingPointIsland", createInstruction(
                "שברים ומספרים עשרוניים",
                "שבר מייצג חלק ממשהו שלם, ומספר עשרוני מציין חלקים מהשלם בעזרת נקודה עשרונית.",
                "שברים: ¼ = אחד מתוך 4 חלקים\n\nמספרים עשרוניים: 1.75 = 1 שלם + ¾\n0.5 = חצי",
                "בשברים, המונה מראה כמה חלקים יש לך מהמכנה. במספרים עשרוניים, כל מספר אחרי הנקודה הוא חלק מהשלם. לדוגמה, 1.75 זה אחד ועוד ¾.",
                "🍰 ציור של עוגה או פיצה עוזר להבין שברים!\n💡 אפשר לדמיין סרגל מחולק ל־10 חלקים כדי להבין מספרים עשרוניים."
        ));

        // האי הסיוטי-שזה האי שמכיל תרגילים מכל האיים ביחד
        all.put("mixedChallengeIsland", createInstruction(
                "Mixed Challenge Island",
                "הסברים על חיבור, חיסור, כפל, חילוק, שברים ומספרים עשרוניים – הכל ביחד. אתגר שלם שמכיל את כל סוגי החשבונות.",
                "חיבור: 8 = 3 + 5\nחיסור: 5 = 9 - 4\nכפל: 12 = 3 × 4\nחילוק: 20 ÷ 5 = 4\nשברים: ¼ = אחד מתוך 4 חלקים\nמספרים עשרוניים: 1.75 = 1 שלם + ¾",
                "כל ההסברים שמדובר עליהם בתיאורים שונים – חיבור, חיסור, כפל, חילוק, שברים ומספרים עשרוניים – כל אחד מהם מצורף לעבודה עם אתגרי חשבון שונים.",
                "💡 זהו אתגר שיכול לשפר את הכישורים שלך בפתרון בעיות מסוגים שונים של חישובים!"
        ));

        //אי המשוואות
        all.put("equationsIsland", createInstruction(
                "משוואות",
                "הסברים על סוגי משוואות שונות, כיצד לפתור משוואות עם נעלם אחד, שני נעלמים ושלושה נעלמים.",
                "משוואה עם נעלם אחד: x + 5 = 12 → x = 7\n" +
                        "משוואה עם שני נעלמים: x + y = 10, x − y = 4 → x = 7, y = 3\n" +
                        "משוואה עם שלושה נעלמים: x + y + z = 6, x − y + z = 2, x + y − z = 4 → x = 3, y = -2, z = -1",
                "במשוואות עם נעלם אחד, נבודד את המשתנה על ידי ביצוע פעולה הפוכה בשני הצדדים. במשוואות עם שני נעלמים, נפתור בעזרת הצבה או חיסור, ובמשוואות עם שלושה נעלמים נשתמש בחיסורים והצבות עד שנמצא את הערכים של כל המשתנים.",
                "💡 אל תשכח לכתוב כל שלב, לבדוק את הפתרון ולבחור את השיטה שמתאימה לך ביותר."
        ));


        return all;
    }


    public static Object getInstructionById(int islandId) {
        Map<String, Object> all = new HashMap<>();
        all = createInstructionOf();
        return switch (islandId) {
//            case Constants.ADD_SUB_ISLAND -> (String) all.get("additionAndSubtractionIsland");
            case Constants.ADD_SUB_ISLAND -> all.get("additionAndSubtractionIsland");
            case Constants.MULTIPLICATION_ISLAND ->  all.get("multiplicationIsland");
            case Constants.DIVISION_ISLAND->  all.get("divisionIsland");
            case Constants.ISLAND_DECIMALS_AND_FRACTIONS ->  all.get("floatingPointIsland");
            case Constants.ISLAND_LONG_ADDITION_AND_SUBTRACTION -> all.get("longAdditionAndSubtractionIsland");
            case Constants.ISLAND_LONG_MULTIPLICATION_AND_DIVISION ->  all.get("longMultiplicationAndDivisionIsland");
            case Constants.ISLAND_MIXED_CHALLENGE -> all.get("mixedChallengeIsland");
            case Constants.ISLAND_EQUATIONS ->  all.get("equationsIsland");
            default -> "there is no match at all";
        };
    }
}



