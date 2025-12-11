package com.company.analysis;

import com.company.analysis.helper.OrganizationChart;
import com.company.analysis.helper.SalaryPolicy;
import com.company.analysis.model.Employee;
import com.company.analysis.repository.EmployeeRepository;
import com.company.analysis.service.ReportingLineService;
import com.company.analysis.service.SalaryCheckerService;

import java.io.IOException;
import java.util.List;

public class OrganizationalComplianceApp {
    private static final double MINIMUM_SALARY = 1.20;
    private static final double MAXIMUM_SALARY = 1.50;

    public static void main(String[] args) throws IOException {
        String filePath = "employees.csv";

        EmployeeRepository repository = new EmployeeRepository();
        List<Employee> employees = repository.loadEmployees(filePath);

        OrganizationChart orgChart = new OrganizationChart(employees);
        SalaryPolicy salaryPolicy = new SalaryPolicy(MINIMUM_SALARY, MAXIMUM_SALARY);
        SalaryCheckerService salaryCheckerService = new SalaryCheckerService(orgChart, salaryPolicy);
        ReportingLineService reportingLine = new ReportingLineService(orgChart);

        System.out.println("=== Managers Underpaid ===");
        salaryCheckerService.findUnderpaidManagers().forEach(System.out::println);

        System.out.println("\n=== Managers Overpaid ===");
        salaryCheckerService.findOverpaidManagers().forEach(System.out::println);

        System.out.println("\n=== Reporting Depth Violations ===");
        reportingLine.findDepthViolations(4).forEach(System.out::println);
    }
}
