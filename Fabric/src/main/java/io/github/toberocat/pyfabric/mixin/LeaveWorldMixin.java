package io.github.toberocat.pyfabric.mixin;

import io.github.toberocat.pyfabric.PyFabric;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class LeaveWorldMixin {

    @Inject(at = @At("TAIL"), method = "onDisconnect(Lnet/minecraft/network/packet/s2c/play/DisconnectS2CPacket;)V")
    private void onDisconnect(DisconnectS2CPacket packet, CallbackInfo ci) {
        PyFabric.server.disconnectFromServer();
    }

    @Inject(at = @At("TAIL"), method = "onDisconnected(Lnet/minecraft/text/Text;)V")
    private void onDisconnected(Text reason, CallbackInfo ci) {
        PyFabric.server.disconnectFromServer();

    }
}
