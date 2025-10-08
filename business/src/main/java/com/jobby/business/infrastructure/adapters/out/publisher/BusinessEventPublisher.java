package com.jobby.business.infrastructure.adapters.out.publisher;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.BusinessPublisher;
import com.jobby.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.MessagingPublisher;
import org.springframework.stereotype.Component;

@Component
public class BusinessEventPublisher implements BusinessPublisher {

    private final MessagingPublisher messagingPublisher;
    private final SchemaBusinessMapper schemaBusinessMapper;

    public BusinessEventPublisher(MessagingPublisher messagingPublisher, SchemaBusinessMapper schemaBusinessMapper) {
        this.messagingPublisher = messagingPublisher;
        this.schemaBusinessMapper = schemaBusinessMapper;
    }

    @Override
    public Result<Void, Error> sendBusiness(Business business) {
        var schemaBusiness = this.schemaBusinessMapper.toSchema(business);
        this.messagingPublisher.publishAsync("com.jobby.messaging.event.business.created.v1", schemaBusiness);
        return Result.success(null);
    }
}
