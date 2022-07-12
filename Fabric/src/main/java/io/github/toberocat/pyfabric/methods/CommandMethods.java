package io.github.toberocat.pyfabric.methods;

import io.github.toberocat.pyfabric.PyFabric;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Server;
import org.jetbrains.annotations.NotNull;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandMethods extends ServerMethod {
    public CommandMethods(Server server) {
        super(server);
    }

    @Override
    public void register() {
        registerNoResponse("register_command", (objects) -> {
            if (objects.size() != 1) return;

            String name = objects.get(0).toString();
            createCommand(name);
        });
    }

    private void createCommand(@NotNull String name) {
        logger.info("Creating command: " + name);
        PyFabric.DISPATCHER.register(literal(name)
                .executes(ctx -> {
                    server.sendPacket(new Package("on_command", name));
                    return 1;
                }));
    }
}
