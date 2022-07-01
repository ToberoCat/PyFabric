package io.github.toberocat.pyfabric.server;

import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;

public class Server extends AbstractServer {

    private ClientWorld world = null;

    public Server(int port, Logger logger) {
        super(port, logger);

        registerEntityGetters();

        createServer();
    }

    @Override
    protected void pythonJoined() {
        System.out.println("hggh");
        if (world == null) return;

        logger.info("Resending join world");
        sendEvent(new Package("join_world"));
    }

    private void registerEntityGetters() {
        registerEntityGetter("x", (uuid -> {
            List<Object> objects = new ArrayList<>();
            objects.add(1);
            return objects;
        }));
        registerEntityGetter("y", (uuid -> {
            List<Object> objects = new ArrayList<>();
            objects.add(2);
            return objects;
        }));
        registerEntityGetter("z", (uuid -> {
            List<Object> objects = new ArrayList<>();
            objects.add(3);
            return objects;
        }));
        registerEntityGetter("yaw", (uuid -> {
            List<Object> objects = new ArrayList<>();
            objects.add(4);
            return objects;
        }));
        registerEntityGetter("pitch", (uuid -> {
            List<Object> objects = new ArrayList<>();
            objects.add(5);
            return objects;
        }));
        registerEntityGetter("world", (uuid -> {
            List<Object> objects = new ArrayList<>();
            objects.add("worldname");
            return objects;
        }));

        addMethod("get_client_player_uuid", (objects, reply) -> {
            if (objects.size() == 1) {
                reply.accept(new Package("invalid_request", "get_client_player_uuid doesn't requires a data arg"));
                return;
            }

            reply.accept(new Package("get_client_player_uuid", UUID.randomUUID().toString()));
        });
    }

    public void registerNoResponse(@NotNull String id, Consumer<List<Object>> call) {
        addMethod("no_response_" + id, (objects, ignored) -> call.accept(objects));
    }

    public void registerEntityGetter(@NotNull String id, Request<UUID, List<Object>> request) {
        addMethod("get_entity_" + id, (objects, reply) -> {
            if (objects.size() != 1) {
                reply.accept(new Package("invalid_request", "The entity_" + id + " request requires a uuid"));
                return;
            }

            if (objects.get(0) instanceof String stringUUID) { // Correct uuid type for parsing
                UUID uuid = UUID.fromString(stringUUID);
                reply.accept(new Package("get_entity_" + id, request.run(uuid)));
                return;
            }

            reply.accept(new Package("uuid_wrong_type", "Requires uuid to be type of string"));
        });
    }

    public ClientWorld getWorld() {
        return world;
    }

    public void setWorld(ClientWorld world) {
        logger.info(String.valueOf(world == null));
        this.world = world;
        sendEvent(new Package("join_world"));
    }
}
