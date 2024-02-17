package com.kiszka;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final int PORT = 7767;
    private static final Set<Socket> clients = new HashSet<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        for(;;){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected "+clientSocket.getRemoteSocketAddress());
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }
    static class ClientHandler implements Runnable{
        private final Socket clientSocket;
        public ClientHandler(Socket clientSocket){
            this.clientSocket=clientSocket;
            synchronized (clients){
                clients.add(clientSocket);
            }
        }

        @Override
        public void run() {
            try{
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                for(;;){
                    String message = inputStream.readUTF();
                    synchronized (clients){
                        for(var client : clients){
                            if(client != clientSocket){
                                try{
                                    DataOutputStream output = new DataOutputStream(client.getOutputStream());
                                    output.writeUTF(message);
                                } catch (IOException e){
                                    clients.remove(client);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e){
                synchronized (clients){
                    clients.remove(clientSocket);
                }
            } finally {
                try{
                    clientSocket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
