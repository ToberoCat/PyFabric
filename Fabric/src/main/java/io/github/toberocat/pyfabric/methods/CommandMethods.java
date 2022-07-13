package io.github.toberocat.pyfabric.methods;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.toberocat.pyfabric.PyFabric;
import io.github.toberocat.pyfabric.command.Type;
import io.github.toberocat.pyfabric.command.JsonCommand;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Server;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandMethods extends ServerMethod {
    public CommandMethods(Server server) {
        super(server);
    }

    @Override
    public void register() {
        registerNoResponse("register_command", (objects) -> {
            if (objects.size() != 1) return;

            String rawJson = objects.get(0).toString();
            JsonCommand parsed = server.getGson().fromJson(rawJson, JsonCommand.class);

            PyFabric.COMMANDS_TO_REGISTER.add(createCommand("", parsed));
        });
    }

    private LiteralArgumentBuilder<FabricClientCommandSource> createCommand(@NotNull String previousNamespace,
                                                                            @NotNull JsonCommand command) {

        String nameSpace = previousNamespace + " " + command.getLiteral();
        LiteralArgumentBuilder<FabricClientCommandSource> builder = literal(command.getLiteral())
                .requires(x -> true)
                .executes(ctx -> {
                    server.sendPacket(new Package("on_command", nameSpace));
                    return 1;
                });

        command.getSubCommands().forEach((cmd) -> {
            if (cmd.getType() == Type.SUB) builder.then(createCommand(nameSpace, cmd));
            else builder
                    .then(argument(cmd.getLiteral(), cmd.getType().argument())
                    .executes((ctx) -> {
                        Object object = cmd.getType().getConsumer().accept(ctx, cmd.getLiteral());
                        server.sendPacket(new Package("on_command", nameSpace, server.getGson().toJson(object)));
                        return 1;
                    }));
        });
        return builder;
    }
}
