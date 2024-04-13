import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;


// Server for a multi-player TicTacToe game

public class TicTacToeServer {

	// creating server socket and will display that the server is running once engaged
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(58901)) {
            System.out.println("Tic Tac Toe Server is Running...");
            var pool = Executors.newFixedThreadPool(200);
            while (true) {
            	
            	// looking for player 'X' and player 'O' to start the game; if one of them is not connected, the game won't start
                Game1 game = new Game1();
                pool.execute(game.new Player(listener.accept(), 'X'));
                pool.execute(game.new Player(listener.accept(), 'O'));
            }
        }
    }
}

class Game1 {

	// the board is numbered 0 - 8 from top to bottom and left to right. 
	// empty = null
    private Player[] board = new Player[9];

    Player currentPlayer;

    public boolean hasWinner() {
        return (board[0] != null && board[0] == board[1] && board[0] == board[2])
                || (board[3] != null && board[3] == board[4] && board[3] == board[5])
                || (board[6] != null && board[6] == board[7] && board[6] == board[8])
                || (board[0] != null && board[0] == board[3] && board[0] == board[6])
                || (board[1] != null && board[1] == board[4] && board[1] == board[7])
                || (board[2] != null && board[2] == board[5] && board[2] == board[8])
                || (board[0] != null && board[0] == board[4] && board[0] == board[8])
                || (board[2] != null && board[2] == board[4] && board[2] == board[6]);
    }

    public boolean boardFilledUp() {
        return Arrays.stream(board).allMatch(p -> p != null);
    }

    // this resets the board
    public void reset() {
        for(int i = 0; i < 9; i++) {
            board[i] = null;
        }
    }

    // these are the illegal actions of the game. whether it is not a players turn, a box is already occupied, or there is no opponent to play against
    public synchronized void move(int location, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        } else if (player.opponent == null) {
            throw new IllegalStateException("You don't have an opponent yet");
        } else if (board[location] != null) {
            throw new IllegalStateException("Cell already occupied");
        }
        board[location] = currentPlayer;
        currentPlayer = currentPlayer.opponent;
    }

    
    // when joining the server, each client will be either the 'X' or 'O' player. 
    // each client will have a Socket, Scanner, and PrintWriter.
    class Player implements Runnable {
        char mark;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;
        boolean choice = true;

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
        }

        @Override
        public void run() {
            try {
                    setup();
                    processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (opponent != null && opponent.output != null) {
                    opponent.output.println("OTHER_PLAYER_LEFT");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        public boolean getChoice() {
            return choice;
        }

        
        // as new player joins, the player is welcomed and marked as either 'X' or 'O' player
        // if both players are on the server, then it will display a message to the player stating whether it is their turn to make a move
        // otherwise, if only one player is on the server, it will state that they are waiting for another opponent to connect
        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + mark);
            if (mark == 'X') {
                currentPlayer = this;
                output.println("MESSAGE Waiting for opponent to connect");
            } else {
                opponent = currentPlayer;
                opponent.opponent = this;
                opponent.output.println("MESSAGE Your move");
            }
        }

        // gives players the option to restart, quit, or move the game
        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if(command.startsWith("Restart")) {
                    reset();
                }
                if (command.startsWith("QUIT")) {
                    return;
                } else if (command.startsWith("MOVE")) {
                    processMoveCommand(Integer.parseInt(command.substring(5)));
                }
            }
        }

        // this processes each players moves and determines whether it is valid or not. 
        // also determines who to print the winner and loser message to
        private void processMoveCommand(int location) {
            try {
                move(location, this);
                output.println("VALID_MOVE");
                opponent.output.println("OPPONENT_MOVED " + location);
                if (hasWinner()) {
                    output.println("VICTORY");
                    opponent.output.println("DEFEAT");
                } else if (boardFilledUp()) {
                    output.println("TIE");
                    opponent.output.println("TIE");
                }

            } catch (IllegalStateException e) {
                output.println("MESSAGE " + e.getMessage());
            }
        }
    }
}