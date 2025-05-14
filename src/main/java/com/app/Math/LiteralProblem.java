package com.app.Math;

import com.app.entities.ChildrenNameEntity;
import com.app.entities.ObjectsEntity;

import java.util.*;

public class LiteralProblem {

    public static Map<String, Object> literalProblem(
            List<ObjectsEntity> objects,
            List<ChildrenNameEntity> childrenList,
            int maxRange,
            Random random,
            String operator,
            boolean useFractions,
            boolean useDecimals
    ) {

        ChildrenNameEntity name1 = childrenList.get(random.nextInt(childrenList.size()));
        ChildrenNameEntity name2 = childrenList.get(random.nextInt(childrenList.size()));

        ObjectsEntity obj1 = objects.get(random.nextInt(objects.size()));
        ObjectsEntity obj2 = objects.get(random.nextInt(objects.size()));

        Number num1;
        Number num2;

        if (useFractions) {
            int den1 = random.nextInt(maxRange - 1) + 1;
            int den2 = random.nextInt(maxRange - 1) + 1;
            int nume1 = random.nextInt(maxRange);
            int nume2 = random.nextInt(maxRange);
            num1 = new Fraction(nume1, den1);
            num2 = new Fraction(nume2, den2);
        } else if (useDecimals) {
            double d1 = Math.round((random.nextDouble() * maxRange) * 10.0) / 10.0;
            double d2 = Math.round((random.nextDouble() * maxRange) * 10.0) / 10.0;
            num1 = d1;
            num2 = (operator.equals("/") && d2 == 0) ? 1.0 : d2;
        } else {
            int i1 = random.nextInt(1, maxRange);
            int i2 = (operator.equals("-")) ? random.nextInt(1, i1) : random.nextInt(1, maxRange);
            num1 = i1;
            num2 = i2;
        }

        String template = getTemplate(operator);

        template = template.replace("{שם1}", name1.getName());
        template = template.replace("{שם2}", name2.getName());
        template = template.replace("{מספר1}", num1.toString());
        template = template.replace("{מספר2}", num2.toString());

        template = switch (operator) {
            case "+" -> template.replace("{זכר/נקבה1}", name2.getGender().equals("male") ? "נתן" : "נתנה");
            case "-" -> template.replace("{זכר/נקבה1}", name2.getGender().equals("male") ? "לקח" : "לקחה");
            case "*" -> template.replace("{זכר/נקבה1}", name1.getGender().equals("male") ? "קנה" : "קנתה");
            default -> template;
        };

        template = template.replace("{זכר/נקבה2}", name1.getGender().equals("male") ? "לו" : "לה");

        String nameObj1 = obj1.getPluralName();
        String nameObj2 = obj2.getPluralName();
        template = template.replace("{עצם1}", nameObj1);
        template = template.replace("{עצם2}", nameObj2);

        List<Map<String, Object>> imageHint = new ArrayList<>();
        imageHint.add(Map.of("name", nameObj1, "count", num1.toString(), "svg", obj1.getSvg()));
        imageHint.add(Map.of("name", nameObj2, "count", num2.toString(), "svg", obj2.getSvg()));

        String answerStr;
        if (num1 instanceof Fraction && num2 instanceof Fraction) {
            Fraction f1 = (Fraction) num1;
            Fraction f2 = (Fraction) num2;
            answerStr = switch (operator) {
                case "+" -> Fraction.add(f1, f2).toString();
                case "-" -> Fraction.subtract(f1, f2).toString();
                case "*" -> Fraction.multiply(f1, f2).toString();
                case "/" -> Fraction.divide(f1, f2).toString();
                default -> "0";
            };
        } else {
            double d1 = num1.doubleValue();
            double d2 = num2.doubleValue();
            double ans = switch (operator) {
                case "+" -> d1 + d2;
                case "-" -> d1 - d2;
                case "*" -> d1 * d2;
                case "/" -> d2 == 0 ? 0 : d1 / d2;
                default -> 0;
            };
            answerStr = String.format("%.2f", ans);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("question", template);
        result.put("hint", num1 + " " + operator + " " + num2);
        result.put("answer", answerStr);
        result.put("svg1", obj1.getSvg());
        result.put("svg2", obj2.getSvg());
        result.put("imageHint", imageHint);

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
    }}