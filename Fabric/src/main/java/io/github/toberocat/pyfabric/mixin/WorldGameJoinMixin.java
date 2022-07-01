package io.github.toberocat.pyfabric.mixin;

import io.github.toberocat.pyfabric.PyFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class WorldGameJoinMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "onGameJoin", at = @At(value = "NEW", target = "net/minecraft/client/world/ClientWorld"))
    private void onGameJoin(GameJoinS2CPacket packet) {
        new Thread(() -> {
            if (PyFabric.server.getWorld() == null) {
                PyFabric.server.setWorld(world);
            }
        }).start();
    }
}
