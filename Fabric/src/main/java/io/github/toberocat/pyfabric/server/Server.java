package io.github.toberocat.pyfabric.server;

import io.github.toberocat.pyfabric.PyFabric;
import io.github.toberocat.pyfabric.methods.CommandMethods;
import io.github.toberocat.pyfabric.methods.entity.EntityMethods;
import io.github.toberocat.pyfabric.methods.entity.InventoryMethods;
import io.github.toberocat.pyfabric.methods.entity.PlayerMethods;
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
        new InventoryMethods(this).register();
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
        logger.info("Disconnect");
        if (joinedWorld) sendPacket(new Package("quit"));
        this.joinedWorld = false;

        PyFabric.COMMANDS_TO_REGISTER.clear();
    }
}
