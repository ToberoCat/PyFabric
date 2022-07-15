import time

from src.PyFabric.Networking.Client import Client


class ClientWorld:
    def __init__(self, client: Client):
        self.client = client

    def get_entities(self):
        """
        This will return a generator with all entites in the world
        Note: It
        :return: A generator function for all entities
        """
        self.client.notify_server("reset_entities")
        time.sleep(0.1)  # make sure the socket flushed before sending the other requests
        raw_uuid = self.client.request_string("get_all_entities_in_world_single", 0)
        while raw_uuid != "CONSUMED":
            yield raw_uuid
            raw_uuid = self.client.request_string("get_all_entities_in_world_single", 0)

    def spawn_particle(self):
        """
        Has no implementation yet - So don't use it
        :return:
        """
        pass

    def play_sound(self):
        """
        Has no implementation yet - So don't use it
        :return:
        """
        pass

    def get_players(self):
        self.client.notify_server("reset_players")
        time.sleep(0.1)  # make sure the socket flushed before sending the other requests
        raw_uuid = self.client.request_string("get_all_players_in_world_single", 0)
        while raw_uuid != "CONSUMED":
            yield raw_uuid
            raw_uuid = self.client.request_string("get_all_players_in_world_single", 0)

    def get_block_at(self, x: int, y: int, z: int):
        material = self.client.request_string("get_block_at", 0, x, y, z)
        return material
