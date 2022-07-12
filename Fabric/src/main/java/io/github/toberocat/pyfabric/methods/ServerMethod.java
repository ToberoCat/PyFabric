package io.github.toberocat.pyfabric.methods;

import io.github.toberocat.pyfabric.PyFabric;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Request;
import io.github.toberocat.pyfabric.server.Server;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ServerMethod {
    protected final Server server;
    protected final Logger logger;

    public ServerMethod(Server server) {
        this.server = server;
        this.logger = PyFabric.LOGGER;
    }

    public abstract void register();

    protected void registerNoResponse(@NotNull String id, Consumer<List<Object>> call) {
        server.addMethod("no_response_" + id, (objects, ignored) -> call.accept(objects));
    }

    protected void registerEntityGetter(@NotNull String id, Request<UUID, List<Object>> request) {
        server.addMethod("get_entity_" + id, (objects, reply) -> {
            if (objects.size() != 1) {
                reply.accept(new Package("__invalid_request", "The entity_" + id + " request requires a uuid"));
                return;
            }

            if (objects.get(0) instanceof String stringUUID) { // Correct uuid type for parsing
                UUID uuid = UUID.fromString(stringUUID);
                reply.accept(new Package("get_entity_" + id, request.run(uuid)));
                return;
            }

            reply.accept(new Package("__uuid_wrong_type", "Requires uuid to be type of string"));
        });
    }

    protected void addMethod(@NotNull String id, BiConsumer<List<Object>, Consumer<Package>> action) {
        server.addMethod(id, action);
    }
}
