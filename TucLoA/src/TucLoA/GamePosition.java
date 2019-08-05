package TucLoA;

import java.util.Arrays;

public class GamePosition {
	private byte board[][];
	private byte turn;
	final byte BOARD_SIZE = 8;
	final byte WHITE = 0;
	final byte BLACK = 1;
	final byte EMPTY = 2;

	public GamePosition(GamePosition gamePosition){
		board = new byte[BOARD_SIZE][BOARD_SIZE];
		for(int i=0;i<BOARD_SIZE;i++){
			for(int j=0;j<BOARD_SIZE;j++){
				board[i][j]=gamePosition.getBoard()[i][j];
			}
		}
		this.turn=gamePosition.getTurn();
	}


	public GamePosition() {
		board = new byte[BOARD_SIZE][BOARD_SIZE];
	}
	
	public byte[][] getBoard() {
		return board;
	}
	
	public byte getTurn() {
		return turn;
	}
	
	public void setTurn(byte t) {
		turn = t;
	}
}
