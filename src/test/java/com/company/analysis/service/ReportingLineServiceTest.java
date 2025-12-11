package com.company.analysis.service;

import com.company.analysis.helper.OrganizationChart;
import com.company.analysis.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportingLineServiceTest {

    private OrganizationChart org;
    private ReportingLineService analyzer;

    private Employee ceo;
    private Employee lvl1;
    private Employee lvl2;
    private Employee lvl3;
    private Employee lvl4;
    private Employee lvl5;
    private final Map<Integer, Employee> employees = new HashMap<>();

    @BeforeEach
    void setup() {
        List<Employee> list = new ArrayList<>();
        //list.add(new Employee(1, "CEO", null, 300000));

        ceo  = new Employee(1, "Satya", "CEO", 150000, null);
        lvl1 = new Employee(2, "Bob", "L1", 90000, 1);
        lvl2 = new Employee(3, "Charlie", "L2", 80000, 2);
        lvl3 = new Employee(4, "David", "L3", 70000, 3);
        lvl4 = new Employee(5, "Evan", "L4", 60000, 4);
        lvl5 = new Employee(6, "Frank", "L5", 50000, 5);

        // Manually stub out a small org chart
        org = new OrganizationChart(List.of(ceo, lvl1, lvl2, lvl3, lvl4, lvl5));

        analyzer = new ReportingLineService(org);
    }

    @Test
    void testNoViolationsWhenDepthWithinLimit() {
        List<String> results = analyzer.findDepthViolations(5);
        // only lvl5 has depth 5
        assertTrue(results.isEmpty(), "No employee should exceed depth 5");
    }

    @Test
    void testDetectsDepthViolation() {
        List<String> results = analyzer.findDepthViolations(3);

        assertEquals(2, results.size(), "Employees with depth > 3 should be flagged");

        assertTrue(
                results.get(0).contains("Evan L4") || results.get(1).contains("Evan L4"),
                "L4 should violate (depth=4)"
        );
        assertTrue(
                results.get(0).contains("Frank L5") || results.get(1).contains("Frank L5"),
                "L5 should violate (depth=5)"
        );
    }

    @Test
    void testDepthCalculationForCEO() {
        int depth = invokeComputeDepth(ceo);
        assertEquals(0, depth, "CEO depth must always be 0");
    }

    @Test
    void testDepthCalculationForLevel3Employee() {
        int depth = invokeComputeDepth(lvl3);
        assertEquals(3, depth, "L3 should have depth 3");
    }

    // Helper to call private computeDepth()
    private int invokeComputeDepth(Employee e) {
        try {
            var method = ReportingLineService.class.getDeclaredMethod("computeDepth", Employee.class);
            method.setAccessible(true);
            return (int) method.invoke(analyzer, e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

