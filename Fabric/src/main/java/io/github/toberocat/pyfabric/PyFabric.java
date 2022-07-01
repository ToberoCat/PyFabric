package io.github.toberocat.pyfabric;

import io.github.toberocat.pyfabric.server.Server;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PyFabric implements ModInitializer {

    public static final String ID = "pyfabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static Server server;

    @Override
    public void onInitialize() {
        server = new Server(1337, LOGGER);
    }
}
