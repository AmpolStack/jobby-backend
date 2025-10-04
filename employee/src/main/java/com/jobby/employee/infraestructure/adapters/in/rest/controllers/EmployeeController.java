package com.jobby.employee.infraestructure.adapters.in.rest.controllers;

import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.in.GetEmployeeByIdUseCase;
import com.jobby.employee.domain.ports.in.PutEmployeeUseCase;
import com.jobby.employee.infraestructure.adapters.in.rest.dto.created.EmployeeCreated;
import com.jobby.employee.infraestructure.adapters.in.rest.mappers.EmployeeCreatedMapper;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaAppUserEntity;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaEmployeeEntity;
import com.jobby.infraestructure.common.EncryptionPropertyInitializer;
import com.jobby.infraestructure.common.MacPropertyInitializer;
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
    private final MacPropertyInitializer macPropertyInitializer;
    private final EncryptionPropertyInitializer encryptionPropertyInitializer;

    public EmployeeController(GetEmployeeByIdUseCase getEmployeeByIdUseCase, PutEmployeeUseCase putEmployeeUseCase, ApiResponseMapper apiResponseMapper, EmployeeCreatedMapper employeeCreatedMapper, MacPropertyInitializer macPropertyInitializer, EncryptionPropertyInitializer encryptionPropertyInitializer) {
        this.getEmployeeByIdUseCase = getEmployeeByIdUseCase;
        this.putEmployeeUseCase = putEmployeeUseCase;
        this.apiResponseMapper = apiResponseMapper;
        this.employeeCreatedMapper = employeeCreatedMapper;
        this.macPropertyInitializer = macPropertyInitializer;
        this.encryptionPropertyInitializer = encryptionPropertyInitializer;
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

    @PostMapping("/lamp")
    public ResponseEntity<?> lamp() {
        var com = new JpaAppUserEntity();
        com.setEmail("comp");
        com.setFirstName("first");
        com.setLastName("last");
        com.setPhone("32421");

        var employee = new JpaEmployeeEntity();
        employee.setUsername("employee");
        employee.setPassword("password");
        employee.setPositionName("position");
        employee.setUser(com);

        var x = this.macPropertyInitializer
                .addElement(employee)
                .addElement(employee.getUser())
                .processAll();

        var y = this.encryptionPropertyInitializer
                .addElement(employee)
                .addElement(employee.getUser())
                .processAll();

        return ResponseEntity.ok(com);
    }
}
