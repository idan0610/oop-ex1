import java.util.Scanner;

/**
 * The Competition class represents a Nim competition between two players, consisting of a given number of rounds. 
 * It also keeps track of the number of victories of each player.
 */
public class Competition {
	
	private Player player1;
	private Player player2;
	private int player1Score = 0;
	private int player2Score = 0;
	private boolean displayMessage;
	private Player currentPlayer;
	private static final int PLAYER1_ID = 1;
	private static final int PLAYER2_ID = 2;
	
	/**
	 * Constructs a new Competition object with the given parameters.
	 * @param player1 The first player.
	 * @param player2 The second player.
	 * @param displayMessage a flag indicating if to print messages in game.
	 */
	public Competition(Player player1, Player player2, boolean displayMessage) {
		this.player1 = player1;
		this.player2 = player2;
		this.displayMessage = displayMessage;
	}
	
	/**
	 * Returns the score of the given player position.
	 * @param playerPosition The player position whose score is requested.
	 * @return The score of the player with the given position, or -1 if the
	 * given position is not exist.
	 */
	public int getPlayerScore(int playerPosition) {
		if (playerPosition == PLAYER1_ID) {
			return player1Score;
		}
		else if (playerPosition == PLAYER2_ID) {
			return player2Score;
		}
		else {
			return -1;
		}
	}
	
	/**
	 * Runs the game for the given number of rounds.
	 * @param numberOfRounds Number of rounds of the competition.
	 */
	public void playMultipleRounds(int numberOfRounds) {
		
		String repTypeOfPlayer1 = player1.getTypeName();
		String repTypeOfPlayer2 = player2.getTypeName();
		
		System.out.println("Starting a Nim competition of " + numberOfRounds + " rounds between a " + 
				repTypeOfPlayer1 + " player and a " + repTypeOfPlayer2 + " player.");
		
		// Run number of given rounds
		for (int i = 0; i < numberOfRounds; i++) {
			round();
		}
		
	}
	
	/**
	 * This method runs one round of the game.
	 */
	private void round() {
		
		// Initiate new board game for the round
		Board board = new Board();
		
		if (displayMessage == true) {
			System.out.println("Welcome to the sticks game!");
		}
		
		// Always the first player playing is player 1
		currentPlayer = player1;
		
		while (board.getNumberOfUnmarkedSticks() > 0) {
			// While there are still sticks on board, make another turn and then change the current player.
			turn(board);
			changeCurrentPlayer();
		}
		
		// After the round ends, add 1 point to the winner score and print a message indicating who won.
		roundWinner();
		
	}
	
	/**
	 * This method is used to add 1 point to the winner of the last round, and print a message indicating
	 * the winner's id (if the show messages flag is true)
	 */
	private void roundWinner() {
		
		// The current player is the winner because after the loser took the last stick, the current player
		// changes and holds the winner. Add 1 point to the winner's score.
		if (currentPlayer.getPlayerId() == PLAYER1_ID) {
			player1Score++;
		}
		else {
			player2Score++;
		}
		
		if (displayMessage == true) {
			System.out.println("Player " + currentPlayer.getPlayerId() + " won!");
		}
	}
	
	/**
	 * This method changes the current player playing. If the last player was the first player, the current
	 * player will change to the second player and vice versa.
	 */
	private void changeCurrentPlayer() {
		if (currentPlayer.getPlayerId() == PLAYER1_ID) {
			currentPlayer = player2;
		}
		else {
			currentPlayer = player1;
		}
	}
	
	/**
	 * This method makes one turn of the current player playing.
	 * @param board The board game on the current state in the current round.
	 */
	private void turn(Board board) {
		
		if (displayMessage == true) {
			System.out.println("Player " + currentPlayer.getPlayerId() + ", it is now your turn!");
		}
		
		int moveAttempt = 0;
		Move currentMove = null;
		while (moveAttempt != 1) {
			// While there is no legal move, ask for a move and try to make the move on board.
			currentMove = currentPlayer.produceMove(board);
			moveAttempt = board.markStickSequence(currentMove);
			if (moveAttempt != 1 && displayMessage == true) {
				// If the move is illegal, and display message flag is true, print an error.
				System.out.println("Invalid move. Enter another:");
			}
		}
		
		if (displayMessage == true) {
			System.out.println("Player " + currentPlayer.getPlayerId() + "  made the move: " + currentMove);
		}
	}
	
	
	/*
	 * Returns the integer representing the type of the player; returns -1 on bad
	 * input.
	 */
	private static int parsePlayerType(String[] args,int index){
		try{
			return Integer.parseInt(args[index]);
		} catch (Exception E){
			return -1;
		}
	}
	
	/*
	 * Returns the integer representing the type of player 2; returns -1 on bad
	 * input.
	 */
	private static int parseNumberOfGames(String[] args){
		try{
			return Integer.parseInt(args[2]);
		} catch (Exception E){
			return -1;
		}
	}

	/**
	 * The method runs a Nim competition between two players according to the three user-specified arguments. 
	 * (1) The type of the first player, which is a positive integer between 1 and 4: 1 for a Random computer
	 *     player, 2 for a Heuristic computer player, 3 for a Smart computer player and 4 for a human player.
	 * (2) The type of the second player, which is a positive integer between 1 and 4.
	 * (3) The number of rounds to be played in the competition.
	 * @param args an array of string representations of the three input arguments, as detailed above.
	 */
	public static void main(String[] args) {
		
		// Take the players types and number of rounds from arguments
		int p1Type = parsePlayerType(args,0);
		int p2Type = parsePlayerType(args,1);
		int numGames = parseNumberOfGames(args);
		
		// Initiate a new Scanner object used for human player.
		Scanner scanner = new Scanner(System.in);
		
		// Initiate 2 new Player objects for each player with the given types, id = 1 for first player or
		// id = 2 for second player, and the scanner object.
		Player player1 = new Player(p1Type, PLAYER1_ID, scanner);
		Player player2 = new Player(p2Type, PLAYER2_ID, scanner);
		
		
		boolean displayMessage = false;
		if (p1Type == Player.HUMAN || p2Type == Player.HUMAN) {
			// If at least 1 player is human, set display messages flag as true.
			// Otherwise, leave it as false.
			displayMessage = true;
		}
		
		// Initiate a new Competition object with the 2 players object and the display messages flag.
		Competition competition = new Competition(player1, player2, displayMessage);
		
		// Play the given number of rounds given.
		competition.playMultipleRounds(numGames);
		
		// After all rounds played, print the final score of both players.
		System.out.println("The results are " + competition.getPlayerScore(player1.getPlayerId()) + ":" 
				+ competition.getPlayerScore(player2.getPlayerId()));
		
	}
	
}
