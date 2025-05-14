package com.app.Math;

public class Fraction extends Number {
    private final int numerator;
    private final int denominator;

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

    @Override
    public int intValue() {
        return (int) doubleValue();
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return (double) numerator / denominator;
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }
}

