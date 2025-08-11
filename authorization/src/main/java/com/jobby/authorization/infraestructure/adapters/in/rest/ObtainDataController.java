package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.application.useCases.ObtainEmployeesUseCase;
import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByCredentials;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/obtain")
public class ObtainDataController {
    private final ObtainEmployeesUseCase obtainEmployeesUseCase;
    private final AuthorizeEmployeeByCredentials authorizeEmployeeWithCredentialsUseCase;

    @Autowired
    public ObtainDataController(ObtainEmployeesUseCase obtainEmployeesUseCase, AuthorizeEmployeeByCredentials authorizeEmployeeWithCredentialsUseCase) {
        this.obtainEmployeesUseCase = obtainEmployeesUseCase;
        this.authorizeEmployeeWithCredentialsUseCase = authorizeEmployeeWithCredentialsUseCase;
    }

    @PostMapping("/auth")
    public Result<TokenRegistry, Error> ObtainEmployees(
            @RequestHeader String email,
            @RequestHeader String password) {
        return this.authorizeEmployeeWithCredentialsUseCase.execute(email, password);
    }


}

