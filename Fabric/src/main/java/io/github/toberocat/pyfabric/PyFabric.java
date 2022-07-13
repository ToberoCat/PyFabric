package io.github.toberocat.pyfabric;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.toberocat.pyfabric.server.Server;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class PyFabric implements ModInitializer {

    public static final String ID = "pyfabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static Server server;
    public static LinkedList<LiteralArgumentBuilder<FabricClientCommandSource>> COMMANDS_TO_REGISTER = new LinkedList<>();

    public static void runLater(long delay, Runnable run) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run.run();
        }).start();
    }

    @Override
    public void onInitialize() {
        server = new Server(1337, LOGGER);
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            COMMANDS_TO_REGISTER.forEach(dispatcher::register);
        }));
    }
}
