from src.PyFabric import Fabric
from src.PyFabric.Command.Command import CommandBuilder, ArgumentType
from src.PyFabric.Events.EventType import EventType

Minecraft = Fabric.create()

# Register commands
Minecraft.register_command(CommandBuilder()
                           .set_name("python")
                           .add_sub_command(CommandBuilder()
                                            .set_name("armor")
                                            .build())
                           .add_sub_command(CommandBuilder()
                                            .set_name("hand")
                                            .add_sub_command(CommandBuilder()
                                                             .set_name("main")
                                                             .build())
                                            .add_sub_command(CommandBuilder()
                                                             .set_name("off")
                                                             .build())
                                            .build())
                           .add_sub_command(CommandBuilder()
                                            .set_name("slot")
                                            .add_sub_command(CommandBuilder()
                                                             .set_name("item_slot")
                                                             .set_arg(ArgumentType.INTEGER)
                                                             .build())
                                            .build())
                           .build())


@Minecraft.on(EventType.JOIN)
def join_world():
    print("Loaded world")

    player = Minecraft.get_client_player()

    player.send_chat("§ePython§7 connect with §6your§7 client")  # Limited to max chat limit

    # player.send_actionbar(f"§aYour world: {player.get_dimension()}")
    # print(player.get_inventory().get_mainhand())
    # print(Minecraft.get_world().get_block_at(0, 70, 0))


# Handle commands
@Minecraft.cmd.on("/python hand main")
def tell_main_item(player):
    main_hand = player.get_inventory().get_mainhand()
    player.send_chat(main_hand.registry)


@Minecraft.cmd.on("/python hand off")
def tell_off_item(player, args):
    main_hand = player.get_inventory().get_offhand()
    player.send_chat(main_hand.registry)


@Minecraft.cmd.on("/python armor")
def tell_armor(player, args):
    armor_generator = player.get_inventory().get_armor()
    for armor in list(armor_generator):
        player.send_chat(armor.registry)


@Minecraft.cmd.on("/python slot")
def tell_slot(player, args):
    player.send_chat(args[0])


Minecraft.ready()
