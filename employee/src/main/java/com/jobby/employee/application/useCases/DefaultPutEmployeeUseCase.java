package com.jobby.employee.application.useCases;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.in.PutEmployeeUseCase;
import org.springframework.stereotype.Service;

@Service
public class DefaultPutEmployeeUseCase implements PutEmployeeUseCase {

    @Override
    public Result<Employee, Error> execute(Employee employee) {
        return null;
    }
}
