package com.company.analysis.service;

import com.company.analysis.helper.OrganizationChart;
import com.company.analysis.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class ReportingLineService {
    private final OrganizationChart org;

    public ReportingLineService(OrganizationChart org) {
        this.org = org;
    }

    public List<String> findDepthViolations(int allowedDepth) {
        List<String> res = new ArrayList<>();
        for (Employee e : org.getAllEmployees()) {
            int depth = computeDepth(e);
            if (depth > allowedDepth) {
                res.add(String.format("Too Long -> %s depth is %d  (exceeds by %d)", e.getFullName(), depth
                        , (depth - allowedDepth)));
            }
        }
        return res;
    }

    private int computeDepth(Employee e) {
        int depth = 0;
        Integer managerId = e.managerId();
        while (managerId != null) {
            depth++;
            managerId = org.getEmployee(managerId).managerId();
        }
        return depth;
    }
}
