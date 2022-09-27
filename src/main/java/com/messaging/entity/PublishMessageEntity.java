package com.messaging.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class PublishMessageEntity {
    @JsonProperty
    private String clientId;
    private String payload;

    public PublishMessageEntity() {
    }

    public String getUuid() {
        return clientId;
    }


    public String getPayload() {
        return payload;
    }

}
