import java.util.Random;
import java.util.Scanner;

/**
 * The Player class represents a player in the Nim game, producing Moves as a response to a Board state. Each player 
 * is initialized with a type, either human or one of several computer strategies, which defines the move he 
 * produces when given a board in some state. The heuristic strategy of the player is already implemented. You are 
 * required to implement the rest of the player types according to the exercise description.
 * @author OOP course staff
 */
public class Player {

	//Constants that represent the different players.
	/** The constant integer representing the Random player type. */
	public static final int RANDOM = 1;
	/** The constant integer representing the Heuristic player type. */
	public static final int HEURISTIC = 2;
	/** The constant integer representing the Smart player type. */
	public static final int SMART = 3;
	/** The constant integer representing the Human player type. */
	public static final int HUMAN = 4;
	
	//Used by produceHeuristicMove() for binary representation of board rows.
	private static final int BINARY_LENGTH = 3;
	
	private final int playerType;
	private final int playerId;
	private Scanner scanner;
	
	/**
	 * Initializes a new player of the given type and the given id, and an initialized scanner.
	 * @param type The type of the player to create.
	 * @param id The id of the player (either 1 or 2).
	 * @param inputScanner The Scanner object through which to get user input
	 * for the Human player type. 
	 */
	public Player(int type, int id, Scanner inputScanner){		
		// Check for legal player type (we will see better ways to do this in the future).
		if (type != RANDOM && type != HEURISTIC 
				&& type != SMART && type != HUMAN){
			System.out.println("Received an unknown player type as a parameter"
					+ " in Player constructor. Terminating.");
			System.exit(-1);
		}		
		playerType = type;	
		playerId = id;
		scanner = inputScanner;
	}
	
	/**
	 * @return an integer matching the player type.
	 */	
	public int getPlayerType(){
		return playerType;
	}
	
	/**
	 * @return the players id number.
	 */	
	public int getPlayerId(){
		return playerId;
	}
	
	
	/**
	 * @return a String matching the player type.
	 */
	public String getTypeName(){
		switch(playerType){
			
			case RANDOM:
				return "Random";			    
	
			case SMART: 
				return "Smart";	
				
			case HEURISTIC:
				return "Heuristic";
				
			case HUMAN:			
				return "Human";
		}
		//Because we checked for legal player types in the
		//constructor, this line shouldn't be reachable.
		return "UnkownPlayerType";
	}
	
	/**
	 * This method encapsulates all the reasoning of the player about the game. The player is given the 
	 * board object, and is required to return his next move on the board. The choice of the move depends
	 * on the type of the player: a human player chooses his move manually; the random player should 
	 * return some random move; the Smart player can represent any reasonable strategy; the Heuristic 
	 * player uses a strong heuristic to choose a move. 
	 * @param board - a Board object representing the current state of the game.
	 * @return a Move object representing the move that the current player will play according to his strategy.
	 */
	public Move produceMove(Board board){
		
		switch(playerType){
		
			case RANDOM:
				return produceRandomMove(board);				
				    
			case SMART: 
				return produceSmartMove(board);
				
			case HEURISTIC:
				return produceHeuristicMove(board);
				
			case HUMAN:
				return produceHumanMove(board);

			//Because we checked for legal player types in the
			//constructor, this line shouldn't be reachable.
			default: 
				return null;			
		}
	}
	
	/*
	 * Produces a random move.
	 */
	private Move produceRandomMove(Board board){
		Random random = new Random();
		
		int numOfRows = board.getNumberOfRows();
		boolean stickUnmarked = false;
		int randRow = 0, rowLength = 0, randLeft = 0, randRight = 0;
		
		while (!stickUnmarked) {
			// First find a random stick until it is unmarked. Each time get a random row number, and then
			// get a random stick. If it is unmarked, finish the loop, otherwise try again.
			randRow = random.nextInt(numOfRows) + 1;
			rowLength = board.getRowLength(randRow);
			randLeft = random.nextInt(rowLength) + 1;
			stickUnmarked = board.isStickUnmarked(randRow, randLeft);
		}
		
		// Find a random stick which is right of the random left stick on the same row.
		randRight = random.nextInt(rowLength - randLeft + 1) + randLeft;
		for (int i = randLeft + 1; i <= randRight; i++) {
			// For each stick on the sequence between the random left and right, start from left + 1, if the
			// current is Marked, change the right to current - 1.
			if (!board.isStickUnmarked(randRow, i)) {
				randRight = i - 1;
			}
		}
		
		return new Move(randRow, randLeft, randRight);
		
	}
	
	/*
	 * Produce some intelligent strategy to produce a move
	 */
	private Move produceSmartMove(Board board){
		int numOfRows = board.getNumberOfRows();
		int numOfSequences = 0; // Counter for number of sequences of unmarked sticks
		int row = 0, left = 0, right = 0;
		
		// First count how many sequences of unmarked sticks are exist currently on board
		for (int i = 1; i <= numOfRows; i++) {
			int rowLength = board.getRowLength(i);
			for (int j = 1; j <= rowLength; j++) {
				if (board.isStickUnmarked(i, j) && !board.isStickUnmarked(i, j-1)) {
					// If the current stick on row is unmarked and the one before is marked, or out of board,
					// then it is a new sequence of sticks
					if (numOfSequences == 0) {
						// Keep the first sequence row, left stick on sequence, and find the right stick on
						// sequence.
						row = i;
						left = j;
						right = j;
						while (board.isStickUnmarked(row, right+1)) {
							// If the next stick is also unmarked, change the right stick to that one
							right++;
						}
					}
					numOfSequences++;
				}
			}
		}
		
		Move move;
		
		if (numOfSequences %2 != 0) {
			// If the number of sequences on board is odd, mark 1 stick from the first sequence found
			move = new Move(row, left, left);
		}
		else {
			// If the number of sticks is even, mark the whole sequence found
			move = new Move(row, left, right);
		}
		
		return move;
	}	
	
	/*
	 * Interact with the user to produce his move.
	 */
	private Move produceHumanMove(Board board){
		int userInput = 0;
		while (userInput != 2) {
			System.out.println("Press 1 to display the board. Press 2 to make a move:");
			userInput = scanner.nextInt();
			if (userInput == 1) {
				System.out.println(board);
			}
			else if (userInput != 2) {
				System.out.println("Unknown input.");
			}
		}
		
		System.out.println("Enter the row number:");
		int rowNum = scanner.nextInt();
		System.out.println("Enter the index of the leftmost stick:");
		int left = scanner.nextInt();
		System.out.println("Enter the index of the rightmost stick:");
		int right = scanner.nextInt();
		
		return new Move(rowNum, left, right);
	}
	
	/*
	 * Uses a winning heuristic for the Nim game to produce a move.
	 */
	private Move produceHeuristicMove(Board board){

		if(board == null){
			return null;
		}
	
		int numRows = board.getNumberOfRows();
		int[][] bins = new int[numRows][BINARY_LENGTH];
		int[] binarySum = new int[BINARY_LENGTH];
		int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,lastSize=0,lastOneRow=0,lastOneLeft=0;
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
			binarySum[bitIndex] = 0;
		}
		
		for(int k=0;k<numRows;k++){
			
			int curRowLength = board.getRowLength(k+1);
			int i = 0;
			int numOnes = 0;
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				bins[k][bitIndex] = 0;
			}
			
			do {
				if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
					numOnes++;
				} else {
					
					if(numOnes>0){
						
						String curNum = Integer.toBinaryString(numOnes);
						while(curNum.length()<BINARY_LENGTH){
							curNum = "0" + curNum;
						}
						for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
							bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; //Convert from char to int
						}
						
						if(numOnes>1){
							higherThenOne++;
							lastRow = k +1;
							lastLeft = i - numOnes + 1;
							lastSize = numOnes;
						} else {
							totalOnes++;
						}
						lastOneRow = k+1;
						lastOneLeft = i;
						
						numOnes = 0;
					}
				}
				i++;
			}while(i<=curRowLength);
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
			}
		}
		
		
		//We only have single sticks
		if(higherThenOne==0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//We are at a finishing state				
		if(higherThenOne<=1){
			
			if(totalOnes == 0){
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
			} else {
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-(1-totalOnes%2));
			}
			
		}
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
			
			if(binarySum[bitIndex]>0){
				
				int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
				for(int k=0;k<numRows;k++){
					
					if(bins[k][bitIndex]>0){
						eraseRow = k+1;
						eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
						
						for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
							
							if(binarySum[b2]>0){
								
								if(bins[k][b2]==0){
									finalSum = finalSum + (int)Math.pow(2,BINARY_LENGTH-b2-1);
								} else {
									finalSum = finalSum - (int)Math.pow(2,BINARY_LENGTH-b2-1);
								}
								
							}
							
						}
						break;
					}
				}
				
				numRemove = eraseSize - finalSum;
				
				//Now we find that part and remove from it the required piece
				int numOnes=0,i=0;
				//while(numOnes<eraseSize){
				while(numOnes<numRemove && i<board.getRowLength(eraseRow)){

					if(board.isStickUnmarked(eraseRow,i+1)){
						numOnes++;
					} else {
						numOnes=0;
					}
					i++;
					
				}
				
				//This is the case that we cannot perform a smart move because there are marked
				//Sticks in the middle
				if(numOnes == numRemove){
					return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
				} else {
					return new Move(lastRow,lastLeft,lastLeft);
				}
				
			}
		}
		
		//If we reached here, and the board is not symmetric, then we only need to erase a single stick
		if(binarySum[BINARY_LENGTH-1]>0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//If we reached here, it means that the board is already symmetric,
		//and then we simply mark one stick from the last sequence we saw:
		return new Move(lastRow,lastLeft,lastLeft);		
	}
	
	
}

/* int numOfRows = board.getNumberOfRows();
int left = 0;
int right = 0;
int row = 0;
for (int i = 1; i <= numOfRows; i++) {
	for (int j = 1; j < board.getRowLength(i); j++) {
		if (board.isStickUnmarked(i, j)) {
			left = j;
			row = i;
			break;
		}
	}
	if (left != 0) {
		break;
	}
}

if (left != 0) {
	right = left;

	for (int i = left + 1; i < board.getRowLength(row); i++) {
		if (!board.isStickUnmarked(row, i)) {
			break;
		}
		right = i;
	}
}
else {
	for (int i = 1; i <= numOfRows; i++) {
		if (board.isStickUnmarked(i, board.getRowLength(i))) {
			left = board.getRowLength(i);
			right = left;
			row = i;
			break;
		}
	}
}

return new Move(row, left, right); */
