package io.github.toberocat.pyfabric.server;

public interface Request<C, T> {
    default T run(C c) {
        try {
            return accept(c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    T accept(C c) throws Exception;
}
