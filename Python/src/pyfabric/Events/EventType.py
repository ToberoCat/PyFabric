from enum import Enum


class EventType(Enum):
    JOIN = "joined"
    QUIT = "quit"
    ON_COMMAND = "on_command"
    ON_CHAT = "on_chat"
