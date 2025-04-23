package com.app.Math;

import com.app.entities.ChildrenNameEntity;
import com.app.entities.ObjectsEntity;

import java.util.*;

public class LiteralProblem {
    public static Map<String, Object> literalProblem(List<ObjectsEntity> objects, List<ChildrenNameEntity> childrenList, int maxRange, Random random, String operator) {

        ChildrenNameEntity name1 = childrenList.get(random.nextInt(childrenList.size()));
        ChildrenNameEntity name2 = childrenList.get(random.nextInt(childrenList.size()));

        ObjectsEntity obj1 = objects.get(random.nextInt(objects.size()));
        ObjectsEntity obj2 = objects.get(random.nextInt(objects.size()));

        int num1 = random.nextInt(1, maxRange);
        int num2;
        if (operator.equals("-")) {
            num2 = random.nextInt(1, num1);
        } else if (operator.equals("/")) {
            num2 = random.nextInt(1, maxRange); // avoid division by 0
        } else {
            num2 = random.nextInt(1, maxRange);
        }

        String template = getTemplate(operator);

        template = template.replace("{שם1}", name1.getName());
        template = template.replace("{שם2}", name2.getName());
        template = template.replace("{מספר1}", String.valueOf(num1));
        template = template.replace("{מספר2}", String.valueOf(num2));

        template = switch (operator) {
            case "+" -> template.replace("{זכר/נקבה1}", name2.getGender().equals("male") ? "נתן" : "נתנה");
            case "-" -> template.replace("{זכר/נקבה1}", name2.getGender().equals("male") ? "לקח" : "לקחה");
            case "*" -> template.replace("{זכר/נקבה1}", name1.getGender().equals("male") ? "קנה" : "קנתה");
            default -> template;
        };

        template = template.replace("{זכר/נקבה2}", name1.getGender().equals("male") ? "לו" : "לה");

        String nameObj1 = (num1 > 1) ? obj1.getPluralName() : obj1.getSingularName();
        String nameObj2 = (num2 > 1) ? obj2.getPluralName() : obj2.getSingularName();
        template = template.replace("{עצם1}", nameObj1);
        template = template.replace("{עצם2}", nameObj2);

        List<Map<String, Object>> imageHint = new ArrayList<>();
        imageHint.add(Map.of(
                "name", nameObj1,
                "count", num1,
                "svg", obj1.getSvg()
        ));
        imageHint.add(Map.of(
                "name", nameObj2,
                "count", num2,
                "svg", obj2.getSvg()
        ));

        int answer = switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num2 == 0 ? 0 : num1 / num2;
            default -> throw new IllegalStateException("Unexpected operator: " + operator);
        };

        Map<String, Object> result = new HashMap<>();
        result.put("question", template);
        result.put("hint", num1 + " " + operator + " " + num2);
        result.put("answer", answer);
        result.put("svg1", obj1.getSvg());
        result.put("svg2", obj2.getSvg());
        result.put("imageHint", imageHint);

        System.out.println(result);
        return result;
    }

    private static String getTemplate(String operator) {
        return switch (operator) {
            case "+" -> "ל{שם1} יש {מספר1} {עצם1} ו{שם2} {זכר/נקבה1} {זכר/נקבה2} עוד {מספר2} {עצם2}. כמה {עצם1} {עצם2} יש ל{שם1} בסך הכול?";
            case "-" -> "ל{שם1} יש {מספר1} {עצם1} ו{שם2} {זכר/נקבה1} {זכר/נקבה2} {מספר2} {עצם2}. כמה {עצם1} {עצם2} נשארו ל{שם1}?";
            case "*" -> "{שם1} {זכר/נקבה1} {מספר1} {עצם1} בכל פעם. הוא עשה זאת {מספר2} פעמים השבוע. כמה {עצם1} יש ל{שם1} בסך הכול?";
            case "/" -> "ל{שם1} יש {מספר1} {עצם1}. הוא חילק אותם ל{מספר2} קבוצות שוות. כמה {עצם1} יש בכל קבוצה?";
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }
}