package io.github.toberocat.pyfabric.server;

import io.github.toberocat.pyfabric.methods.CommandMethods;
import io.github.toberocat.pyfabric.methods.EntityMethods;
import io.github.toberocat.pyfabric.methods.PlayerMethods;
import io.github.toberocat.pyfabric.methods.WorldMethods;
import org.slf4j.Logger;

public class Server extends AbstractServer {

    private boolean joinedWorld = false;

    public Server(int port, Logger logger) {
        super(port, logger);

        registerEntityGetters();

        createServer();
    }

    @Override
    protected void pythonJoined() {
        if (!joinedWorld) return;

        logger.info("Resending join world");
        sendPacket(new Package("joined"));
    }

    private void registerEntityGetters() {
        new PlayerMethods(this).register();
        new EntityMethods(this).register();
        new WorldMethods(this).register();
        new CommandMethods(this).register();
    }

    public boolean alreadyJoined() {
        return joinedWorld;
    }

    public void join() {
        this.joinedWorld = true;
        logger.info("Sent join event");
        sendPacket(new Package("joined"));
    }

    public void disconnectFromServer() {
        this.joinedWorld = false;
        sendPacket(new Package("quit"));
    }
}
