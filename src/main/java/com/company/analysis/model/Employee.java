package com.company.analysis.model;

public record Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
