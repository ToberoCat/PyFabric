from enum import Enum, auto


class ArgumentType(Enum):
    SUB = auto()
    BOOL = auto()
    FLOAT = auto()
    DOUBLE = auto()
    INTEGER = auto()
    LONG = auto()
    STRING = auto()
    GREEDY_STRING = auto()
    BLOCK_POS = auto()
    VEC3 = auto()
    VEC2 = auto()
    COLOR = auto()
    COMPONENT = auto()
    MESSAGE = auto()
    NBT_COMPOUND_TAG = auto()
    NBT_TAG = auto()
    OPERATION = auto()
    PARTICLE = auto()
    ANGLE = auto()
    ROTATION = auto()
    ITEM_SLOT = auto()
    DIMENSION = auto()
    TIME = auto()
    UUID = auto()
    INT_RANGE = auto()
    FLOAT_RANGE = auto()

    def _generate_next_value_(self, start, count, last_values):
        return self


class CommandBuilder:
    def __init__(self):
        self.__literal__ = ""
        self.__argument_type__ = ArgumentType.SUB
        self.__subs__ = []

    def set_name(self, name: str):
        self.__literal__ = name
        return self

    def set_arg(self, arg: ArgumentType):
        self.__argument_type__ = arg
        return self

    def add_sub_command(self, cmd: str):
        self.__subs__.append(cmd)
        return self

    def build(self) -> str:
        subs = ", ".join(self.__subs__)

        return '{"literal":"%s","type":"%s","subCommands": [%s]}' % (self.__literal__, self.__argument_type__.name, subs)