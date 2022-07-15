import time

from src.PyFabric.Networking.Client import Client


class ItemStack:
    def __init__(self,
                 registry: str,
                 count: int,
                 max_count: int,
                 damage: int,
                 max_damage: int,
                 name: str,
                 rarity: str,
                 repair_cost: int,
                 is_enchanted: bool,
                 is_damageable: bool,
                 is_food: bool,
                 nbt: str):
        self.registry = registry

        self.count = count
        self.max_count = max_count

        self.damage = damage
        self.max_damage = max_damage

        self.name = name
        self.rarity = rarity
        self.repair_cost = repair_cost
        self.is_enchanted = is_enchanted
        self.is_damageable = is_damageable
        self.is_food = is_food
        self.nbt = nbt

    def __str__(self):
        return "{item:{registry:%s,count:%d,max_count:%d,damage:%d," \
               "max_damage%d,name:%s,rarity:%s,repair_cost:%d," \
               "is_enchanted:%s,is_damageable:%s,is_food:%s, nbt:%s}}" % \
               (self.registry, self.count, self.max_count, self.damage, self.max_damage,
                self.name, self.rarity, self.repair_cost,
                str(self.is_enchanted), str(self.is_damageable), str(self.is_food), self.nbt)


class Inventory:
    def __init__(self, client: Client, entity_uuid: str):
        self.entity_uuid = entity_uuid
        self.client = client

    def get_mainhand(self) -> ItemStack:
        items = self.client.request("get_entity_inventory_main_hand", self.entity_uuid)
        if "data" not in items:
            return ItemStack("minecraft:air", 0, 64, 0, 0, "translation{key='item.minecraft.air', args=[]}", "COMMON",
                             0, False, False, False, "{}")
        return ItemStack(*items["data"])

    def get_offhand(self) -> ItemStack:
        items = self.client.request("get_entity_inventory_off_hand", self.entity_uuid)
        return ItemStack(*items["data"])

    def get_armor(self):
        self.client.notify_server("get_entity_inventory_armor:reset")
        time.sleep(0.1)  # make sure the socket flushed before sending the other requests
        raw_stack = self.client.request("get_entity_inventory_armor:iterator", self.entity_uuid)
        while "data" in raw_stack and raw_stack["data"][0] != 'CONSUMED':
            if raw_stack["data"] != "NULL":
                yield ItemStack(*raw_stack["data"])

            raw_stack = self.client.request("get_entity_inventory_armor:iterator", self.entity_uuid)

    def get_selected_slot(self) -> int:
        return self.client.request_int("get_entity_inventory_selected_slot", 0, self.entity_uuid)

    def get_item_in_slot(self, slot: int) -> ItemStack:
        items = self.client.request("get_entity_inventory_stack_at", slot)
        if "data" not in items or items["data"][0] == "NULL":
            return ItemStack("minecraft:air", 0, 64, 0, 0, "translation{key='item.minecraft.air', args=[]}", "COMMON",
                             0, False, False, False, "{}")

        return ItemStack(*items["data"])



