package io.github.toberocat.pyfabric.methods.entity;

import io.github.toberocat.pyfabric.exceptions.ClientException;
import io.github.toberocat.pyfabric.methods.ServerMethod;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Server;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class EntityMethods extends ServerMethod {
    public EntityMethods(Server server) {
        super(server);
    }

    @Override
    public void register() {
        registerEntityGetter("location", uuid -> {
            List<Object> objects = new ArrayList<>();

            try {
                Entity entity = checkEntity(uuid);

                objects.add(entity.getX());
                objects.add(entity.getY());
                objects.add(entity.getZ());
                objects.add(entity.getYaw());
                objects.add(entity.getPitch());
            } catch (ClientException e) {
                objects.add(e.getMessage());
            }
            return objects;
        });

        addMethod("get_client_player_world", (objects, reply) -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) {
                reply.accept(new Package("__invalid_world", "Client world is null"));
                return;
            }

            reply.accept(new Package("get_client_player_world", world.getRegistryKey().getValue().toString()));
        });
    }


    private Entity checkEntity(UUID uuid) throws ClientException {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) throw new ClientException("Client world is null");

        Entity entity = StreamSupport.stream(world.getEntities().spliterator(), false)
                .filter(x -> x.getUuid().equals(uuid))
                .findFirst().orElse(null);
        if (entity == null) throw new ClientException("Couldn't find entity in world with given uuid");
        return entity;
    }
}
