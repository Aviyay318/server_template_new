package com.app.Math;

import java.util.*;

public class BaseMath {
    private static int idCounter = 1;

    protected int id;
    protected int maxRange;
    protected String operator;
    protected Map<String, Object> values = new HashMap<>();
    public static final double SUCCESS_BOOST_THRESHOLD = 0.80;
    public static final double FAILURE_REDUCE_THRESHOLD = 0.50;
    protected Random random;

    public BaseMath() {
        this.random = new Random();
        generateId();
    }

    protected void generateId() {
        this.id = idCounter++;
    }

    public Map<String, Object> getExercise() {


        int num1 = this.random.nextInt(Math.max(1, maxRange));
        int num2 = this.operator.equals("-")
                ? this.random.nextInt(Math.max(1, num1))
                : this.random.nextInt(Math.max(1, maxRange));


        this.values.put("id", id);
        this.values.put("num1", num1);
        this.values.put("operator", this.operator);
        this.values.put("num2", num2);
        this.values.put("equalsSign", "=");
        this.values.put("solution", calculate(num1, num2));

        return values;
    }

    private int calculate(int num1, int num2) {
        return switch (this.operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }
    public static Map<String, Object> generateOptions(int solution, int range, Random random) {
        Set<Integer> optionsSet = new HashSet<>();
        int min = Math.max(0, solution - range);
        int max = solution + range;

        // נוודא שהטווח לפחות בגודל 1
        if (max <= min) {
            max = min + range;
        }

        optionsSet.add(solution);

        while (optionsSet.size() < 4) {
            int r = max - min + 1;
            if (r <= 0) break; // הגנה נוספת
            int fakeOption = random.nextInt(r) + min;
            optionsSet.add(fakeOption);
        }

        List<Integer> options = new ArrayList<>(optionsSet);
        Collections.shuffle(options);

        Map<String, Object> result = new HashMap<>();
        result.put("options", options);
        result.put("correctIndex", options.indexOf(solution));
        return result;
    }
    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
