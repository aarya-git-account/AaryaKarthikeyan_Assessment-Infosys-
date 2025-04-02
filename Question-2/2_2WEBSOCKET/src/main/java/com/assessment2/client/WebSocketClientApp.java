package com.assessment2.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClientApp extends WebSocketClient {

    public WebSocketClientApp(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connection established with server sending message 'Hello Server' ");
        send("Hello Server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message -> " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    public static void main(String[] args) throws Exception {
        WebSocketClientApp client = new WebSocketClientApp(new URI("ws://localhost:8080/msg"));
        client.connectBlocking();
        client.send("How are you?");
    }
}

