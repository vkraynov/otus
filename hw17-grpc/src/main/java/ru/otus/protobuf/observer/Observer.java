package ru.otus.protobuf.observer;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.MessageResponse;

import java.util.concurrent.BlockingQueue;

/**
 * Observer.
 *
 * @author Vadim_Kraynov
 * @since 26.02.2023
 */
public class Observer implements StreamObserver<MessageResponse> {
    private final BlockingQueue<MessageResponse> queue;

    public Observer(BlockingQueue<MessageResponse> queue) {
        this.queue = queue;
    }

    @Override
    public void onNext(MessageResponse value) {
        System.out.println("Message from server: " + value.getNumber());
        queue.offer(value);
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t);
    }

    @Override
    public void onCompleted() {
        System.out.println("All numbers has been received!");
    }
}