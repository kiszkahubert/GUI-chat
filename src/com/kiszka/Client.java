package com.kiszka;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private static final int PORT = 7767;
    private final JTextArea messageTextArea;
    private final Socket clientSocket;

    public Client(JTextArea messageTextArea) throws IOException {
        this.messageTextArea = messageTextArea;
        this.clientSocket = new Socket("localhost",PORT);
    }

    public void connect() throws IOException {
        DataInputStream input = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

        new Thread(new ClientMessageReceiver(input)).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String message = reader.readLine();
            output.writeUTF(message);
        }
    }
    public void sendMessage(String message) throws IOException{
        DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
        output.writeUTF(message);
    }

    private class ClientMessageReceiver implements Runnable {
        private final DataInputStream input;

        public ClientMessageReceiver(DataInputStream input) {
            this.input = input;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = input.readUTF();
                    messageTextArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
