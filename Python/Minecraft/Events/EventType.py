from enum import Enum


class EventType(Enum):
    JOIN = "joined"
    QUIT = "quit"
    PLAYER_MOVED = "player_moved"
