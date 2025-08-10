package com.jobby.authorization.infraestructure.adapters.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.shared.result.ErrorType;
import com.jobby.authorization.infraestructure.persistence.entities.MongoEmployeeEntity;
import com.jobby.authorization.infraestructure.persistence.mappers.MongoEmployeeEntityMapper;
import com.jobby.authorization.infraestructure.persistence.repositories.SpringDataMongoEmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import java.util.Optional;
import java.util.stream.Stream;
import static com.jobby.authorization.infraestructure.TestAssertions.*;
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
    public void findByEmailAndPassword_WhenTheEmailIsNull() {
        // Act
        var resp = this.employeeRepository.findByEmailAndPassword(null, VALID_PASSWORD);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "email", "is null or blank");
        verify(springDataMongoEmployeeRepository, never()).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo5")
    public void findByEmailAndPassword_WhenTheEmailIsBlank(String input) {
        // Act
        var resp = this.employeeRepository.findByEmailAndPassword(input, VALID_PASSWORD);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "email", "is null or blank");
        verify(springDataMongoEmployeeRepository, never()).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }


    @Test
    public void findByEmailAndPassword_WhenThePasswordIsNull() {
        // Act
        var resp = this.employeeRepository.findByEmailAndPassword(VALID_EMAIL, null);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "password", "is null or blank");
        verify(springDataMongoEmployeeRepository, never()).findByEmailAndPassword(any(), any());
        verify(mongoEmployeeEntityMapper, never()).toDomain(any());
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo5")
    public void findByEmailAndPassword_WhenThePasswordIsBlank(String input) {
        // Act
        var resp = this.employeeRepository.findByEmailAndPassword(VALID_EMAIL, input);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "password", "is null or blank");
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
        assertFailure(resp, ErrorType.ITN_EXTERNAL_SERVICE_FAILURE,"mongo.db.database", expectedErrorMessage);
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
        assertFailure(resp,   ErrorType.USER_NOT_FOUND, "employee", "No employee found with given id");
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

    private static Stream<String> blankStringsUpTo5() {
        return Stream.of("", " ", "  ", "   ", "     ");
    }


}
