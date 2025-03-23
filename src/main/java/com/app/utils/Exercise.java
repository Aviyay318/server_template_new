package com.app.utils;

public class Exercise {
    private int factor1;
    private int factor2;
    private int answer;
    private boolean solved;

    public Exercise(int factor1, int factor2) {
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.answer = factor1 * factor2;
        this.solved = false;
    }

    public int getFactor1() {
        return factor1;
    }

    public void setFactor1(int factor1) {
        this.factor1 = factor1;
    }

    public int getFactor2() {
        return factor2;
    }

    public void setFactor2(int factor2) {
        this.factor2 = factor2;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "factor1=" + factor1 +
                ", factor2=" + factor2 +
                ", answer=" + answer +
                ", solved=" + solved +
                '}';
    }
}
