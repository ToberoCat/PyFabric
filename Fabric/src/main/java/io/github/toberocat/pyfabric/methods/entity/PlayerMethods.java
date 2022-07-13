package io.github.toberocat.pyfabric.methods.entity;

import io.github.toberocat.pyfabric.methods.ServerMethod;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Server;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerMethods extends ServerMethod {

    public PlayerMethods(Server server) {
        super(server);
    }

    @Override
    public void register() {
        addMethod("get_client_player_uuid", (objects, reply) -> {
            if (objects.size() > 0) {
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

        registerEntityGetter("block_looking_at", uuid -> {
            List<Object> objects = new LinkedList<>();

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) {
                objects.add("The player didn't get generated yet");
                return objects;
            }

            double rayLength = 100d;
            Vec3d playerRotation = player.getRotationVector();
            Vec3d rayPath = playerRotation.multiply(rayLength);

            Vec3d from = player.getEyePos();
            Vec3d to = from.add(rayPath);

            RaycastContext rayCtx = new RaycastContext(from, to, RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.ANY,
                    player);
            BlockHitResult rayHit = player.getWorld().raycast(rayCtx);

            if (rayHit.getType() == HitResult.Type.MISS) objects.add("miss");
            else objects.add(Registry.BLOCK.getId(player.getWorld().getBlockState(rayHit.getBlockPos()).getBlock()));

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
}
