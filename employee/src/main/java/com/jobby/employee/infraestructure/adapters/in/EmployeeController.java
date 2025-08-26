package com.jobby.employee.infraestructure.adapters.in;

import com.jobby.employee.domain.ports.in.GetEmployeeByIdUseCase;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final GetEmployeeByIdUseCase getEmployeeByIdUseCase;
    private final ApiResponseMapper apiResponseMapper;

    public EmployeeController(GetEmployeeByIdUseCase getEmployeeByIdUseCase, ApiResponseMapper apiResponseMapper) {
        this.getEmployeeByIdUseCase = getEmployeeByIdUseCase;
        this.apiResponseMapper = apiResponseMapper;
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getById(@RequestHeader("X-User-Id") int id) {
        var resp = this.getEmployeeByIdUseCase
                .execute(id);

        return this.apiResponseMapper.map(resp);
    }
}
