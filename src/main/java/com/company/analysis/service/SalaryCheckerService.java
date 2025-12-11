package com.company.analysis.service;

import com.company.analysis.helper.OrganizationChart;
import com.company.analysis.helper.SalaryPolicy;
import com.company.analysis.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class SalaryCheckerService {
    private static final String STATUS_UNDERPAID = "UNDERPAID";
    private static final String STATUS_OVERPAID = "OVERPAID";

    private static final String LIMIT_MIN = "Min allowed";
    private static final String LIMIT_MAX = "Max allowed";
    private static final String STATUS_SHORTFALL = "shortfall";
    private static final String STATUS_EXCESS = "excess";

    private final OrganizationChart org;
    private final SalaryPolicy policy;

    public SalaryCheckerService(OrganizationChart org, SalaryPolicy policy) {
        this.org = org;
        this.policy = policy;
    }

    public List<String> findUnderpaidManagers() {
        return findManagers(true);
    }

    public List<String> findOverpaidManagers() {
        return findManagers(false);
    }

    private List<String> findManagers(boolean underpaid) {
        List<String> result = new ArrayList<>();

        for (Employee m : org.getAllEmployees()) {

            List<Employee> subs = org.getSubordinates(m.id());
            if (subs.isEmpty()) continue;

            double avg = subs.stream().mapToDouble(Employee::salary).average().orElse(0);

            double limit = underpaid ? policy.getMin(avg) : policy.getMax(avg);
            double salary = m.salary();

            if (underpaid && salary < limit) {
                result.add(buildMessage(m, limit, limit - salary, STATUS_UNDERPAID, STATUS_SHORTFALL, LIMIT_MIN));
            } else if (!underpaid && salary > limit) {
                result.add(buildMessage(m, limit, salary - limit, STATUS_OVERPAID, STATUS_EXCESS, LIMIT_MAX));
            }
        }

        return result;
    }

    private String buildMessage(Employee m, double threshold, double diff, String status, String status1, String minMaxStatus) {
        return String.format("%s -> %s earns %.2f, %s by %.2f (%s %.2f)", status, m.getFullName(), m.salary(), status1, diff, minMaxStatus, threshold);
    }
}
