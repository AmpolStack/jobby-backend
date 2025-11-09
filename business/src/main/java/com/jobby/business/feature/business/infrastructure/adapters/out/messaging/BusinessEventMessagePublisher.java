package com.jobby.business.feature.business.infrastructure.adapters.out.messaging;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.MessagingPublisher;
import org.springframework.stereotype.Component;

@Component("byMessaging")
public class BusinessEventMessagePublisher implements BusinessMessagePublisher {

    private final MessagingPublisher messagingPublisher;
    private final SchemaBusinessMapper schemaBusinessMapper;

    public BusinessEventMessagePublisher(MessagingPublisher messagingPublisher, SchemaBusinessMapper schemaBusinessMapper) {
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
