package sfs2x.client.examples;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.util.ConfigData;

public class SFS2XConnector {
    private final SmartFox sfs;
    private final String userName;

    public SFS2XConnector(String userName) {
        // Configure client connection settings
        ConfigData cfg = new ConfigData();
        cfg.setHost("localhost");
        cfg.setPort(9933);
        cfg.setZone("IndianGamer");
        cfg.setDebug(false);

        this.userName = userName;	

        // Set up event handlers
        sfs = new SmartFox();
        sfs.addEventListener(SFSEvent.CONNECTION, this::onConnection);
        sfs.addEventListener(SFSEvent.CONNECTION_LOST, this::onConnectionLost);
        sfs.addEventListener(SFSEvent.LOGIN, this::onLogin);
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, this::onLoginError);
        sfs.addEventListener(SFSEvent.ROOM_JOIN, this::onRoomJoin);
        sfs.addEventListener(SFSEvent.ADMIN_MESSAGE, baseEvent -> {
            System.out.println("Message from Server Admin: " + baseEvent.getArguments().get("message"));
        });

        sfs.connect(cfg);
    }

    // ----------------------------------------------------------------------
    // Event Handlers
    // ----------------------------------------------------------------------

    private void onConnection(BaseEvent evt) {
        boolean success = (boolean) evt.getArguments().get("success");

        if (success) {
            System.out.println("Connection success");
            sfs.send(new LoginRequest(userName));
        } else {
            System.out.println("Connection Failed. Is the server running?");
        }
    }

    private void onConnectionLost(BaseEvent evt) {
        System.out.println("-- Connection lost --");
    }

    private void onLogin(BaseEvent evt) {
        System.out.println("Logged in as: " + sfs.getMySelf().getName());

        sfs.send(new JoinRoomRequest("The Lobby"));
    }

    private void onLoginError(BaseEvent evt) {
        String message = (String) evt.getArguments().get("errorMessage");
        System.out.println("Login failed. Cause: " + message);
    }

    private void onRoomJoin(BaseEvent evt) {
        Room room = (Room) evt.getArguments().get("room");
        System.out.println("Joined Room: " + room.getName());
    }

    // ----------------------------------------------------------------------
}
