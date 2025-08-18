package com.jobby.authorization.infraestructure.adapters.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.domain.mobility.ErrorType;
import com.jobby.domain.result.Result;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
import com.jobby.authorization.infraestructure.persistence.entities.MongoEmployeeEntity;
import com.jobby.authorization.infraestructure.persistence.mappers.MongoEmployeeEntityMapper;
import com.jobby.authorization.infraestructure.persistence.repositories.SpringDataMongoEmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import java.util.Optional;
import static com.jobby.authorization.TestAssertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultEmployeeRepositoryTest {

    private static final String VALID_EMAIL = "test@jobby.com";
    private static final String VALID_PASSWORD = "jobby";
    private static final int VALID_ID = 1;

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
        assertFailure(resp, ErrorType.ITS_EXTERNAL_SERVICE_FAILURE,"mongo.db.database", expectedErrorMessage);
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
    public void findByEmailAndPassword_WhenTheEmailIsNull() {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "email")
                .build();

        // Act
        var result = this.employeeRepository.findByEmailAndPassword(null, VALID_PASSWORD);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
        verify(springDataMongoEmployeeRepository, never()).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void findByEmailAndPassword_WhenTheEmailIsBlank(String input) {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(input, "email")
                .build();

        // Act
        var result = this.employeeRepository.findByEmailAndPassword(input, VALID_PASSWORD);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
        verify(springDataMongoEmployeeRepository, never()).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }


    @Test
    public void findByEmailAndPassword_WhenThePasswordIsNull() {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "password")
                .build();

        // Act
        var result = this.employeeRepository.findByEmailAndPassword(VALID_EMAIL, null);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
        verify(springDataMongoEmployeeRepository, never()).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void findByEmailAndPassword_WhenThePasswordIsBlank(String input) {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(input, "password")
                .build();

        // Act
        var result = this.employeeRepository.findByEmailAndPassword(VALID_EMAIL, input);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
        verify(springDataMongoEmployeeRepository, never()).findByEmailAndPassword(any(), any());
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


    @Test
    public void findById_WhenDatabaseFails() {
        // Arrange
        var expectedErrorMessage = "error in database, check";
        when(springDataMongoEmployeeRepository.findById(VALID_ID)).thenThrow(new DataAccessResourceFailureException(expectedErrorMessage));

        // Act
        var resp = this.employeeRepository.findById(VALID_ID);

        // Assert
        assertFailure(resp, ErrorType.ITS_EXTERNAL_SERVICE_FAILURE,"mongo.db.database", expectedErrorMessage);
        verify(springDataMongoEmployeeRepository, times(1)).findById(any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @Test
    public void findById_WhenTheEmployeeIsNotFound() {
        // Arrange
        when(springDataMongoEmployeeRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        var resp = this.employeeRepository.findById(VALID_ID);

        // Assert
        assertFailure(resp, ErrorType.USER_NOT_FOUND, "employee", "No employee found with given id");
        verify(springDataMongoEmployeeRepository, times(1)).findById(anyInt());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @Test
    public void findById_WhenTheEmployeeIsFound() {
        // Arrange
        when(springDataMongoEmployeeRepository.findById(any())).thenReturn(Optional.of(new MongoEmployeeEntity()));
        when(mongoEmployeeEntityMapper.toDomain(any())).thenReturn(new Employee());

        // Act
        var resp = this.employeeRepository.findById(VALID_ID);

        // Assert
        assertSuccess(resp);
        verify(springDataMongoEmployeeRepository, times(1)).findById(any());
        verify(mongoEmployeeEntityMapper, times(1)).toDomain(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {-2,-200, -3, -1})
    public void findById_WhenTheIdIsSmallerThanZero(int input){
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalGreaterThan(input, 0, "employee-id")
                .build();

        // Act
        var resp = this.employeeRepository.findById(input);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }
}
