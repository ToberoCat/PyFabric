from Minecraft.Events.EventType import EventType
from Minecraft.Networking.Client import Client
from Minecraft.Entity.Entity import Player, Entity, Location
from pyee.base import EventEmitter


class Fabric(EventEmitter):
    def __init__(self):
        super().__init__()
        self.client = Client(self.__handle_event__, 1337)

    def __handle_event__(self, event_type: str):
        self.emit(EventType(event_type))

    def get_client_player(self):
        if not self.client.connected:
            return None
        return Player(self.client)

    def get_entity(self, uuid):
        if not self.client.connected:
            return None
        return Entity(uuid, Location(uuid, self.client))

    def get_world(self):
        if not self.client.connected:
            return None
        return


def create():
    return Fabric()

