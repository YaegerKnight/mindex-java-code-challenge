package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getNumberOfReports(String employeeId){

        if(employeeId == null){
            throw new NullPointerException(" EmployeeId provided is null");
        }
        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        List<Employee> directReportees = employee.getDirectReports();

        Queue<String> employeeIds = new LinkedList<>();
        Set<String> totalReportingEmployeeIds = new HashSet<>();

        if (employee.getDirectReports() != null){
            for (Employee e: directReportees){
                employeeIds.add(e.getEmployeeId());
            }
        }

        while(!employeeIds.isEmpty()){

            String tempEmployeeId = employeeIds.remove();
            Employee tempEmployee = employeeRepository.findByEmployeeId(tempEmployeeId);

            if(tempEmployee.getDirectReports() != null && !tempEmployee.getDirectReports().isEmpty()){
                for (Employee e: tempEmployee.getDirectReports()){
                    employeeIds.add(e.getEmployeeId());
                }
            }

            totalReportingEmployeeIds.add(tempEmployeeId);
        }

        ReportingStructure reportingStructure = new ReportingStructure(employee, totalReportingEmployeeIds.size());

        return reportingStructure;
    }
}
