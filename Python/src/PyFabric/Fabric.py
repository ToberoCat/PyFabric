from Client import Client
from EventType import EventType
from Entity import Player, Entity
from World import ClientWorld
from pyee.base import EventEmitter


class Fabric(EventEmitter):
    def __init__(self):
        super().__init__()
        self.client = Client(self.__handle_event__, 1337)
        self.cmd = EventEmitter()
        self.__client_player__ = None
        self.__ready__ = False
        self.queue_event = []

    def __handle_event__(self, event_type: str, packet: dict):
        if not self.__ready__:
            self.queue_event.append((event_type, packet))
            return

        val = EventType(event_type)
        if val == EventType.ON_COMMAND:
            data = packet['data']
            if len(data) == 1:
                self.cmd.emit("/" + data[0][1:], self.get_client_player())
            else:
                self.cmd.emit("/" + data[0][1:], self.get_client_player(), data[1:])
        self.emit(val)

    def ready(self):
        self.__ready__ = True
        for event_type, packet in self.queue_event:
            self.__handle_event__(event_type, packet)

        self.queue_event.clear()

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
