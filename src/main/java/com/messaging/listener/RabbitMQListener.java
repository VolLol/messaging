package com.messaging.listener;

import com.messaging.entity.PublishMessageEntity;
import com.messaging.repository.UserSessionsRepository;
import org.slf4j.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.LinkedList;

@EnableRabbit
@Component
public class RabbitMQListener {

    Logger log = LoggerFactory.getLogger(RabbitMQListener.class);

    private final UserSessionsRepository userSessionsRepository;

    public RabbitMQListener(UserSessionsRepository userSessionsRepository) {
        this.userSessionsRepository = userSessionsRepository;
    }

    @RabbitListener(queues = "#{wsQueue.name}")
    public void consumeMessageFromQueue(PublishMessageEntity message) throws IOException {
        log.info("Got message uuid = " + message.getUuid() + " payload = " + message.getPayload());
        LinkedList<WebSocketSession> webSocketSessions = userSessionsRepository.findSessionsByClientId(message.getUuid());
        if (!webSocketSessions.isEmpty()) {
            for (WebSocketSession session : webSocketSessions) {
                log.info("Send message uuid = " + message.getUuid() + " payload = " + message.getPayload() + " to sessionId = " + session.getId());
                session.sendMessage(new TextMessage(message.getPayload()));
            }
        }
    }
}
