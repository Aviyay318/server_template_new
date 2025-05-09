package com.app.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructionGenerator {

    public static Map<String, Object> getUserInstructionsJson() {
        Map<String, Object> result = new HashMap<>();

        List<String> scoring = List.of(
                "תשובה נכונה רגילה: " + Constants.BASE_SCORE + " נקודות.",
                "תשובה נכונה ללא שימוש ברמז בשאלה מילולית: " + Constants.LITERAL_SCORE_NO_CLUE + " נקודות.",
                "תשובה נכונה מהירה (פחות מ-2 דקות בהשלם את הלוח): " + Constants.COMPLETE_TABLE_FAST_SCORE + " נקודות.",
                "שימוש ברמז: " + Constants.SCORE_WITH_CLUE + " נקודות בלבד.",
                "תשובה שגויה: הפחתה של " + Math.abs(Constants.WRONG_ANSWER_PENALTY) + " נקודות."
        );

        List<String> leveling = List.of(
                "הצלחה של מעל " + (int) (LevelUp.EXCELLENT_SUCCESS_RATE * 100) + "% מהשאלות + רצף של " + LevelUp.EXCELLENT_STREAK + " תשובות נכונות + " + LevelUp.EXCELLENT_FAST_ANSWERS + " תשובות מהירות – קידום מהיר.",
                "הצלחה רגילה (" + (int) (LevelUp.AVERAGE_SUCCESS_RATE * 100) + "% ומעלה) – קידום רגיל.",
                "הצלחה חלשה (פחות מ-" + (int) (LevelUp.POOR_SUCCESS_RATE * 100) + "%) + רצף טעויות – ירידה ברמה."
        );

        List<String> helpers = List.of(
                "בכל תרגיל עומד לרשותכם לוח עזר אישי – תוכלו לצייר, לכתוב ולעשות בו שימוש חופשי.",
                "עד טווח 30 תופיע גם עזרה ויזואלית: צורות, סמלים ודוגמאות שיעזרו לכם להבין טוב יותר.",
                "מטווח 31 ואילך – העזרה תהפוך לטקסטואלית (רמזים במילים בלבד), כך שתוכלו להתקדם בהבנה."
        );


        List<String> tips = List.of(
                "נסו לענות מהר (פחות מ-" + LevelUp.FAST_COMPLETION_TIME + " שניות) כדי לקבל בונוס!",
                "השתמשו ברמזים כשאתם באמת צריכים – הם מורידים ניקוד.",
                "אל תפחדו לטעות – ככה לומדים! המערכת מתאימה את עצמה לקצב שלכם."
        );

        result.put("scoring", scoring);
        result.put("leveling", leveling);
        result.put("helpers", helpers);
        result.put("tips", tips);

        return result;
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



