package org.example;


import com.example.grpc.Service;
import com.example.grpc.subscribeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Receiver {
    private static String port;
    private static String topic;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException{
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext().build();

        System.out.println("Enter port");
        port = scanner.nextLine();
        System.out.println("Enter Topic");
        topic = scanner.nextLine();
        String clientAdress = InetAddress.getLocalHost().getHostAddress()+":"+port;
        new Server(InetAddress.getLocalHost().getHostAddress(),port);

        //SubscribePart
        subscribeServiceGrpc.subscribeServiceBlockingStub stub1 = subscribeServiceGrpc.newBlockingStub(channel);
        Service.subscribeRequest.Builder subscribeRequestBuilder = Service.subscribeRequest.newBuilder();
        Service.subscribeRequest request1 = subscribeRequestBuilder.setAddress(clientAdress).setTopic(topic).build();
        Iterator<Service.subscribeResponse> response1 = stub1.subscribe(request1);
        System.out.println("You was subscribed on topic -> " + topic);

        List<Service.receivedMessage> a = response1.next().getMessageList();
        for (Service.receivedMessage mes : a)
        {
            System.out.println(mes.getMessage());
        }

    }
}

