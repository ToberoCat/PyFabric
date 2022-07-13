package io.github.toberocat.pyfabric.methods.entity;

import io.github.toberocat.pyfabric.methods.ServerMethod;
import io.github.toberocat.pyfabric.server.Package;
import io.github.toberocat.pyfabric.server.Server;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InventoryMethods extends ServerMethod {

    private Iterator<ItemStack> armor;

    public InventoryMethods(Server server) {
        super(server);
    }

    @Override
    public void register() {
        registerEntityGetter("inventory_main_hand", uuid -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) return Collections.singletonList("Player didn't get generated yet");

            ItemStack stack = player.getInventory().getMainHandStack();
            if (stack == null) return Collections.singletonList("No item in main hand");

            return prepareSending(stack);
        });

        registerEntityGetter("inventory_off_hand", uuid -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) return Collections.singletonList("Player didn't get generated yet");

            ItemStack stack = player.getInventory().offHand.get(0);
            return prepareSending(stack);
        });

        addMethod("get_entity_inventory_stack_at", ((objects, reply) -> {
            if (objects.size() != 1) {
                reply.accept(new Package("__invalid_request", "get_entity_inventory_stack_at request requires a slot"));
                return;
            }

            if (objects.get(0) instanceof Double slot) {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player == null) {
                    reply.accept(new Package("__invalid_request", "Player didn't get generated yet"));
                    return;
                }
                ItemStack stack = player.getInventory().getStack((int) Math.floor(slot));
                if (stack == null) reply.accept(new Package("get_entity_inventory_stack_at", "NULL"));
                else reply.accept(new Package("get_entity_inventory_stack_at", prepareSending(stack)));
                return;
            }

            reply.accept(new Package("__uuid_wrong_type", "Requires uuid to be type of string"));
        }));

        registerEntityGetter("inventory_selected_slot", uuid -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) return Collections.singletonList("Player didn't get generated yet");

            return Collections.singletonList(player.getInventory().selectedSlot);
        });

        addMethod("get_entity_inventory_armor:iterator", (objects, reply) -> {
            if (objects.size() != 1) {
                reply.accept(new Package("__invalid_request", "The entity_inventory_armor:iterator request requires a uuid"));
                return;
            }

            if (objects.get(0) instanceof String stringUUID) { // Correct uuid type for parsing
                UUID uuid = UUID.fromString(stringUUID);

                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player == null) {
                    reply.accept(new Package("__invalid_request", "Player didn't get generated yet"));
                    return;
                }

                if (armor == null) armor = player.getArmorItems().iterator();

                if (armor.hasNext()) {
                    ItemStack stack = armor.next();
                    if (stack != null) {
                        reply.accept(new Package("get_entity_inventory_armor:iterator", prepareSending(stack)));
                    } else {
                        reply.accept(new Package("get_entity_inventory_armor:iterator", "NULL"));
                    }
                } else {
                    reply.accept(new Package("get_entity_inventory_armor:iterator", "CONSUMED"));
                }
                return;
            }

            reply.accept(new Package("__uuid_wrong_type", "Requires uuid to be type of string"));
        });

        registerNoResponse("get_entity_inventory_armor:reset", (objects -> armor = null));
    }

    private List<Object> prepareSending(@NotNull ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return List.of(Registry.ITEM.getId(stack.getItem()).toString(),
                stack.getCount(),
                stack.getMaxCount(),
                stack.getDamage(),
                stack.getMaxDamage(),
                stack.getName().toString(),
                stack.getRarity(),
                stack.getRepairCost(),
                stack.hasEnchantments(),
                stack.isDamageable(),
                stack.isFood(),
                nbt == null ? "{}" : nbt);
    }
}
