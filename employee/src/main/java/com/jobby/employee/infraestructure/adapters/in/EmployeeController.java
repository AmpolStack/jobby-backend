package com.jobby.employee.infraestructure.adapters.in;

import com.jobby.employee.domain.ports.in.GetEmployeeByIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final GetEmployeeByIdUseCase getEmployeeByIdUseCase;

    public EmployeeController(GetEmployeeByIdUseCase getEmployeeByIdUseCase) {
        this.getEmployeeByIdUseCase = getEmployeeByIdUseCase;
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getById(@RequestParam int id) {
        var resp = this.getEmployeeByIdUseCase
                .execute(id);
        return ResponseEntity.ok(resp);
    }
}
