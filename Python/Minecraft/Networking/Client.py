import json
import socket
import threading
import uuid

from Minecraft.Events.EventType import EventType
from Minecraft.Networking.Response import WaitResponse


class ResponseError(Exception):
    def __init__(self, text: str):
        super(ResponseError, self).__init__(text)


class Client:
    def __init__(self, handle_event, port: int = 1337):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.settimeout(10)

        try:
            self.socket.connect(("localhost", port))
            self.connected = True
        except ConnectionRefusedError:
            print("[ERROR]: Couldn't connect with the fabric mod")
            self.connected = False
            return

        self.handle_event = handle_event
        self.waiting_response = {}
        self.uuid = uuid.uuid4()

        self.worker = threading.Thread(target=self.__request_worker__)
        self.worker.start()

    def __request_worker__(self):
        # Start listening to server response
        while self.connected:
            try:
                raw = self.socket.recv(1024).decode('utf-8')
                if not raw:
                    continue

                msg = json.loads(raw)
                if not self.connected:
                    continue

                if "id" not in msg:
                    print(f"[WARN]: Received an event with no id. Event response: {msg}")
                    continue

                msg_id = msg["id"]
                if msg_id in self.waiting_response:
                    self.waiting_response.get(msg_id)(msg)
                    del self.waiting_response[msg_id]
                else:
                    self.handle_event(msg_id)

            except socket.timeout:  # Ignored
                pass
            except ConnectionResetError:
                print("[INFO]: Server closed. Stopping services")
                self.connected = False

    def request_float(self, registry: str, index: int, *data) -> float:
        return self.request_item(registry, index, *data)

    def request_string(self, registry: str, index: int, *data) -> str:
        return self.request_item(registry, index, *data)

    def request_item(self, registry: str, index: int, *data):
        response = self.request(registry, *data)
        if "data" not in response:
            raise ResponseError(f"Response doesn't contain a data tag. Response: {response}")
        data_array = response.get("data")
        if index >= len(data_array):
            raise ResponseError(f"Response index out of index. Response: {response}")
        return data_array[index]

    def request(self, registry: str, *data) -> dict:
        package = {
            "id": registry,
            "data": data
        }

        return self.request_raw(package)

    def notify_server(self, registry: str, *data) -> None:
        if not self.connected:
            return

        packet = {
            "id": registry,
            "data": data
        }

        self.socket.send(bytes(json.dumps(packet).encode('utf-8')))

    def request_raw(self, packet: dict) -> dict:
        if not self.connected:
            return {}

        response = WaitResponse()
        self.waiting_response[packet["id"]] = response.receive_packet
        self.socket.send(bytes(json.dumps(packet).encode('utf-8')))

        # Get server response
        return response.create_promise()
