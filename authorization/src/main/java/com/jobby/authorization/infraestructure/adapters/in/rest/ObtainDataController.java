package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.application.ObtainEmployeesUseCase;
import com.jobby.authorization.domain.model.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/obtain")
public class ObtainDataController {
    private final ObtainEmployeesUseCase obtainEmployeesUseCase;

    @Autowired
    public ObtainDataController(ObtainEmployeesUseCase obtainEmployeesUseCase) {
        this.obtainEmployeesUseCase = obtainEmployeesUseCase;
    }

    @GetMapping("/employee")
    public List<EmployeeEntity> ObtainEmployees() {
        var result = this.obtainEmployeesUseCase.getEmployees();
        return result;
    }
}

