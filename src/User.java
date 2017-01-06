/*
 *  Klasa User
 *  Obsługuje połączenia wychodzące oraz wybór kontaktów użytkownika.
 *
 *  @author Tobiasz Rumian
 *  @version 7.9
 *   Data: 06 Styczeń 2017 r.
 *   Indeks: 226131
 *   Grupa: śr 13:15 TN
 */

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class User implements Runnable {
    private UserGui gui;
    private UserServer userServer = new UserServer(this);
    static final int SERVER_PORT = 15000;
    private String nick;
    private String host;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    User(String nick) {
        new About(Boolean.FALSE);
        this.nick = nick;
        gui = new UserGui(this);
        (new Thread(this)).start();
    }


    @Override
    public void run() {
        try {
            host = InetAddress.getLocalHost().getHostName();
            socket = new Socket(host, SERVER_PORT);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(nick);
            output.writeObject(userServer.getHost());
            output.writeObject(Integer.toString(userServer.getPort()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
            gui.kill();
            return;
        }
        try {
            while (true) {
                String in = (String) input.readObject();
                if (in.equals("$CONNECTION$")) {
                    String user = (String) input.readObject();
                    String host = (String) input.readObject();
                    int port = Integer.parseInt((String) input.readObject());

                    new Conversation(nick, user, port, host);

                } else if (in.equals("$LIST$")) {
                    gui.clearUsers();
                    String s = (String) input.readObject();
                    while (!s.equals("$!LIST$")) {
                        gui.addUser(s);
                        s = (String) input.readObject();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
            userServer.kill();
            gui.kill();
        }
    }

    String getNick(){
        return nick;
    }
    void connect(int userIndex){
        try {
            output.writeObject("$" + userIndex);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    void getList(){
        try {
            output.writeObject("LIST");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    void about(){
        About about;
        try {
            about = new About();
            about.setVisible(true);
        } catch (Exception event) {
            System.err.println(event.getMessage());
        }
    }
}
