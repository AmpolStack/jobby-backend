package com.jobby.employee.application.useCases;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.MessagingPublisher;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.in.PutEmployeeUseCase;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import com.jobby.employee.infraestructure.adapters.in.messaging.mappers.AvroEmployeeMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WritePutEmployeeUseCase implements PutEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final SafeResultValidator safeResultValidator;
    private final MessagingPublisher messagingPublisher;
    private final AvroEmployeeMapper employeeMapper;

    public WritePutEmployeeUseCase(@Qualifier("writeEmployeeRepository") EmployeeRepository employeeRepository,
                                   SafeResultValidator safeResultValidator,
                                   MessagingPublisher messagingPublisher,
                                   AvroEmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.safeResultValidator = safeResultValidator;
        this.messagingPublisher = messagingPublisher;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Result<Employee, Error> execute(Employee employee) {
        return this.employeeRepository.save(employee)
                .flatMap(employeeCreated -> this.employeeRepository.getEmployeeById(employeeCreated.getId()))
                .map(employeeConsulted ->{
                    var mapped = this.employeeMapper.toAvro(employeeConsulted);
                    this.messagingPublisher.publishAsync("com.jobby.messaging.event.employee.created.v1", mapped);
                    return employeeConsulted;
                });
    }
}
