package io.github.toberocat.pyfabric.command;

import java.util.LinkedList;

public class JsonCommand {
    private String literal;
    private Type type;
    private LinkedList<JsonCommand> subCommands;

    public JsonCommand() {
        literal = "";
        subCommands = new LinkedList<>();
        type = Type.SUB;

    }

    public JsonCommand(String literal, LinkedList<JsonCommand> subCommands) {
        this.literal = literal;
        this.subCommands = subCommands;
        type = Type.SUB;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public LinkedList<JsonCommand> getSubCommands() {
        return subCommands;
    }

    public void setSubCommands(LinkedList<JsonCommand> subCommands) {
        this.subCommands = subCommands;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
