package io.github.toberocat.pyfabric.server;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

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
        sendEvent(new Package("joined"));
    }

    private void registerEntityGetters() {
        registerEntityGetter("location", uuid -> {
            List<Object> objects = new ArrayList<>();

            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) {
                objects.add("Client world is null");
                return objects;
            }

            Entity entity = StreamSupport.stream(world.getEntities().spliterator(), false)
                    .filter(x -> x.getUuid().equals(uuid))
                    .findFirst().orElse(null);
            if (entity == null) {
                objects.add("Couldn't find entity in world with given uuid");
                return objects;
            }

            objects.add(entity.getX());
            objects.add(entity.getY());
            objects.add(entity.getZ());
            objects.add(entity.getYaw());
            objects.add(entity.getPitch());
            return objects;
        });

        registerEntityGetter("dimension", uuid -> {
            List<Object> objects = new ArrayList<>();

            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) {
                objects.add("Client world is null");
                return objects;
            }

            Entity entity = StreamSupport.stream(world.getEntities().spliterator(), false)
                    .filter(x -> x.getUuid().equals(uuid))
                    .findFirst().orElse(null);
            if (entity == null) {
                objects.add("Couldn't find entity in world with given uuid");
                return objects;
            }

            objects.add(entity.getWorld().getRegistryKey().getValue().toString());
            return objects;
        });

        registerPlayerMethods();
    }

    private void register

    private void registerPlayerMethods() {
        addMethod("get_client_player_uuid", (objects, reply) -> {
            if (objects.size() == 1) {
                reply.accept(new Package("__invalid_request", "get_client_player_uuid doesn't requires a data arg"));
                return;
            }

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) {
                reply.accept(new Package("__cant_resolve", "The player didn't get generated yet"));
                return;
            }
            UUID uuid = player.getUuid();
            if (uuid == null) {
                reply.accept(new Package("__cant_resolve", "The player didn't hold any uuid"));
                return;
            }
            reply.accept(new Package("get_client_player_uuid", uuid));
        });

        registerEntityGetter("inventory", (uuid) -> {
            List<Object> objects = new LinkedList<>();
            //MinecraftClient.getInstance().player.
            return objects;
        });

        /* Notifications */
        registerNoResponse("client_chat", (objects) -> {
            logger.info("Got request");
            if (objects.size() <= 0) return;
            logger.info("Got msg");

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) return;
            logger.info("Got player");

            String message = objects.get(0).toString();
            logger.info("Sending...");
            player.sendMessage(Text.literal(message));
        });

        registerNoResponse("client_actionbar", (objects) -> {
            if (objects.size() <= 0) return;

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) return;

            String message = objects.get(0).toString();
            player.sendMessage(Text.literal(message), true);
        });
    }

    public void registerNoResponse(@NotNull String id, Consumer<List<Object>> call) {
        addMethod("no_response_" + id, (objects, ignored) -> call.accept(objects));
    }

    public void registerEntityGetter(@NotNull String id, Request<UUID, List<Object>> request) {
        addMethod("get_entity_" + id, (objects, reply) -> {
            if (objects.size() != 1) {
                reply.accept(new Package("__invalid_request", "The entity_" + id + " request requires a uuid"));
                return;
            }
            logger.info(id);

            if (objects.get(0) instanceof String stringUUID) { // Correct uuid type for parsing
                UUID uuid = UUID.fromString(stringUUID);
                reply.accept(new Package("get_entity_" + id, request.run(uuid)));
                return;
            }

            reply.accept(new Package("__uuid_wrong_type", "Requires uuid to be type of string"));
        });
    }

    public boolean alreadyJoined() {
        return joinedWorld;
    }

    public void join() {
        this.joinedWorld = true;
        logger.info("Sent join event");
        sendEvent(new Package("joined"));
    }

    public void disconnectFromServer() {
        this.joinedWorld = false;
        sendEvent(new Package("quit"));
    }
}
