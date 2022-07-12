package io.github.toberocat.pyfabric.server;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractServer implements Runnable {

    protected final Logger logger;
    private final ServerSocket server;
    private final LinkedHashMap<String, BiConsumer<List<Object>, Consumer<Package>>> methods;
    private final ArrayList<Package> sendToSocket;
    private final Gson gson;
    private Thread senderThread;

    private Socket connected;
    private boolean running;

    public AbstractServer(int port, Logger logger) {
        ServerSocket server1;
        try {
            server1 = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            server1 = null;
        }
        this.server = server1;
        this.running = true;
        this.methods = new LinkedHashMap<>();
        this.sendToSocket = new ArrayList<>();
        this.gson = new Gson();
        this.logger = logger;
    }

    public void sendPacket(Package pack) {
        sendToSocket.add(pack);
        if (senderThread != null && senderThread.isAlive()) return;
        senderThread = new Thread(() -> {
            while (sendToSocket.size() != 0) {
                sendNextPacket();
            }
        });
        senderThread.start();
    }

    public void createServer() {
        new Thread(this).start();
    }

    private Package parsePackage(@NotNull String raw) throws IOException {
        logger.info(raw);
        return gson.fromJson(raw, Package.class);
    }

    private String packToString(Package pack) throws IOException {
        return gson.toJson(pack);
    }

    protected void pythonJoined() {
        // Event overrideable
    }

    private void sendNextPacket() {
        if (connected == null || connected.isClosed() || sendToSocket.size() == 0) return;
        PrintWriter outputPipeline;
        try {
            Package pack = sendToSocket.get(0);
            outputPipeline = new PrintWriter(connected.getOutputStream());
            outputPipeline.write(packToString(pack));
            outputPipeline.flush();

            sendToSocket.remove(0);
            Thread.sleep(100);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void worker() throws IOException {
        Socket socket = server.accept();
        connected = socket;

        if (socket == null) return;
        logger.info("Python connected");
        pythonJoined();

        PrintWriter outputPipeline = new PrintWriter(socket.getOutputStream());
        InputStream is = socket.getInputStream();
        while (!socket.isClosed()) {
            byte[] buffer = new byte[1024];
            int read = is.read(buffer);
            if (read == -1) continue;

            Package pack = parsePackage(new String(buffer, 0, read));
            logger.info("Received: " + pack.getId());
            if (methods.containsKey(pack.getId())) {
                methods.get(pack.getId()).accept(pack.getData(), this::sendPacket);
            }

        }

        outputPipeline.close();
        is.close();
        socket.close();
        connected = null;
    }

    @Override
    public void run() {
        while (running) {
            try {
                worker();
            } catch (IOException e) {
                logger.info("Python disconnected");
                connected = null;
            }
        }
    }

    public void addMethod(String id, BiConsumer<List<Object>, Consumer<Package>> action) {
        methods.put(id, action);
    }
}
