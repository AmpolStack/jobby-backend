package com.jobby.employee.infraestructure.adapters.in.rest.controllers;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.in.GetEmployeeByIdUseCase;
import com.jobby.employee.domain.ports.in.PutEmployeeUseCase;
import com.jobby.employee.infraestructure.adapters.in.rest.dto.created.EmployeeCreated;
import com.jobby.employee.infraestructure.adapters.in.rest.mappers.EmployeeCreatedMapper;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private GetEmployeeByIdUseCase getEmployeeByIdUseCase;

    @Mock
    private PutEmployeeUseCase putEmployeeUseCase;

    @Mock
    private ApiResponseMapper apiResponseMapper;

    @Mock
    private EmployeeCreatedMapper employeeCreatedMapper;

    @InjectMocks
    private EmployeeController controller;

    private Employee employee;
    private ResponseEntity<?> response;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        response = ResponseEntity.ok().build();
    }

    @Test
    void getByIdShouldReturnMappedResponse() {
        int id = 12;
        Result<Employee, Error> result = Result.success(employee);

        when(getEmployeeByIdUseCase.execute(id)).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);

        var actual = controller.getById(id);

        assertEquals(response, actual);
        verify(getEmployeeByIdUseCase).execute(id);
        verify(apiResponseMapper).map(result);
    }

    @Test
    void createShouldMapPayloadAndInvokeUseCase() {
        var dto = new EmployeeCreated();
        Result<Employee, Error> result = Result.success(employee);

        when(employeeCreatedMapper.toDomain(dto)).thenReturn(employee);
        when(putEmployeeUseCase.execute(employee)).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);


        var actual = controller.create(dto);

        assertEquals(response, actual);
        verify(employeeCreatedMapper).toDomain(dto);
        verify(putEmployeeUseCase).execute(employee);
        verify(apiResponseMapper).map(result);
    }
}

