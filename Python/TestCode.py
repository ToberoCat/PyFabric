from Minecraft import Fabric
from Minecraft.Events.EventType import EventType

Minecraft = Fabric.create()


@Minecraft.on(EventType.JOIN_WORLD)
def join_world():
    print("loaded world")
    player = Minecraft.get_client_player()
    print(player)
