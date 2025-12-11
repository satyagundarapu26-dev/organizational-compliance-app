package com.company.analysis.repository;

import com.company.analysis.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    public List<Employee> loadEmployees(String filePath) throws IOException {
        List<Employee> list = new ArrayList<>();
        try (var br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                int id = Integer.parseInt(p[0].trim());
                String firstName = p[1].trim();
                String lastName = p[2].trim();
                double salary = Double.parseDouble(p[3].trim());
                Integer managerId = p.length > 4 && !p[4].trim().isEmpty() ? Integer.parseInt(p[4].trim()) : null;
                list.add(new Employee(id, firstName, lastName, salary, managerId));
            }
        }
        return list;
    }
}
