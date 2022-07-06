package io.github.toberocat.pyfabric.mixin;

import io.github.toberocat.pyfabric.PyFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class JoinWorldMixin {
    @Shadow
    private ClientWorld world;

    @Inject(at = @At("TAIL"), method = "onGameJoin(Lnet/minecraft/network/packet/s2c/play/GameJoinS2CPacket;)V")
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        PyFabric.server.join();
    }
}
