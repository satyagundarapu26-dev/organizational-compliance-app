package com.company.analysis.helper;

import com.company.analysis.model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationChart {
    private final Map<Integer, Employee> employees;
    private final Map<Integer, List<Employee>> subordinates;
    private final Employee ceo;

    public OrganizationChart(List<Employee> list) {
        employees = new HashMap<>();
        for (Employee e : list)
            employees.put(e.id(), e);

        subordinates = new HashMap<>();
        for (Employee e : list) {
            if (e.managerId() != null)
                subordinates.computeIfAbsent(e.managerId(), k -> new ArrayList<>()).add(e);
        }

        ceo = list.stream().filter(e -> e.managerId() == null).findFirst().orElseThrow();
    }

    public Employee getEmployee(int id) {
        return employees.get(id);
    }

    public List<Employee> getSubordinates(int m) {
        return subordinates.getOrDefault(m, List.of());
    }

    public Employee getCEO() {
        return ceo;
    }

    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }
}
