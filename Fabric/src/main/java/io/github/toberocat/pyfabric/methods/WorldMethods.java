package io.github.toberocat.pyfabric.methods;

import io.github.toberocat.pyfabric.exceptions.ClientException;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Server;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class WorldMethods extends ServerMethod {
    private Iterator<Entity> loaded_entities;
    private Iterator<AbstractClientPlayerEntity> loaded_players;

    public WorldMethods(Server server) {
        super(server);
    }

    @Override
    public void register() {
        addMethod("get_all_entities_in_world_single", ((objects, reply) -> {
            try {
                ClientWorld world = getWorld();
                if (loaded_entities == null)
                    loaded_entities = world.getEntities().iterator();

                if (loaded_entities.hasNext()) {
                    Entity entity = loaded_entities.next();
                    if (entity == null) {
                        reply.accept(new Package("get_all_entities_in_world_single", ""));
                    } else {
                        reply.accept(new Package("get_all_entities_in_world_single", entity.getUuid()));
                    }
                } else {
                    reply.accept(new Package("get_all_entities_in_world_single", "CONSUMED"));
                }
            } catch (ClientException e) {
                reply.accept(new Package("__invalid", e.getMessage()));
            }
        }));

        addMethod("get_all_players_in_world_single", ((objects, reply) -> {
            try {
                ClientWorld world = getWorld();
                if (loaded_players == null)
                    loaded_players = world.getPlayers().iterator();

                if (loaded_players.hasNext()) {
                    AbstractClientPlayerEntity player = loaded_players.next();
                    if (player == null) {
                        reply.accept(new Package("get_all_players_in_world_single", ""));
                    } else {
                        reply.accept(new Package("get_all_players_in_world_single", player.getUuid()));
                    }
                } else {
                    reply.accept(new Package("get_all_players_in_world_single", "CONSUMED"));
                }
            } catch (ClientException e) {
                reply.accept(new Package("__invalid", e.getMessage()));
            }
        }));

        addMethod("get_block_at", ((objects, reply) -> {
            if (objects.size() != 3) {
                reply.accept(new Package("__invalid_args",
                        "get_block_at requires 3 argument: x, y, z. All must be integers"));
                return;
            }

            Object rawX = objects.get(0);
            Object rawY = objects.get(1);
            Object rawZ = objects.get(2);

            if (rawX instanceof Double x && rawY instanceof Double y && rawZ instanceof Double z) {
                try {
                    ClientWorld world = getWorld();
                    Identifier id = Registry.BLOCK.getId(world.getBlockState(new BlockPos(x, y, z)).getBlock());
                    if (id == null) {
                        reply.accept(new Package("__invalid_block", "block at " + x + ", " + y + ", "
                                + z + " wasn't to be found"));
                        return;
                    }

                    logger.info(id.toString());
                    reply.accept(new Package("get_block_at", id.toString()));
                } catch (ClientException e) {
                    reply.accept(new Package("__invalid", e.getMessage()));
                }
            } else {
                reply.accept(new Package("__invalid_args",
                        "get_block_at requires 3 argument: x, y, z. All must be integers"));
            }
        }));

        registerNoResponse("spawn_particle", (objects -> {
            try {
                ClientWorld world = getWorld();
            } catch (ClientException ignored) {
            }

        }));

        registerNoResponse("reset_entities", (objects -> {
            loaded_entities = null;
        }));

        registerNoResponse("reset_players", (objects -> {
            loaded_players = null;
        }));
    }

    private @NotNull ClientWorld getWorld() throws ClientException {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            throw new ClientException("Client world is null");
        }

        return world;
    }
}
