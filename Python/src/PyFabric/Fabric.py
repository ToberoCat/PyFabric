from src.PyFabric.Events.EventType import EventType
from src.PyFabric.Networking.Client import Client
from src.PyFabric.Entity.Entity import Player, Entity
from pyee.base import EventEmitter

from src.PyFabric.World.World import ClientWorld


class Fabric(EventEmitter):
    def __init__(self):
        super().__init__()
        self.client = Client(self.__handle_event__, 1337)
        self.cmd = EventEmitter()
        self.__client_player__ = None

    def __handle_event__(self, event_type: str, packet: dict):
        val = EventType(event_type)
        if val == EventType.ON_COMMAND:
            data = packet['data']
            if len(data) == 1:
                self.cmd.emit("/" + data[0][1:], self.get_client_player())
            else:
                self.cmd.emit("/" + data[0][1:], self.get_client_player(), data[1:])
        self.emit(val)

    def get_client_player(self):
        if not self.client.connected:
            return None

        if self.__client_player__ is None:
            uuid = self.client.request_string("get_client_player_uuid", 0)
            self.__client_player__ = Player(self.client, uuid)

        return self.__client_player__

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

    def register_command(self, cmd: str):
        self.client.notify_server("register_command", cmd)


def create():
    return Fabric()

