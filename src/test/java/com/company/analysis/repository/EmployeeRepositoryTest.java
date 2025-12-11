package com.company.analysis.repository;

import com.company.analysis.model.Employee;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryTest {

    @Test
    void testLoadEmployees_validCsv() throws Exception {
        // Sample CSV with header + 2 rows
        String csv =
                "Id,firstName,lastName,salary,managerId\n" +
                        "1,Satya,CEO,150000,\n" +
                        "2,Ram,Dorrai,60000,1\n";

        Path file = Files.createTempFile("employees", ".csv");
        Files.writeString(file, csv);

        EmployeeRepository repo = new EmployeeRepository();
        List<Employee> employees = repo.loadEmployees(file.toString());

        assertEquals(2, employees.size(), "Should load exactly 2 employees");

        Employee e1 = employees.get(0);
        Employee e2 = employees.get(1);

        assertEquals(1, e1.id());
        assertEquals("Satya", e1.firstName());
        assertEquals("CEO", e1.lastName());
        assertEquals(150000, e1.salary());
        assertNull(e1.managerId(), "CEO should have null managerId");

        assertEquals(2, e2.id());
        assertEquals("Ram", e2.firstName());
        assertEquals("Dorrai", e2.lastName());
        assertEquals(60000, e2.salary());
        assertEquals(1, e2.managerId());
    }

    @Test
    void testLoadEmployees_multipleRows() throws Exception {
        String csv =
                "Id,firstName,lastName,salary,managerId\n" +
                        "3,A,B,50000,2\n" +
                        "4,C,D,40000,2\n" +
                        "5,E,F,30000,1\n";

        Path file = Files.createTempFile("multi", ".csv");
        Files.writeString(file, csv);

        EmployeeRepository repo = new EmployeeRepository();
        List<Employee> employees = repo.loadEmployees(file.toString());

        assertEquals(3, employees.size());
        assertEquals(3, employees.get(0).id());
        assertEquals(4, employees.get(1).id());
        assertEquals(5, employees.get(2).id());
    }

    @Test
    void testLoadEmployees_handlesEmptyFileGracefully() throws Exception {
        String csv = "Id,firstName,lastName,salary,managerId\n"; // only header

        Path file = Files.createTempFile("empty", ".csv");
        Files.writeString(file, csv);

        EmployeeRepository repo = new EmployeeRepository();
        List<Employee> employees = repo.loadEmployees(file.toString());

        assertTrue(employees.isEmpty(), "No employees should be returned for empty CSV");
    }
}

