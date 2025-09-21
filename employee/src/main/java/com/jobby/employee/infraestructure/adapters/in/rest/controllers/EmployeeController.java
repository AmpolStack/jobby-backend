package com.jobby.employee.infraestructure.adapters.in.rest.controllers;

import com.jobby.employee.domain.ports.in.GetEmployeeByIdUseCase;
import com.jobby.employee.domain.ports.in.PutEmployeeUseCase;
import com.jobby.employee.infraestructure.adapters.in.rest.dto.created.EmployeeCreated;
import com.jobby.employee.infraestructure.adapters.in.rest.mappers.EmployeeCreatedMapper;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final GetEmployeeByIdUseCase getEmployeeByIdUseCase;
    private final PutEmployeeUseCase putEmployeeUseCase;
    private final ApiResponseMapper apiResponseMapper;
    private final EmployeeCreatedMapper employeeCreatedMapper;

    public EmployeeController(GetEmployeeByIdUseCase getEmployeeByIdUseCase, PutEmployeeUseCase putEmployeeUseCase, ApiResponseMapper apiResponseMapper, EmployeeCreatedMapper employeeCreatedMapper) {
        this.getEmployeeByIdUseCase = getEmployeeByIdUseCase;
        this.putEmployeeUseCase = putEmployeeUseCase;
        this.apiResponseMapper = apiResponseMapper;
        this.employeeCreatedMapper = employeeCreatedMapper;
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getById(@RequestHeader("X-User-Id") int id) {
        var resp = this.getEmployeeByIdUseCase
                .execute(id);

        return this.apiResponseMapper.map(resp);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmployeeCreated employee) {
        var domainEmployee = this.employeeCreatedMapper.toDomain(employee);
        var resp = this.putEmployeeUseCase
                .execute(domainEmployee);

        return this.apiResponseMapper.map(resp);
    }
}
