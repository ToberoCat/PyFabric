from src.PyFabric.Entity.Inventory import Inventory
from src.PyFabric.Networking.Client import Client


class Location:
    def __init__(self, uuid: str, client: Client):
        self.client = client
        self.uuid = uuid
        self.x, self.y, self.z, self.yaw, self.pitch = 0, 0, 0, 0, 0
        self.update()

    def update(self):
        raw_location = self.client.request("get_entity_location", self.uuid)
        self.x, self.y, self.z, self.yaw, self.pitch = raw_location["data"]

    def __str__(self):
        return "location:{x:~%d,y:~%d,z:~%d,yaw:~%d,pitch:~%d}" % (
            self.x, self.y, self.z, self.yaw, self.pitch)


class Entity:
    def __init__(self, client: Client, uuid: str):
        self.uuid = uuid
        self.client = client
        self.location = Location(uuid, client)

    def __str__(self):
        return "{entity:{uuid:%s,location:%s}}" % (self.uuid, self.location)


class Player(Entity):
    def __init__(self, client: Client, uuid: str):
        super(Player, self).__init__(client, uuid)
        self.client = client

    def get_block_looking_at(self):
        return self.client.request_string("block_looking_at", 0, self.uuid)

    def get_dimension(self):
        return self.client.request_string("get_client_player_world", 0)

    def send_chat(self, message: str):
        """
        Send a message to the player

        Note: messages in minecraft can only be 256 characters long (Socket transfer limit)
        :param message: The message the player should receive. Use § for colors
        """
        self.client.notify_server("client_chat", message)

    def send_actionbar(self, action_bar):
        self.client.notify_server("client_actionbar", action_bar)

    def get_inventory(self) -> Inventory:
        return Inventory(self.client, self.uuid)
