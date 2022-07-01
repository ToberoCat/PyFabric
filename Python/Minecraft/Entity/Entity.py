from Minecraft.Networking.Client import Client


class Location:
    def __init__(self, uuid: str, client: Client):
        print("requesting")
        self.x = client.request_float("get_entity_x", 0, uuid)
        self.y = client.request_float("get_entity_y", 0, uuid)
        self.z = client.request_float("get_entity_z", 0, uuid)
        self.yaw = client.request_float("get_entity_yaw", 0, uuid)
        self.pitch = client.request_float("get_entity_pitch", 0, uuid)
        self.world = client.request_float("get_entity_world", 0, uuid)

    def __str__(self):
        return "location:{x:%d,y:%d,z:%d,yaw:%d,pitch:%d,world:%s}" % (
            self.x, self.y, self.z, self.yaw, self.pitch, self.world)


class Inventory:
    def __init__(self, bound_entity: str):
        self.uuid = bound_entity


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

    def send_title(self, title: str, subtitle: str):
        pass

    def send_chat(self, message: str):
        pass

    def send_actionbar(self, action_bar):
        pass

    def get_inventory(self):
        return Inventory(self.uuid)
