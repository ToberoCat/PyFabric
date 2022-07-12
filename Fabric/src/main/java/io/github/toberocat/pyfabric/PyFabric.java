package io.github.toberocat.pyfabric;

import com.mojang.brigadier.CommandDispatcher;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Server;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PyFabric implements ModInitializer {

    public static final String ID = "pyfabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static Server server;
    public static CommandDispatcher<FabricClientCommandSource> DISPATCHER;

    @Override
    public void onInitialize() {
        server = new Server(1337, LOGGER);
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> DISPATCHER = dispatcher));
    }

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
}
