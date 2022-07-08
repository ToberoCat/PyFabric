import time

from Minecraft import Fabric
from Minecraft.Events.EventType import EventType

Minecraft = Fabric.create()


@Minecraft.on(EventType.JOIN)
def join_world():
    print("Loaded world")
    player = Minecraft.get_client_player()

    player.send_chat("§ePython§7 connect with §6your§7 client")
    player.send_chat(f"§aYour world: {player.get_world()}")
