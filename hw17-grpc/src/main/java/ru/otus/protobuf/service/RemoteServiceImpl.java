package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.MessageRequest;
import ru.otus.protobuf.generated.MessageResponse;
import ru.otus.protobuf.generated.RemoteServiceGrpc;


public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Override
    public void getNumbers(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
        for (long i = request.getFirstNumber(); i <= request.getLastNumber(); i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(MessageResponse.newBuilder().setNumber(i).build());
        }
        responseObserver.onCompleted();
    }
}
