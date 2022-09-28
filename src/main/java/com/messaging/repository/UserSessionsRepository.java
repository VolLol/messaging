package com.messaging.repository;


import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionsRepository {

    Logger log = LoggerFactory.getLogger(UserSessionsRepository.class);

    private final ConcurrentHashMap<String, WebSocketSession> sessionIdWebSocketMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, LinkedList<String>> clientIdToSessionId = new ConcurrentHashMap<>();

    public UserSessionsRepository() {

    }

    public void addSession(WebSocketSession session) {
        sessionIdWebSocketMap.put(session.getId(), session);
    }

    public void joinSessionAndClientId(String sessionId, String clientId) {
        LinkedList<String> sessions;
        if (clientIdToSessionId.containsKey(clientId)) {
            sessions = clientIdToSessionId.get(clientId);
        } else {
            sessions = new LinkedList<>();
        }
        sessions.add(sessionId);
        clientIdToSessionId.put(clientId, sessions);
    }

    public LinkedList<WebSocketSession> findSessionsByClientId(String clientId) {
        LinkedList<WebSocketSession> webSocketSessions = new LinkedList<>();
        if (clientIdToSessionId.containsKey(clientId)) {
            LinkedList<String> sessions = clientIdToSessionId.get(clientId);
            log.info("Find client = " + clientId + " in session list = " + sessions.toString());
            for (String sessionId : sessions) {
                if (sessionIdWebSocketMap.containsKey(sessionId)) {
                    webSocketSessions.push(sessionIdWebSocketMap.get(sessionId));
                }
            }
        }
        return webSocketSessions;
    }

    public void removeSessionBySessionId(String sessionId) {
        log.info("Remove sessionId = " + sessionId + " from userSessionsRepository");
        sessionIdWebSocketMap.remove(sessionId);
        for (LinkedList<String> sessionIdList : clientIdToSessionId.values()) {
            sessionIdList.remove(sessionId);
        }
    }
}
