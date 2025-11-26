package com.jobby.business.feature.business.infrastructure.adapters.in.rest.controller;

import com.jobby.business.feature.business.application.services.BusinessCommandExecutor;
import com.jobby.business.feature.business.application.services.BusinessQueryExecutor;
import com.jobby.business.feature.business.application.useCase.commands.BusinessDefaultCreateCommand;
import com.jobby.business.feature.business.application.useCase.commands.BusinessDeleteByIdCommand;
import com.jobby.business.feature.business.application.useCase.commands.UpdateBusinessPicturesCommand;
import com.jobby.business.feature.business.application.useCase.commands.UpdateBusinessPropertiesCommand;
import com.jobby.business.feature.business.application.useCase.queries.BusinessQueryById;
import com.jobby.business.feature.business.application.useCase.queries.BusinessSetQueryByCityId;
import com.jobby.business.feature.business.application.useCase.queries.BusinessSetQueryByCountryId;
import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.operations.commands.BusinessCreateCommand;
import com.jobby.business.feature.business.domain.operations.commands.BusinessDeleteCommand;
import com.jobby.business.feature.business.domain.operations.commands.BusinessUpdateCommand;
import com.jobby.business.feature.business.domain.operations.queries.BusinessQuery;
import com.jobby.business.feature.business.domain.operations.queries.BusinessSetQuery;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.dto.UpdateBusinessPicturesDto;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.dto.UpdateBusinessPropertiesDto;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.mappers.CreateBusinessMapper;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessControllerTest {

    @Mock
    private CreateBusinessMapper createBusinessMapper;

    @Mock
    private ApiResponseMapper apiResponseMapper;

    @Mock
    private BusinessCommandExecutor businessCommandExecutor;

    @Mock
    private BusinessQueryExecutor businessQueryExecutor;

    @InjectMocks
    private BusinessController controller;

    private Business business;
    private ResponseEntity<?> response;

    @BeforeEach
    void setUp() {
        business = new Business();
        response = ResponseEntity.ok().build();
    }

    @Test
    void createShouldInvokeMapperAndCommandExecutor() {
        var dto = new CreateBusinessDto();
        Result<Business, Error> result = Result.success(business);

        when(createBusinessMapper.toDomain(dto)).thenReturn(business);
        when(businessCommandExecutor.execute(any(BusinessCreateCommand.class))).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);

        var actual = controller.create(dto);

        assertEquals(response, actual);
        ArgumentCaptor<BusinessCreateCommand> captor = ArgumentCaptor.forClass(BusinessCreateCommand.class);
        verify(businessCommandExecutor).execute(captor.capture());
        assertInstanceOf(BusinessDefaultCreateCommand.class, captor.getValue());
        verify(createBusinessMapper).toDomain(dto);
        verify(apiResponseMapper).map(result);
    }

    @Test
    void deleteByIdShouldExecuteDeleteCommand() {
        int id = 9;
        Result<Void, Error> result = Result.success(null);

        when(businessCommandExecutor.execute(any(BusinessDeleteCommand.class))).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);


        var actual = controller.deleteById(id);

        assertEquals(response, actual);
        ArgumentCaptor<BusinessDeleteCommand> captor = ArgumentCaptor.forClass(BusinessDeleteCommand.class);
        verify(businessCommandExecutor).execute(captor.capture());
        assertInstanceOf(BusinessDeleteByIdCommand.class, captor.getValue());
        verify(apiResponseMapper).map(result);
    }

    @Test
    void getByCityIdShouldExecuteCityQuery() {
        int cityId = 2;
        Result<Set<Business>, Error> result = Result.success(Set.of());

        when(businessQueryExecutor.execute(any(BusinessSetQuery.class))).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);


        var actual = controller.getByCityId(cityId);

        assertEquals(response, actual);
        ArgumentCaptor<BusinessSetQuery> captor = ArgumentCaptor.forClass(BusinessSetQuery.class);
        verify(businessQueryExecutor).execute(captor.capture());
        assertInstanceOf(BusinessSetQueryByCityId.class, captor.getValue());
        verify(apiResponseMapper).map(result);
    }

    @Test
    void getByCountryIdShouldExecuteCountryQuery() {
        int countryId = 3;
        Result<Set<Business>, Error> result = Result.success(Set.of());

        when(businessQueryExecutor.execute(any(BusinessSetQuery.class))).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);


        var actual = controller.getByCountryId(countryId);

        assertEquals(response, actual);
        ArgumentCaptor<BusinessSetQuery> captor = ArgumentCaptor.forClass(BusinessSetQuery.class);
        verify(businessQueryExecutor).execute(captor.capture());
        assertInstanceOf(BusinessSetQueryByCountryId.class, captor.getValue());
        verify(apiResponseMapper).map(result);
    }

    @Test
    void getByIdShouldExecuteSingleQuery() {
        int id = 5;
        Result<Business, Error> result = Result.success(business);

        when(businessQueryExecutor.execute(any(BusinessQuery.class))).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);


        var actual = controller.getById(id);

        assertEquals(response, actual);
        ArgumentCaptor<BusinessQuery> captor = ArgumentCaptor.forClass(BusinessQuery.class);
        verify(businessQueryExecutor).execute(captor.capture());
        assertInstanceOf(BusinessQueryById.class, captor.getValue());
        verify(apiResponseMapper).map(result);
    }

    @Test
    void updatePropertiesShouldExecuteUpdateCommand() {
        int id = 4;
        var dto = new UpdateBusinessPropertiesDto();
        dto.setName("new");
        dto.setDescription("desc");
        Result<Business, Error> result = Result.success(business);

        when(businessCommandExecutor.execute(any(BusinessUpdateCommand.class))).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);


        var actual = controller.updateProperties(id, dto);

        assertEquals(response, actual);
        ArgumentCaptor<BusinessUpdateCommand> captor = ArgumentCaptor.forClass(BusinessUpdateCommand.class);
        verify(businessCommandExecutor).execute(captor.capture());
        assertInstanceOf(UpdateBusinessPropertiesCommand.class, captor.getValue());
        verify(apiResponseMapper).map(result);
    }

    @Test
    void updatePicturesShouldExecutePicturesCommand() {
        int id = 1;
        var dto = new UpdateBusinessPicturesDto();
        dto.setBannerImageUrl("banner");
        dto.setProfileImageUrl("profile");
        Result<Business, Error> result = Result.success(business);

        when(businessCommandExecutor.execute(any(BusinessUpdateCommand.class))).thenReturn(result);
        doReturn(response).when(apiResponseMapper).map(result);


        var actual = controller.updatePics(id, dto);

        assertEquals(response, actual);
        ArgumentCaptor<BusinessUpdateCommand> captor = ArgumentCaptor.forClass(BusinessUpdateCommand.class);
        verify(businessCommandExecutor).execute(captor.capture());
        assertInstanceOf(UpdateBusinessPicturesCommand.class, captor.getValue());
        verify(apiResponseMapper).map(result);
    }
}

