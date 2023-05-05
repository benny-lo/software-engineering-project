package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public final class ServerSettings {
    private static String hostName;
    private static int rmiPort;
    private static int socketPort;

    private ServerSettings() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableJdkUnsafe()
                .create();
        Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/configuration/server_config.json")));
        ServerSettingsFake settings = gson.fromJson(reader, ServerSettingsFake.class);
        hostName = settings.hostNameF;
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

    private static class ServerSettingsFake {
        private String hostNameF;
        private int rmiPortF;
        private int socketPortF;
    }
}
