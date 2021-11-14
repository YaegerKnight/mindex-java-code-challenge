package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {

        LOG.debug("Creating compensation for employee with id [{}]", compensation.getEmployee().getEmployeeId());

        // Added null check
        if(compensation.getEmployee().getEmployeeId() == null){
            throw new NullPointerException("Employee ID not found");
        }

        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployee().getEmployeeId());

        // Creating new compensation object and setting it's properties
        Compensation employeeCompensation = new Compensation();
        employeeCompensation.setEmployee(employee);
        employeeCompensation.setSalary(compensation.getSalary());
        employeeCompensation.setEffectiveDate(compensation.getEffectiveDate());

        // Inserting the compensation object into DB
        compensationRepository.insert(employeeCompensation);

        return employeeCompensation;

    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation for employee with id [{}]", employeeId);

        if(employeeId == null){
            throw new NullPointerException(" EmployeeId provided is null");
        }

        Compensation compensation = compensationRepository.findByEmployee(
                                    employeeRepository.findByEmployeeId(employeeId)
                                    );

        if(compensation == null){
            throw new NullPointerException(" Employee does not exist");
        }
        return compensation;
    }
}
