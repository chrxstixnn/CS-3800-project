import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


// Client for a multi-player TicTacToe game

public class TicTacToeClient {

    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel messageLabel = new JLabel("...");

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    // creating buttons for the users to be allowed to choose which boxes they want to play their 'X' or 'O'
    private JButton[] buttons = new JButton[9];
    private JButton currentButton;

    public TicTacToeClient(String serverAddress) throws Exception {

    	// connecting to server address
        socket = new Socket(serverAddress, 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        // setting design for message label and frame
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, BorderLayout.SOUTH);


        

        var boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for(int i = 0; i < 9; i++) {
            final int j = i;
            buttons[i] = new JButton();
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentButton = buttons[j];
                    out.println("MOVE "+j);
                }
            });
            boardPanel.add(buttons[i]);
        }
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
    }
    
    // the client will listen for messages from the server where the first messages welcome each player and mark them 'X' and 'O'
    // once welcomed, the code enters a loop that processes each message and proceeds accordingly
    // after the "VICTORY", "DEFEAT", or "TIE" messages, the users will be asked whether they want a rematch or not
    // if one of the players says 'no' then loop is exited and the message "OTHER_PLAYER_LEFT" is displayed
    // otherwise, if both say 'yes', the game will restart
    
    public void play() throws Exception {
        try {
            var response = in.nextLine();
            var mark = response.charAt(8);
            var opponentMark = mark == 'X' ? 'O' : 'X';
            frame.setTitle("Tic Tac Toe: Player " + mark);
            while (in.hasNextLine()) {
                response = in.nextLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentButton.setText(mark+"");
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    var loc = Integer.parseInt(response.substring(15));
                    buttons[loc].setText(opponentMark+"");
                    messageLabel.setText("Opponent moved, your turn");
                } else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                } else if (response.startsWith("VICTORY")) {
                    JOptionPane.showMessageDialog(frame, "Congratulations, You won");
                    int choice = JOptionPane.showConfirmDialog(null, "Would you like to rematch?", "Restart", JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        for(int i =0; i < 9; i++) {
                            buttons[i].setText("");
                        }
                        response = "VALID_MOVE";
                        out.println("Restart");
                        continue;
                    }
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    JOptionPane.showMessageDialog(frame, "Sorry, You lost");
                    int choice = JOptionPane.showConfirmDialog(null, "Would you like to rematch?", "Restart", JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        for(int i =0; i < 9; i++) {
                            buttons[i].setText("");
                        }
                        response = "OPPONENT_MOVED";
                        out.println("Restart");
                        continue;
                    }
                    break;
                } else if (response.startsWith("TIE")) {
                    JOptionPane.showMessageDialog(frame, "Tie");
                    int choice = JOptionPane.showConfirmDialog(null, "Would you like to rematch?", "Restart", JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        for(int i =0; i < 9; i++) {
                            buttons[i].setText("");
                        }
                        if(mark == 'X') {
                            response = "OPPONENT_MOVED";
                        }
                        else {
                            response = "VALID_MOVE";
                        }
                        out.println("Restart");
                        continue;
                    }
                    break;
                } else if (response.startsWith("OTHER_PLAYER_LEFT")) {
                    JOptionPane.showMessageDialog(frame, "Other player left");
                    break;
                }
            }
            
            // quit = exit game
            out.println("QUIT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
            frame.dispose();
        }
    }

    

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        
        // setting frame dimensions
        TicTacToeClient client = new TicTacToeClient(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(320, 320);
        client.frame.setVisible(true);
        client.frame.setResizable(false);
        client.play();
    }
}