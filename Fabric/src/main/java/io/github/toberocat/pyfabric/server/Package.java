package io.github.toberocat.pyfabric.server;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Package {
    private String id;
    private List<Object> data;

    public Package() {
    }

    public Package(@NotNull String id, @NotNull List<Object> data) {
        this.id = id;
        this.data = new ArrayList<>(data);
    }

    public Package(@NotNull String id, Object... data) {
        this.id = id;
        this.data = List.of(data);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
