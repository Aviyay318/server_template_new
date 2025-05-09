package com.app.controllers;

public class Fraction {
    public int numerator;
    public int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) denominator = 1;
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    public double toDouble() {
        return (double) numerator / denominator;
    }

    public String toString() {
        return numerator + "/" + denominator;
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
