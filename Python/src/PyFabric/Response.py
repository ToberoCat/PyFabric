class WaitResponse:
    def __init__(self):
        self.packet = None

    def create_promise(self):
        while self.packet is None:  # Wait for condition to be true (Not very elegant)
            pass
        return self.packet

    def receive_packet(self, packet):
        self.packet = packet
