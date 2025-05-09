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
                String op = getOperatorByLevel(level);  // קובע אופרטור לפי שלב

                if (level % 2 == 0) {
                    // שלב זוגי → מספרים עשרוניים
                    double num1 = round(rand.nextDouble() * maxRange + minRange);
                    double num2;
                    do {
                        num2 = round(rand.nextDouble() * maxRange + minRange);
                    } while (op.equals("/") && num2 == 0);

                    double result = calculateDecimal(num1, num2, op);

                    ex.put("type", "decimal");
                    ex.put("num1", num1);
                    ex.put("num2", num2);
                    ex.put("operator", op);
                    ex.put("solution", result);
                    ex.put("solutionMethod", "פתור לפי פעולת " + op);
                } else {
                    // שלב אי-זוגי → תרגיל בין שברים
                    int denominator1, denominator2;

                    if (level <= 10) {
                        denominator1 = denominator2 = rand.nextInt(10) + 1;  // מכנה משותף
                    } else {
                        denominator1 = rand.nextInt(level + 5) + 1;
                        do {
                            denominator2 = rand.nextInt(level + 5) + 1;
                        } while (denominator2 == denominator1); // מכנים שונים
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
                    ex.put("solutionMethod", "בצע פעולה בין שברים: " + op);
                }

                return ex;
            }

            private double round(double val) {
                return Math.round(val * 100.0) / 100.0;
            }

            private double calculateDecimal(double a, double b, String op) {
                return switch (op) {
                    case "+" -> round(a + b);
                    case "-" -> round(a - b);
                    case "*" -> round(a * b);
                    case "/" -> b == 0 ? 0 : round(a / b);
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
        if (level <= 10) return "+";
        else if (level <= 20) return "-";
        else if (level <= 30) return "*";
        else if (level <= 40) return "/";
        else {
            String[] ops = {"+", "-", "*", "/"};
            return ops[new Random().nextInt(ops.length)];
        }
    }

    // מחלקת שברים עם פעולות
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
            int commonDenominator = lcm(a.denominator, b.denominator);
            int n1 = a.numerator * (commonDenominator / a.denominator);
            int n2 = b.numerator * (commonDenominator / b.denominator);
            return new Fraction(n1 + n2, commonDenominator);
        }

        public static Fraction subtract(Fraction a, Fraction b) {
            int commonDenominator = lcm(a.denominator, b.denominator);
            int n1 = a.numerator * (commonDenominator / a.denominator);
            int n2 = b.numerator * (commonDenominator / b.denominator);
            return new Fraction(n1 - n2, commonDenominator);
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
