package com.app.MathIslands;

import com.app.Math.BaseMath;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class FloatingPointIslandService extends BaseIslandService {

    @Override
    protected BaseMath createService(int level, double success, int questionType) {
        BaseMath service = new BaseMath() {
            @Override
            public Map<String, Object> getExercise() {
                Map<String, Object> ex = new HashMap<>();
                Random rand = getRandom();
                String op = getOperatorByLevel(level);  // אופרטור לפי שלב

                if (level % 2 == 0) {
                    // שלבים זוגיים - תרגילים עשרוניים עם דרגת קושי עולה
                    double num1, num2;
                    int decimalPlaces;

                    if (level <= 8) {
                        // שלבים 2–8: מספר אחד עשרוני והשני שלם, ספרה אחת אחרי הנקודה
                        decimalPlaces = 1;
                        num1 = round(rand.nextDouble() * maxRange + minRange, decimalPlaces);
                        num2 = rand.nextInt(maxRange - minRange + 1) + minRange;
                    } else if (level <= 16) {
                        // שלבים 10–16: שני מספרים עשרוניים עם ספרה אחת
                        decimalPlaces = 1;
                        num1 = round(rand.nextDouble() * maxRange + minRange, decimalPlaces);
                        num2 = round(rand.nextDouble() * maxRange + minRange, decimalPlaces);
                    } else if (level <= 36) {
                        // שלבים 18–36: עשרוני עם ספרה אחת או שתיים
                        decimalPlaces = rand.nextBoolean() ? 1 : 2;
                        num1 = round(rand.nextDouble() * maxRange + minRange, decimalPlaces);
                        num2 = round(rand.nextDouble() * maxRange + minRange, decimalPlaces);
                    } else {
                        // שלבים מתקדמים: 1–3 ספרות אחרי הנקודה
                        decimalPlaces = rand.nextInt(3) + 1;
                        num1 = round(rand.nextDouble() * maxRange + minRange, decimalPlaces);
                        num2 = round(rand.nextDouble() * maxRange + minRange, decimalPlaces);
                    }

                    if (op.equals("/") && num2 == 0) {
                        num2 = 1; // מניעת חילוק באפס
                    }

                    double result = calculateDecimal(num1, num2, op, decimalPlaces);

                    ex.put("type", "decimal");
                    ex.put("num1", num1);
                    ex.put("num2", num2);
                    ex.put("operator", op);
                    ex.put("solution", result);
                    ex.put("equalsSign", "=");
                    ex.put("solutionMethod", "פתור לפי פעולת " + op);
                } else {
                    // שלבים אי זוגיים – שברים
                    int denominator1, denominator2;
                    if (level <= 10) {
                        denominator1 = denominator2 = rand.nextInt(10) + 1;
                    } else {
                        denominator1 = rand.nextInt(level + 5) + 1;
                        do {
                            denominator2 = rand.nextInt(level + 5) + 1;
                        } while (denominator2 == 0);
                    }

                    int numerator1 = level;
                    int numerator2 = rand.nextInt(level + 5) + 1;

                    Fraction f1 = new Fraction(numerator1, denominator1);
                    Fraction f2 = new Fraction(numerator2, denominator2);

                    Fraction resultFraction = switch (op) {
                        case "+" -> Fraction.add(f1, f2);
                        case "-" -> Fraction.subtract(f1, f2);
                        case "*" -> Fraction.multiply(f1, f2);
                        case "/" -> Fraction.divide(f1, f2);
                        default -> throw new IllegalArgumentException("Invalid operator: " + op);
                    };

                    ex.put("type", "fraction");
                    ex.put("num1", f1.toString());
                    ex.put("num2", f2.toString());
                    ex.put("operator", op);
                    ex.put("solution", resultFraction.toString());
                    ex.put("equalsSign", "=");
                    ex.put("solutionMethod", "בצע פעולה בין שברים: " + op);
                }

                return ex;
            }

            private double round(double val, int decimalPlaces) {
                double factor = Math.pow(10, decimalPlaces);
                return Math.round(val * factor) / factor;
            }

            private double calculateDecimal(double a, double b, String op, int decimalPlaces) {
                return switch (op) {
                    case "+" -> round(a + b, decimalPlaces);
                    case "-" -> round(a - b, decimalPlaces);
                    case "*" -> round(a * b, decimalPlaces);
                    case "/" -> b == 0 ? 0 : round(a / b, decimalPlaces);
                    default -> throw new IllegalArgumentException("Invalid op: " + op);
                };
            }
        };

        service.setMinRange(1);
        service.setMaxRange(level + 5);
        service.setOperator(getOperatorByLevel(level));
        service.setRandom(new Random());

        return service;
    }

    private String getOperatorByLevel(int level) {
        String[] ops = {"+", "-", "*", "/"};

        if (level % 2 != 0) {
            int index = ((level - 1) / 2) % ops.length;
            return ops[index];
        }

        if (level <= 10) return "+";
        else if (level <= 20) return "-";
        else if (level <= 30) return "*";
        else if (level <= 40) return "/";
        else return ops[new Random().nextInt(ops.length)];
    }

    static class Fraction {
        int numerator;
        int denominator;

        public Fraction(int numerator, int denominator) {
            if (denominator == 0) denominator = 1;
            int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
        }

        public static Fraction add(Fraction a, Fraction b) {
            int commonDen = lcm(a.denominator, b.denominator);
            int n1 = a.numerator * (commonDen / a.denominator);
            int n2 = b.numerator * (commonDen / b.denominator);
            return new Fraction(n1 + n2, commonDen);
        }

        public static Fraction subtract(Fraction a, Fraction b) {
            int commonDen = lcm(a.denominator, b.denominator);
            int n1 = a.numerator * (commonDen / a.denominator);
            int n2 = b.numerator * (commonDen / b.denominator);
            return new Fraction(n1 - n2, commonDen);
        }

        public static Fraction multiply(Fraction a, Fraction b) {
            return new Fraction(a.numerator * b.numerator, a.denominator * b.denominator);
        }

        public static Fraction divide(Fraction a, Fraction b) {
            return new Fraction(a.numerator * b.denominator, a.denominator * b.numerator);
        }

        @Override
        public String toString() {
            return numerator + "/" + denominator;
        }

        private static int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }

        private static int lcm(int a, int b) {
            return a * b / gcd(a, b);
        }
    }
}
