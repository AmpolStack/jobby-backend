package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.application.useCases.ObtainEmployeesUseCase;
import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeUseCase;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/obtain")
public class ObtainDataController {
    private final ObtainEmployeesUseCase obtainEmployeesUseCase;
    private final AuthorizeEmployeeUseCase authorizeEmployeeUseCase;

    @Autowired
    public ObtainDataController(ObtainEmployeesUseCase obtainEmployeesUseCase, AuthorizeEmployeeUseCase authorizeEmployeeUseCase) {
        this.obtainEmployeesUseCase = obtainEmployeesUseCase;
        this.authorizeEmployeeUseCase = authorizeEmployeeUseCase;
    }

    @PostMapping("/auth")
    public Result<TokenRegistry, Error> ObtainEmployees(
            @RequestHeader String email,
            @RequestHeader String password) {
        return this.authorizeEmployeeUseCase.byCredentials(email, password);
    }


}

