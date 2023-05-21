package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public final class ServerSettings {
    private static final String hostName;
    private static final int rmiPort;
    private static final int socketPort;

    static {
        MockServerSettings settings;
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableJdkUnsafe()
                .create();
        Reader reader = new InputStreamReader(Objects.requireNonNull(MockServerSettings.class.getResourceAsStream("/configuration/server_config.json")));
        settings = gson.fromJson(reader, MockServerSettings.class);
        hostName =  settings.hostNameF;
        rmiPort = settings.rmiPortF;
        socketPort = settings.socketPortF;
    }

    public static String getHostName() {
        return hostName;
    }

    public static int getRmiPort() {
        return rmiPort;
    }

    public static int getSocketPort() {
        return socketPort;
    }

    private static class MockServerSettings {
        private final String hostNameF;
        private final int rmiPortF;
        private final int socketPortF;

        private MockServerSettings() {
            this.hostNameF = null;
            this.rmiPortF = 0;
            this.socketPortF = 0;
        }
    }
}
