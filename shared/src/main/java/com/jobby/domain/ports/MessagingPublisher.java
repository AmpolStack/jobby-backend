package com.jobby.domain.ports;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface MessagingPublisher {
   <T> Result<Void, Error> publish(String topicIdentifier, T data, int timeout);
   <T> void publishAsync(String topicIdentifier, T data);
}
