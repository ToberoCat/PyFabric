from Minecraft.Entity.Inventory import Inventory
from Minecraft.Networking.Client import Client


class Location:
    def __init__(self, uuid: str, client: Client):
        raw_location = client.request("get_entity_location", uuid)
        self.x, self.y, self.z, self.yaw, self.pitch = raw_location["data"]

    def __str__(self):
        return "location:{x:~%d,y:~%d,z:~%d,yaw:~%d,pitch:~%d}" % (
            self.x, self.y, self.z, self.yaw, self.pitch)


class Entity:
    def __init__(self, uuid: str, location: Location):
        self.uuid = uuid
        self.location = location

    def __str__(self):
        return "{entity:{uuid:%s,%s}}" % (self.uuid, self.location)


class Player(Entity):
    def __init__(self, client: Client):
        uuid = client.request_string("get_client_player_uuid", 0)
        super(Player, self).__init__(uuid, Location(uuid, client))
        self.client = client

    def get_dimension(self):
        return self.client.request_string("get_entity_dimension", 0, self.uuid)

    def send_chat(self, message: str):
        self.client.notify_server("client_chat", message)

    def send_actionbar(self, action_bar):
        self.client.notify_server("client_actionbar", action_bar)

    def get_inventory(self):
        return Inventory(self.uuid)
