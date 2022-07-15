# PyFabric - The websocket powered minecraft-python hook

This is a beta build - meaning not all functions are implemented.
Currently only 1.19, but with first stable release there will also be one for 1.8

## Setup - Fabric
- Download fabric for 1.19
- Double-click the fabric installer and select launcher
- After the launcher profile got created, go to `%appdata%/.minecraft` and create `mods` folder if it doesn't exist yet
- Now put the fabric api- and PyFabric jar into the folder
- Start minecraft with fabric (Use fabric for 1.19!)

## Setup - Python
- Put ``Minecraft`` folder and `TestCode.py` into a folder your choice. Note: 
When picking the folder make sure you can find it again
- To get the player position, just open a terminal in the folder where both, file and folder, are stored.
In the console run: ``python TestCode.py``.
- When minecraft fabric with the PyFabric mod gets detected, python will hook into the mod
and now interacts with it
- When connected with python joining a world, you should now see something like in ``example_output.png``
![Example console log](example_output.png)

## Limitations

This package currently only supports data grabbing and does not allow data to be sent to a minecraft server.

Because of that, you can only get data from your client. All actions are limited to the currently loaded chunks / world