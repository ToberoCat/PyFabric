package io.github.toberocat.pyfabric.methods;

import io.github.toberocat.pyfabric.server.Server;

public abstract class ServerMethod {
    protected Server server;

    public ServerMethod(Server server) {
        this.server = server;
    }

    public abstract void register();
}
