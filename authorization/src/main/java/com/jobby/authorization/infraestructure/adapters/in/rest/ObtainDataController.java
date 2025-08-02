package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.application.useCases.ObtainEmployeesUseCase;
import com.jobby.authorization.domain.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/obtain")
public class ObtainDataController {
    private final ObtainEmployeesUseCase obtainEmployeesUseCase;

    @Autowired
    public ObtainDataController(ObtainEmployeesUseCase obtainEmployeesUseCase) {
        this.obtainEmployeesUseCase = obtainEmployeesUseCase;
    }

    @PostMapping("/employee")
    public Employee ObtainEmployees(
            @RequestHeader String email,
            @RequestHeader String password) {
        return this.obtainEmployeesUseCase.getEmployee(email, password);
    }
}

