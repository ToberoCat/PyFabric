package io.github.toberocat.pyfabric.server;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface BiReturnConsumer<T, U, R> {
    R accept(T t, U u) throws CommandSyntaxException;
}
