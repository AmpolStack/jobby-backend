package com.jobby.employee.infraestructure.adapters.in;

import com.jobby.employee.domain.ports.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getById(@RequestParam int id) {
        var resp = this.employeeRepository
                .getEmployeeById(id);
        return ResponseEntity.ok(resp);
    }
}
