from Minecraft import Fabric
from Minecraft.Events.EventType import EventType

Minecraft = Fabric.create()


@Minecraft.on(EventType.JOIN)
def join_world():
    print("Loaded world")
    player = Minecraft.get_client_player()
    print(player.location)  # Only parsed value
