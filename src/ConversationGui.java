/*
 *  Klasa ConversationGui
 *  Obsługuje interfejs graficzny konwersacji
 *
 *  @author Tobiasz Rumian
 *  @version 1.1
 *   Data: 06 Styczeń 2017 r.
 *   Indeks: 226131
 *   Grupa: śr 13:15 TN
 */

import javax.swing.*;


class ConversationGui extends JFrame {
    private Conversation conversation;
    private JMenuBar menuBar = new JMenuBar();
    private JButton
            buttonSend = new JButton("Wyślij"),
            buttonDisconnect = new JButton("Rozłącz");
    private JTextField message = new JTextField();

    {
        menuBar.add(message);
        menuBar.add(buttonSend);
        menuBar.add(buttonDisconnect);
    }

    private JTextArea text = new JTextArea();
    private JScrollPane conversationScroll = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    {
        text.setAutoscrolls(true);
        text.setEditable(false);
        conversationScroll.setAutoscrolls(true);
    }

    ConversationGui(Conversation conversation) {
        this.conversation = conversation;
        setTitle(conversation.getNick() + "->" + conversation.getUser());
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(conversationScroll);
        setJMenuBar(menuBar);
        setVisible(true);
        buttonSend.addActionListener(e -> {
            if (!message.getText().isEmpty()) {
                text.append(conversation.getNick() + " <<<" + message.getText().trim() + "\n");
                conversation.send(message.getText().trim());
                message.setText("");
            }
        });
        buttonDisconnect.addActionListener(e -> {
            conversation.disconnect();
            setVisible(false);
            dispose();
        });
    }
    void append(String nick,String message){
        text.append(nick + " >>>" + message + "\n");
    }
    void kill() {
        setVisible(false);
        dispose();
    }
}
