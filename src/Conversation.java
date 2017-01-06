/*
 *  Klasa Conversation
 *  Obsługuje rozmowy między dwoma użytkownikami
 *
 *  @author Tobiasz Rumian
 *  @version 4.7
 *   Data: 06 Styczeń 2017 r.
 *   Indeks: 226131
 *   Grupa: śr 13:15 TN
 */

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Conversation implements Runnable {
    private ConversationGui gui;
    private String nick;
    private int port;
    private String user;
    private String host;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean server = false;

    Conversation(String nick, String user, int port, String host) {
        this.nick = nick;
        this.user = user;
        this.port = port;
        this.host = host;
        gui = new ConversationGui(this);
        (new Thread(this)).start();
    }

    Conversation(String nick, Socket socket) {
        this.nick = nick;
        try {
            this.socket = socket;
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            user = (String) input.readObject();
            server = true;
            gui = new ConversationGui(this);
            (new Thread(this)).start();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (!server) {
            try {
                socket = new Socket(host, port);
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(nick);

            } catch (IOException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
                return;
            }
        }

        try {
            while (true) {
                String in = (String) input.readObject();
                if (in.equals("EXIT")) {
                    break;
                }
                gui.append(user,in);
            }
            input.close();
            output.close();
            socket.close();
            gui.kill();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
            gui.kill();
        }
    }

    String getNick() {
        return nick;
    }

    String getUser() {
        return user;
    }

    void send(String message) {
        try {
            output.writeObject(message);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    void disconnect(){
        try {
            output.writeObject("EXIT");
            input.close();
            output.close();
            socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
