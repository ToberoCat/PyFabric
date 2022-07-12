from Minecraft.Events.EventType import EventType
from Minecraft.Networking.Client import Client
from Minecraft.Entity.Entity import Player, Entity, Location
from pyee.base import EventEmitter

from Minecraft.World.World import ClientWorld


class Fabric(EventEmitter):
    def __init__(self):
        super().__init__()
        self.client = Client(self.__handle_event__, 1337)
        self.cmd = EventEmitter()

    def __handle_event__(self, event_type: str, packet: dict):
        val = EventType(event_type)
        if val == EventType.ON_COMMAND:
            self.cmd.emit(packet['data'][0])
        self.emit(val)

    def get_client_player(self):
        if not self.client.connected:
            return None

        uuid = self.client.request_string("get_client_player_uuid", 0)
        return Player(self.client, uuid)

    def get_player(self, uuid: str):
        if not self.client.connected:
            return None
        return Player(self.client, uuid)

    def get_entity(self, uuid):
        if not self.client.connected:
            return None
        return Entity(self.client, uuid)

    def get_world(self):
        if not self.client.connected:
            return None
        return ClientWorld(self.client)

    def register_command(self, name: str):
        self.client.notify_server("register_command", name)


def create():
    return Fabric()

