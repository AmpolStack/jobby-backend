package com.jobby.employee.infraestructure.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.model.EmployeeStatus;
import com.jobby.employee.domain.model.Sectional;
import com.jobby.employee.infraestructure.dto.EmployeeCreated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
        uses = { AddressCreatedMapper.class, UserCreatedMapper.class })
public interface EmployeeCreatedMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source = "sectionalId", target = "sectional", qualifiedByName = "sectionalFromId")
    @Mapping(source = "employeeStatusId", target = "status", qualifiedByName = "employeeStatusFromId")
    Employee toDomain(EmployeeCreated employeeCreated);

    @Named("sectionalFromId")
    default Sectional sectionalFromId(int id){
        var sectional = new Sectional();
        sectional.setId(id);
        return sectional;
    }

    @Named("employeeStatusFromId")
    default EmployeeStatus employeeStatusFromId(int id){
        var employeeStatus = new EmployeeStatus();
        employeeStatus.setId(id);
        return employeeStatus;
    }
}
