package com.messaging.handler;

import com.messaging.repository.UserSessionsRepository;
import org.slf4j.*;
import org.springframework.web.socket.*;

public class WebsocketMessageHandler implements WebSocketHandler {

    Logger log = LoggerFactory.getLogger(WebsocketMessageHandler.class);


    private final UserSessionsRepository userSessionsRepository;

    public WebsocketMessageHandler(UserSessionsRepository userSessionsRepository) {
        this.userSessionsRepository = userSessionsRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Connection was established, sessionId = " + session.getId()));
        log.info("Add sessionId = " + session.getId() + " to userSessionRepository");
        userSessionsRepository.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connect sessionId = " + session.getId() + " closed");
        userSessionsRepository.removeSessionBySessionId(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Handle message in sessionId = " + session.getId() + " payload = " + message.getPayload());
        log.info("Join sessionId = " + session.getId() + " and clientId = " + message.getPayload() + " from message");
        userSessionsRepository.joinSessionAndClientId(session.getId(), message.getPayload().toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }
}
