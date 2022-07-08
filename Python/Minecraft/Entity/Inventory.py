class ItemStack:
    def __init__(self):
        pass

class Inventory:
    def __init__(self, entity_uuid: str):
        self.entity_uuid = entity_uuid

    def get_main_hand(self) -> ItemStack:
        return ItemStack()

    def get_offhand(self) -> ItemStack:
        return ItemStack()

    def get_armor(self) -> list:
        """
        Get the entites selected armor
        :return: A list of all armor pieces
        """

        return []

    def get_selected_slot(self) -> int:
        return 0

    def get_item_in_slot(self, slot: int) -> ItemStack:
        return ItemStack



