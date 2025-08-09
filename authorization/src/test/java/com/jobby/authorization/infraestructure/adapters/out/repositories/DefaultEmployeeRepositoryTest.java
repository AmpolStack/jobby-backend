package com.jobby.authorization.infraestructure.adapters.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.infraestructure.persistence.entities.MongoEmployeeEntity;
import com.jobby.authorization.infraestructure.persistence.mappers.MongoEmployeeEntityMapper;
import com.jobby.authorization.infraestructure.persistence.repositories.SpringDataMongoEmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import java.util.Optional;

import static com.jobby.authorization.infraestructure.TestAssertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultEmployeeRepositoryTest {

    private static final String VALID_EMAIL = "test@jobby.com";
    private static final String VALID_PASSWORD = "jobby";

    @Mock
    private MongoEmployeeEntityMapper mongoEmployeeEntityMapper;

    @Mock
    private SpringDataMongoEmployeeRepository springDataMongoEmployeeRepository;

    @InjectMocks
    private DefaultEmployeeRepository employeeRepository;

    @Test
    public void findByEmailAndPassword_WhenDatabaseFails() {
        // Arrange
        var expectedErrorMessage = "error in database, check";
        when(springDataMongoEmployeeRepository.findByEmailAndPassword(any(), any())).thenThrow(new DataAccessResourceFailureException(expectedErrorMessage));

        // Act
        var resp = this.employeeRepository.findByEmailAndPassword(VALID_EMAIL, VALID_PASSWORD);

        // Assert
        assertFailure(resp, ErrorType.ITN_EXTERNAL_SERVICE_FAILURE,"mongo.db.database", expectedErrorMessage);
        verify(springDataMongoEmployeeRepository, times(1)).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @Test
    public void findByEmailAndPassword_WhenTheEmployeeIsNotFound() {
        // Arrange
        when(springDataMongoEmployeeRepository.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        // Act
        var resp = this.employeeRepository.findByEmailAndPassword(VALID_EMAIL, VALID_PASSWORD);

        // Assert
        assertFailure(resp,   ErrorType.USER_NOT_FOUND, "employee", "No employee found with given credentials");
        verify(springDataMongoEmployeeRepository, times(1)).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @Test
    public void findByEmailAndPassword_WhenTheEmployeeIsFound() {
        // Arrange
        when(springDataMongoEmployeeRepository.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(new MongoEmployeeEntity()));
        when(mongoEmployeeEntityMapper.toDomain(any())).thenReturn(new Employee());

        // Act
        var resp = this.employeeRepository.findByEmailAndPassword(VALID_EMAIL, VALID_PASSWORD);

        // Assert
        assertSuccess(resp);
        verify(springDataMongoEmployeeRepository, times(1)).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, times(1)).toDomain(any());
    }

}
