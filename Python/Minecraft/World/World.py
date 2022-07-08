class World:
    def __init__(self, world_name: str):
        self.world_name = world_name

    def get_entites(self):
        pass

    def spawn_particle(self):
        pass

    def play_sound(self):
        pass

    def get_players(self):
        pass

    def get_chunk(self, x: int, z: int):
        pass

    def get_block_at(self, x: int, y: int, z: int):
        pass


class Chunk:
    def __init__(self, x: int, z: int):
        self.x = x
        self.z = z

    def get_bloc_at(self, x: int, y: int, z: int):
        pass
