package com.company.analysis.helper;

public class SalaryPolicy {
    private final double minMultiplier;
    private final double maxMultiplier;

    public SalaryPolicy(double minMultiplier, double maxMultiplier) {
        this.minMultiplier = minMultiplier;
        this.maxMultiplier = maxMultiplier;
    }

    public double getMin(double avg) { return avg * minMultiplier; }
    public double getMax(double avg) { return avg * maxMultiplier; }
}
