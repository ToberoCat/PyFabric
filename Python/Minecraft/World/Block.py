from enum import Enum

class Block:
    def __init__(self, x, y, z, type):
        self.x = x
        self.y = y
        self.z = z
        self.type = type


class Color(Enum):
    RED = 1
    GREEN = 2
    BLUE = 3