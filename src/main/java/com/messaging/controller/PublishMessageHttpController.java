package com.messaging.controller;


import com.messaging.entity.PublishMessageEntity;
import org.slf4j.*;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublishMessageHttpController {

    Logger log = LoggerFactory.getLogger(PublishMessageHttpController.class);

    @Autowired
    private final RabbitTemplate template;

    @Autowired
    private FanoutExchange fanoutExchange;

    PublishMessageHttpController(RabbitTemplate template) {
        this.template = template;
    }

    @PostMapping("/message/publish")
    public String publish(@RequestBody PublishMessageEntity message) {
        log.info("Publish message = " + message.getPayload() + " to exchange ws-messages for clientId = " + message.getUuid());
        template.convertAndSend(fanoutExchange.getName(), "", message);
        return "Messages was published";
    }

}
