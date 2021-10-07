package org.example;

import com.example.grpc.Service;
import com.example.grpc.messageReceiverGrpc;
import com.example.grpc.subscribeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.imageio.ImageTranscoder;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Publisher
{
    private static String port;
    private static String message;
    private static String topic;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {


        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext().build();

        System.out.println("Enter port");
        port = scanner.nextLine();
        System.out.println("Enter Topic");
        topic = scanner.nextLine();

        new Server(InetAddress.getLocalHost().getHostAddress(),port);


        //Sender Part
        messageReceiverGrpc.messageReceiverBlockingStub stub = messageReceiverGrpc.newBlockingStub(channel);
        Service.receivedMessage.Builder requestBuilder = Service.receivedMessage.newBuilder();

        Service.receivedMessage request;
        Service.newResponse response;

        while (true)
        {
            System.out.println("Enter message:");
            message = scanner.nextLine();
            if (message.equals("exit"))
            {
                System.out.println("Bye bye...");
                channel.shutdown();
            }
            request = requestBuilder.setMessage(message).setTopic(topic).build();
            response = stub.receive(request);
        }
    }
}
