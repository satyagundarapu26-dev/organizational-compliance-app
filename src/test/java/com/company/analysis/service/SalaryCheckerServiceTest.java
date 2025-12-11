package com.company.analysis.service;

import com.company.analysis.helper.OrganizationChart;
import com.company.analysis.helper.SalaryPolicy;
import com.company.analysis.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SalaryCheckerServiceTest {

    private OrganizationChart org;
    private SalaryPolicy policy;
    private SalaryCheckerService analyzer;

    // Sample employees
    private Employee manager;
    private Employee sub1;
    private Employee sub2;

    @BeforeEach
    void setUp() {
        org = mock(OrganizationChart.class);
        policy = mock(SalaryPolicy.class);
        analyzer = new SalaryCheckerService(org, policy);

        manager = new Employee(1, "Ram", "Dorrai", 60000, null);
        sub1 = new Employee(2, "Mahesh", "Chevvu", 30000, 1);
        sub2 = new Employee(3, "Bolian", "Ramodin", 36000, 1);

        // Employees returned by organization
        when(org.getAllEmployees()).thenReturn(List.of(manager, sub1, sub2));

        // Subordinates
        when(org.getSubordinates(1)).thenReturn(List.of(sub1, sub2));
        when(org.getSubordinates(2)).thenReturn(Collections.emptyList());
        when(org.getSubordinates(3)).thenReturn(Collections.emptyList());
    }

    @Test
    void testManagerNotUnderpaid() {
        // average = 33000, minAllowed = 20% above = 39600
        when(policy.getMin(33000)).thenReturn(39600.0);

        List<String> result = analyzer.findUnderpaidManagers();

        assertEquals(0, result.size(), "Manager is NOT underpaid because 60000 > 39600");
    }

    @Test
    void testManagerUnderpaid() {
        // Set minAllowed high so manager is underpaid
        when(policy.getMin(33000)).thenReturn(65000.0);

        List<String> result = analyzer.findUnderpaidManagers();

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("UNDERPAID"));
        assertTrue(result.get(0).contains("Ram Dorrai"));
        assertTrue(result.get(0).contains("5000")); // 65000 - 60000
    }

    @Test
    void testManagerNotOverpaid() {
        when(policy.getMax(33000)).thenReturn(80000.0);

        List<String> result = analyzer.findOverpaidManagers();

        assertEquals(0, result.size(), "Manager is NOT overpaid because 60000 < 80000");
    }

    @Test
    void testManagerOverpaid() {
        // Set max low → manager becomes overpaid
        when(policy.getMax(33000)).thenReturn(50000.0);

        List<String> result = analyzer.findOverpaidManagers();

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("OVERPAID"));
        assertTrue(result.get(0).contains("Ram Dorrai"));
        assertTrue(result.get(0).contains("10000"));
    }

    @Test
    void testExactlyAtMinLimit_NotUnderpaid() {
        when(policy.getMin(33000)).thenReturn(60000.0);

        List<String> result = analyzer.findUnderpaidManagers();

        assertTrue(result.isEmpty(), "Exactly at minimum should NOT be underpaid");
    }

    @Test
    void testExactlyAtMaxLimit_NotOverpaid() {
        when(policy.getMax(33000)).thenReturn(60000.0);

        List<String> result = analyzer.findOverpaidManagers();

        assertTrue(result.isEmpty(), "Exactly at maximum should NOT be overpaid");
    }

    @Test
    void testEmployeesWithoutSubordinatesAreIgnored() {
        // Make manager underpaid
        when(policy.getMin(33000)).thenReturn(70000.0);

        List<String> result = analyzer.findUnderpaidManagers();

        // Only employee with subordinates is manager
        assertEquals(1, result.size());
    }
}
