package com.kiszka;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InterfaceCfg extends JFrame implements ActionListener {
    private final JTextArea messageTextArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final Client client;
    public InterfaceCfg() throws IOException {
        messageTextArea = new JTextArea(30,60);
        messageTextArea.setEditable(false);
        messageTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageTextArea);

        client = new Client(messageTextArea);

        inputField = new JTextField(30);
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(scrollPane);
        contentPane.add(Box.createVerticalStrut(10));
        contentPane.add(inputField);
        contentPane.add(Box.createVerticalStrut(10));
        contentPane.add(sendButton);

        this.setContentPane(contentPane);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        client.connect();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sendButton){
            String message = inputField.getText();
            try{
                client.sendMessage(message);
                messageTextArea.append(message+"\n");
            } catch (IOException exp){
                exp.printStackTrace();
            }
            inputField.setText("");
        }
    }

    public static void main(String[] args) {
        try{
            new InterfaceCfg();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

