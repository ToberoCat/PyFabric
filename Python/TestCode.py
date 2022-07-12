from Minecraft import Fabric
from Minecraft.Events.EventType import EventType

Minecraft = Fabric.create()
player = None


@Minecraft.on(EventType.JOIN)
def join_world():
    global player
    print("Loaded world")

    player = Minecraft.get_client_player()

    player.send_chat("§ePython§7 connect with §6your§7 client")
    player.send_actionbar(f"§aYour world: {player.get_dimension()}")

    print(Minecraft.get_world().get_block_at(0, 70, 0))

    # Register commands
    Minecraft.register_command("hallo")


# Handle command
@Minecraft.cmd.on("hallo")
def test():
    player.send_chat("Test command")
