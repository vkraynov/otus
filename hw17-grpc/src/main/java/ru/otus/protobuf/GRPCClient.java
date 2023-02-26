package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.generated.MessageRequest;
import ru.otus.protobuf.generated.MessageResponse;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int FIRST_NUMBER = 0;
    private static final int LAST_NUMBER = 20;

    public static void main(String[] args) {
        BlockingQueue<MessageResponse> queue = new ArrayBlockingQueue<>(LAST_NUMBER - FIRST_NUMBER);

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        System.out.println("Start");

        var newStub = RemoteServiceGrpc.newStub(channel);
        newStub.getNumbers(MessageRequest.newBuilder()
                        .setFirstNumber(FIRST_NUMBER)
                        .setLastNumber(LAST_NUMBER).build(),
                new Observer(queue));

        long currentValue = 0;

        for (int i = 0; i <= 50; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<MessageResponse> currentMessagesFromServer = new ArrayList<>();
            queue.drainTo(currentMessagesFromServer);
            if (!currentMessagesFromServer.isEmpty()) {
                currentValue = currentValue + currentMessagesFromServer.get(currentMessagesFromServer.size() - 1).getNumber() + 1;
            } else {
                currentValue++;
            }
            System.out.println("Current value: " + currentValue);
        }

        channel.shutdown();

        System.out.println("Finish");
    }
}
