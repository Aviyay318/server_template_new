package com.app.utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class QuestionGenerator {
    static Random random = new Random();

    static Map<String, String> genderMap = new HashMap<>();
    static Map<String, String> pluralMap = new HashMap<>();

    static {
        for (String object : DataGenerator.singleObjects) {
            String gender = object.endsWith("ה") || object.endsWith("ת") ? "נקבה" : "זכר";
            String plural = fixIrregularPlurals(object);
            genderMap.put(object, gender);
            pluralMap.put(object, plural);
        }
    }

    static Set<String> boyNameSet = new HashSet<>(Arrays.asList(DataGenerator.boyNames));
    static Set<String> girlNameSet = new HashSet<>(Arrays.asList(DataGenerator.girlNames));

    static String[] templates = {
            "[שם] לקח [עצם1] ונתן אחד לחבר.",
            "[שם] קיבל מתנה - [עצם1] ועוד [עצם2].",
            "[שם] שיחק עם [עצם1].",
            "[שם] הביא לבית הספר [עצם1] ו[עצם2].",
            "[שם] מצא [עצם1] ברחוב. אחר כך מצא עוד [עצם2]."
    };

    public static Map<String, Object> generateQuestion() {
        String name;
        boolean isBoy;

        if (random.nextBoolean()) {
            name = getRandom(DataGenerator.boyNames);
            isBoy = true;
        } else {
            name = getRandom(DataGenerator.girlNames);
            isBoy = false;
        }

        if (girlNameSet.contains(name) && !boyNameSet.contains(name)) {
            isBoy = false;
        } else if (boyNameSet.contains(name) && !girlNameSet.contains(name)) {
            isBoy = true;
        }

        String template = getRandom(templates);

        String word1 = getRandom(new ArrayList<>(genderMap.keySet()));
        String word2 = getRandom(new ArrayList<>(genderMap.keySet()));

        int n1 = randomInt(2, 5);
        int n2 = template.contains("נתן אחד") ? 0 : randomInt(1, 4);

        String formatted1 = format(word1, n1);
        String formatted2 = format(word2, n2);

        String pronoun = isBoy ? "לו" : "לה";
        String ending;

        if (template.contains("נתן אחד")) {
            ending = String.format("כמה %s נשארו ל%s בסוף?", pluralMap.get(word1), name);
        } else {
            ending = String.format("כמה %s ו%s היו ל%s בסך הכל?",
                    pluralMap.get(word1), pluralMap.get(word2), name);
        }

        String question = template
                .replace("[שם]", name)
                .replace("[עצם1]", formatted1)
                .replace("[עצם2]", formatted2)
                .replaceAll("[.?!]+$", "");

        String fullQuestion = question + ". " + ending;
        int answer = template.contains("נתן אחד") ? n1 - 1 : n1 + n2;

        Map<String, Object> result = new HashMap<>();
        result.put("question", fullQuestion);
        result.put("hint", template.contains("נתן אחד") ? n1 + " - 1" : n1 + " + " + n2);
        result.put("answer", answer);
        return result;
    }

    private static String format(String word, int number) {
        String gender = genderMap.getOrDefault(word, "זכר");
        if (number == 1) {
            return word + (gender.equals("נקבה") ? " אחת" : " אחד");
        } else {
            return number + " " + pluralMap.getOrDefault(word, findPlural(word));
        }
    }

    private static String fixIrregularPlurals(String word) {
        Map<String, String> exceptions = new HashMap<>();
        exceptions.put("כיסא", "כיסאות");
        exceptions.put("דגל קטן", "דגלים קטנים");
        exceptions.put("שלט ברוכים הבאים", "שלטים ברוכים הבאים");
        exceptions.put("מדבקת שם", "מדבקות שם");
        exceptions.put("מספריים", "מספריים");
        exceptions.put("מכשיר קשר", "מכשירי קשר");
        exceptions.put("פעמון", "פעמונים");
        exceptions.put("חבל קפיצה", "חבלי קפיצה");
        exceptions.put("שקית הפתעה", "שקיות הפתעה");
        exceptions.put("לוח ציור", "לוחות ציור");
        exceptions.put("מדף", "מדפים");
        exceptions.put("שרשרת", "שרשראות");
        exceptions.put("שלט מעוצב", "שלטים מעוצבים");
        exceptions.put("טוש מחיק", "טושים מחיקים");
        exceptions.put("מכנסיים", "מכנסיים");
        exceptions.put("בריסטול צבעוני", "בריסטולים צבעוניים");
        exceptions.put("כריך", "כריכים");
        exceptions.put("עכבר", "עכברים");
        exceptions.put("דיסק", "דיסקים");
        exceptions.put("כיסא חוף", "כיסאות חוף");
        exceptions.put("פחית צבע", "פחיות צבע");
        exceptions.put("כרטיס ברכה", "כרטיסי ברכה");
        exceptions.put("תבלין", "תבלינים");

        if (exceptions.containsKey(word)) {
            return exceptions.get(word);
        }

        return findPlural(word);
    }

    private static String findPlural(String word) {
        if (word.endsWith("ף")) return word.substring(0, word.length() - 1) + "פים";
        if (word.endsWith("ה")) return word.substring(0, word.length() - 1) + "ות";
        if (word.endsWith("ת")) return word.substring(0, word.length() - 1) + "ות";
        if (word.endsWith("ון")) return word.substring(0, word.length() - 2) + "ונים";
        if (word.endsWith("ן")) return word.substring(0, word.length() - 1) + "נים";
        return word + "ים";
    }

    private static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private static String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }

    private static String getRandom(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }


}
