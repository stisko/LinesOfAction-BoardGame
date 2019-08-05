package TucLoA;

import java.io.IOException;

import java.util.*;

public class Client {
	private Communication comm;
	private GamePosition gamePosition;
	private byte myColor;
	private Moves_and_Checks mac,mac2;
	
	private final byte NM_NEW_POSITION = 1;
	private final byte NM_COLOR_W = 2;
	private final byte NM_COLOR_B = 3;
	private final byte NM_REQUEST_MOVE = 4;
	private final byte NM_PREPARE_TO_RECEIVE_MOVE = 5;
	private final byte NM_REQUEST_NAME = 6;
	private final byte NM_QUIT = 7;

	private final int INF = 100;
	private final int DEPTH_SIZE = 5;

	private int depth;

	public Client(){
		try{
			comm = new Communication("127.0.0.1", 6001);
			gamePosition = new GamePosition();
			mac = new Moves_and_Checks();
			mac2= new Moves_and_Checks();
		}catch (IOException e) {
			System.out.println("No server found!!");
			System.exit(1);
		}
	}
	
	
	public void run() {
		int msg;
		
		try {
			while (true) {
				msg = comm.recvMsg();
				System.out.println("msg: "+msg);
				switch (msg) {
				case NM_REQUEST_NAME:
					comm.sendName("Konkostis");
					break;
					
				case NM_NEW_POSITION:
					comm.getPosition(gamePosition);
					mac.printPosition(gamePosition);
					break;
					
				case NM_COLOR_W:
					myColor = gamePosition.WHITE;
					break;
					
				case NM_COLOR_B:
					myColor = gamePosition.BLACK;
					break;
				
				case NM_PREPARE_TO_RECEIVE_MOVE:
					byte b[] = comm.getMove();
					mac.doMove(gamePosition, b);
					mac.printPosition(gamePosition);
					break;
					
				case NM_REQUEST_MOVE:
					byte myMove[] = new byte[4];
					int i,j,k,l;
					Random rand = new Random(System.currentTimeMillis());
					
					if( !mac.canMove( gamePosition, myColor ) )
					{
						myMove[ 0 ] = -1;		//null move
					}
					else
					{

						//System.err.println("-------"+getPlayersPieces(myColor).size()+"---------");
						myMove=findBestMove(gamePosition);
/**********************************************************/
						// random player - not the most efficient implementation
//						while( true )
//						{
//							i = rand.nextInt( gamePosition.BOARD_SIZE);
//							j = rand.nextInt( gamePosition.BOARD_SIZE);
//
//							if( gamePosition.getBoard()[ i ][ j ] == myColor )		//find a piece of ours
//							{
//
//								myMove[ 0 ] = (byte) i;		//piece we are going to move
//								myMove[ 1 ] = (byte) j;
//
//								int tempRand = rand.nextInt(4);
//								int pieces;
//
//								if( tempRand == 0 )
//								{
//									// horizontal
//									pieces = 0;
//									for( k = 0; k < gamePosition.BOARD_SIZE ; k++ )
//									{
//										if( gamePosition.getBoard()[i][k] == gamePosition.WHITE || gamePosition.getBoard()[i][k] == gamePosition.BLACK )
//											pieces++;
//									}
//
//									myMove[2] = (byte) i;
//									myMove[3] = (byte) (j+pieces);
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//
//									myMove[2] = (byte) i;
//									myMove[3] = (byte) (j-pieces);
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//								}
//								else if( tempRand == 1 )
//								{
//									// vertical
//									pieces = 0;
//									for( k = 0; k < gamePosition.BOARD_SIZE ; k++ )
//									{
//										if( gamePosition.getBoard()[k][j] == gamePosition.WHITE || gamePosition.getBoard()[k][j] == gamePosition.BLACK )
//											pieces++;
//									}
//
//									myMove[2] = (byte) (i+pieces);
//									myMove[3] = (byte) j;
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//
//									myMove[2] = (byte) (i-pieces);
//									myMove[3] = (byte) j;
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//								}
//								else if( tempRand == 2 )
//								{
//									// first diagonal
//									pieces = 0;
//									if( i > j )
//									{
//										k = i - j;
//										l = 0;
//									}
//									else
//									{
//										k = 0;
//										l = j - i;
//									}
//
//									for( ; k < gamePosition.BOARD_SIZE && l < gamePosition.BOARD_SIZE ; k++, l++ )
//									{
//										if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
//											pieces++;
//									}
//
//									myMove[2] = (byte) (i+pieces);
//									myMove[3] = (byte) (j+pieces);
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//
//									myMove[2] = (byte) (i-pieces);
//									myMove[3] = (byte) (j-pieces);
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//								}
//								else if( tempRand == 3 )
//								{
//									// second diagonal
//									pieces = 0;
//									if( i + j > (gamePosition.BOARD_SIZE - 1) )
//									{
//										k = gamePosition.BOARD_SIZE-1;
//										l = i + j - (gamePosition.BOARD_SIZE - 1);
//									}
//									else
//									{
//										k = i + j;
//										l = 0;
//									}
//
//									for( ; k >= 0 && l < gamePosition.BOARD_SIZE ; k--, l++ )
//									{
//										if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
//											pieces++;
//									}
//
//									myMove[2] = (byte) (i-pieces);
//									myMove[3] = (byte) (j+pieces);
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//
//									myMove[2] = (byte) (i+pieces);
//									myMove[3] = (byte) (j-pieces);
//
//									if( mac.isLegal( gamePosition, myMove ) )
//										break;
//								}
//							}
//
//						}
						
						
						
	// end of random
	/**********************************************************/

					}

					comm.sendMove( myMove );			//send our move
					mac.doMove( gamePosition, myMove );		//play our move on our position
					mac.printPosition( gamePosition );
					break;
					
				case NM_QUIT:
					System.out.println("The end!!!");
					System.exit(0);
					break;
					
				default:
					System.exit(1);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR : Network problem!!!");
			System.exit(1);
		}
	}

	public List<Piece> getPlayersPieces(byte color){
		List<Piece> pieceList = new LinkedList<Piece>();

		byte[][] board= gamePosition.getBoard();
		for(int x=0;x<gamePosition.BOARD_SIZE;x++){
			for(int y=0;y<gamePosition.BOARD_SIZE;y++){
				if(board[x][y]==color){
					pieceList.add(new Piece((byte) x,(byte) y,(byte) color,(byte) -100));
				}
			}
		}
		return pieceList;
	}


	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}



	public List<byte[]> findPossibleMoves(GamePosition gamePosition){
		byte currColor= gamePosition.getTurn();
		byte[] newMove;
		List<byte[]> moveList= new LinkedList<byte[]>();
		int pieces=0;
		int k,l;
		for(int i=0;i<gamePosition.BOARD_SIZE;i++){
			for(int j=0;j<gamePosition.BOARD_SIZE;j++){
				if(gamePosition.getBoard()[i][j]==currColor){

					pieces=0; //HORIZONTAL
					for(k=0;k>gamePosition.BOARD_SIZE;k++){
						if(gamePosition.getBoard()[i][k]==gamePosition.WHITE || gamePosition.getBoard()[i][k]==gamePosition.BLACK){
							pieces++;
						}
					}

					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte)j;
					newMove[2]=(byte)i;
					newMove[3]=(byte) (j+pieces);

					if(mac2.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}

					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte)j;
					newMove[2]=(byte)i;
					newMove[3]=(byte) (j-pieces);

					if(mac2.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}

					pieces=0; //VERTICAL
					for (k=0;k<gamePosition.BOARD_SIZE;k++){
						if(gamePosition.getBoard()[i][k]==gamePosition.WHITE || gamePosition.getBoard()[i][k]==gamePosition.BLACK){
							pieces++;
						}
					}

					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte)j;
					newMove[2]=(byte)(i+pieces);
					newMove[3]=(byte) j;

					if(mac2.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}

					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte)j;
					newMove[2]=(byte)(i-pieces);
					newMove[3]=(byte) j;

					if(mac.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}


					pieces=0; //FIRST DIAGONAL
					if( i > j ){
						k = i - j;
						l = 0;
					}else{
						k = 0;
						l = j - i;
					}

					for( ; k < gamePosition.BOARD_SIZE && l < gamePosition.BOARD_SIZE ; k++, l++ ){
						if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
							pieces++;
					}
					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte) j;
					newMove[2] = (byte) (i+pieces);
					newMove[3] = (byte) (j+pieces);

					if(mac2.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}

					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte) j;
					newMove[2] = (byte) (i-pieces);
					newMove[3] = (byte) (j-pieces);

					if(mac2.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}

					pieces=0; //SECOND DIAGONAL
					if( i + j > (gamePosition.BOARD_SIZE - 1) )
					{
						k = gamePosition.BOARD_SIZE-1;
						l = i + j - (gamePosition.BOARD_SIZE - 1);
					}
					else
					{
						k = i + j;
						l = 0;
					}

					for( ; k >= 0 && l < gamePosition.BOARD_SIZE ; k--, l++ )
					{
						if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
							pieces++;
					}

					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte) j;
					newMove[2] = (byte) (i-pieces);
					newMove[3] = (byte) (j+pieces);

					if(mac2.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}

					newMove = new byte[4];
					newMove[0]=(byte) i;
					newMove[1]=(byte) j;
					newMove[2] = (byte) (i+pieces);
					newMove[3] = (byte) (j-pieces);

					if(mac.isLegal(gamePosition,newMove)){
						moveList.add(newMove);
					}
				}
			}
		}
		return moveList;
	}

	public byte[] findBestMove(GamePosition gamePosition){
		List<byte[]> possibleMoves = findPossibleMoves(gamePosition);
		System.out.println("The moves are "+possibleMoves.size());
		GamePosition newPosition  = new GamePosition();
		newPosition= new GamePosition(gamePosition);
		//newPosition= gamePosition;
		byte[] bestMove= new byte[4];
		int bestValue= -INF;
		int value=0;

		if(!mac2.canMove(newPosition,myColor)){
			bestMove[0]=-1;
			return bestMove;
		}
		int cnt2=0;
		for(int cnt=0;cnt<possibleMoves.size();cnt++){
			System.out.println("Mpike sto possible move "+(cnt+1)+"/"+possibleMoves.size());
			mac2.doMove(newPosition,possibleMoves.get(cnt));
			//value= minimax(newPosition,DEPTH_SIZE -1 , false);
			value= minimax_with_AB_pruning(newPosition,DEPTH_SIZE-1,-INF,INF,false);
			if(value>bestValue){
				bestValue=value;
				bestMove=possibleMoves.get(cnt);
				cnt2++;
			}
			newPosition= new GamePosition(gamePosition);
		}
		System.err.println("----cnt2----"+cnt2+"-------");

		return bestMove;
	}
	//a= -INF
	//b= INF
	private int minimax_with_AB_pruning(GamePosition gamePosition, int depth,int a, int b, boolean isMaximize) {
		if(depth<0){
			return eval(gamePosition);
		}
		List<byte[]> possibleMoves= findPossibleMoves(gamePosition);
		byte[] currMove= new byte[4];
		GamePosition newPosition= new GamePosition(gamePosition);
		int value=0;

		if(isMaximize){

			for(int cnt=0;cnt<possibleMoves.size();cnt++){
				mac2.doMove(newPosition,possibleMoves.get(cnt));
				value=minimax_with_AB_pruning(newPosition,depth-1,a,b, false);
				a= Math.max(a,value);
				newPosition= new GamePosition(gamePosition);
				if(b<=a){
					//System.err.println("PRUNE BABY at depth "+depth);
					break;
				}
			}
			return a;
		}else{

			for(int cnt=0;cnt<possibleMoves.size();cnt++){
				mac2.doMove(newPosition,possibleMoves.get(cnt));
				value=minimax_with_AB_pruning(newPosition,depth-1,a,b, true);
				b= Math.min(b,value);
				newPosition= new GamePosition(gamePosition);
				if(b<=a){
					break;
				}
			}
			return b;
		}
	}


	private int minimax(GamePosition gamePosition, int depth, boolean isMaximize) {
		if(depth<0){
			return eval(gamePosition);
		}
		List<byte[]> possibleMoves= findPossibleMoves(gamePosition);
		byte[] currMove= new byte[4];
		GamePosition newPosition= new GamePosition(gamePosition);
		int bestValue=0;
		int value=0;

		if(isMaximize){
			bestValue=-INF;
			for(int cnt=0;cnt<possibleMoves.size();cnt++){
				mac2.doMove(newPosition,possibleMoves.get(cnt));
				value=minimax(newPosition,depth-1, false);
				bestValue= Math.max(bestValue,value);
				newPosition= new GamePosition(gamePosition);
			}
			return bestValue;
		}else{
			bestValue=INF;
			for(int cnt=0;cnt<possibleMoves.size();cnt++){
				mac2.doMove(newPosition,possibleMoves.get(cnt));
				value=minimax(newPosition,depth-1, true);
				bestValue= Math.min(bestValue,value);
				newPosition= new GamePosition(gamePosition);
			}
			return bestValue;
		}
	}


	private int eval(GamePosition gamePosition){
		float value=0;
		int myColor_cnt=0, notMyColor_cnt=0;
		List<byte[]> myColorPieces = new LinkedList<>();
		List<byte[]> notMyColorPieces= new LinkedList<>();

		for(int i=0;i<gamePosition.BOARD_SIZE;i++){
			for(int j=0;j<gamePosition.BOARD_SIZE;j++){
				if(gamePosition.getBoard()[i][j]==myColor){
					byte[] coords= new byte[2];
					coords[0]=(byte) i;
					coords[1]=(byte) j;
					myColorPieces.add(coords);
					myColor_cnt++;
				}else if(gamePosition.getBoard()[i][j]==Math.abs(1-myColor)){
					byte[] coords= new byte[2];
					coords[0]=(byte) i;
					coords[1]=(byte) j;
					notMyColorPieces.add(coords);
					notMyColor_cnt++;
				}
			}
		}

		value -= myColor_cnt-notMyColor_cnt;

		List<List<byte[]>> myColorConnectedPieces= new LinkedList<>();
		List<List<byte[]>> notMyColorConnectedPieces = new LinkedList<>();


		List<byte[]> myColorConnected = new LinkedList<>();
		List<byte[]> notMyColorConnected = new LinkedList<>();


		if(notMyColorPieces.isEmpty() || myColorPieces.isEmpty()){
			return (int) value;
		}
		myColorConnected.add(myColorPieces.remove(0));
		notMyColorConnected.add(notMyColorPieces.remove(0));

		myColorConnectedPieces.add(myColorConnected);
		notMyColorConnectedPieces.add(notMyColorConnected);



		byte[] piece= new byte[2];
		List<byte[]> l;
		boolean contained;

		for(int i=0;i<myColorPieces.size();i++){
			contained=false;
			piece=myColorPieces.get(i);


			for(int j=0;j<myColorConnectedPieces.size();j++){
				l=myColorConnectedPieces.get(j);
				for(int k=0;k<l.size();k++){
					if(adjacent(piece,l.get(k))){
						if(!l.contains(piece)){
							l.add(piece);
						}
					}
				}
			}


			for(int j=0;j<myColorConnectedPieces.size();j++){
				l=myColorConnectedPieces.get(j);
				if(l.contains(piece)){
					contained=true;
				}
			}

			if(!contained){
				myColorConnected= new LinkedList<>();
				myColorConnected.add(piece);
				myColorConnectedPieces.add(myColorConnected);
			}
		}

		//2125 line
		for(int i=0;i<notMyColorPieces.size();i++){
			contained=false;

			piece=notMyColorPieces.get(i);

			for(int j=0;j<notMyColorConnectedPieces.size();j++){
				l=notMyColorConnectedPieces.get(j);
				for(int k=0;k<l.size();k++){
					if(adjacent(piece,l.get(k))){
						if(!l.contains(piece)){
							l.add(piece);
						}
					}
				}
			}

			for(int j=0;j<notMyColorConnectedPieces.size();j++){
				l=notMyColorConnectedPieces.get(j);
				if(l.contains(piece)){
					contained=true;
				}
			}

			if(!contained){
				notMyColorConnected= new LinkedList<>();
				notMyColorConnected.add(piece);
				notMyColorConnectedPieces.add(notMyColorConnected);
			}
		}

		value -= myColorConnectedPieces.size()*3;

		value += notMyColorConnectedPieces.size()*10;

		float xDistFromCenter =0;
		float yDistFromCenter =0;
		float calcDistFromCenter=0;
		float maxDistFromCenterConnected=0;

		for(int i=0; i<myColorConnectedPieces.size();i++){
			l=myColorConnectedPieces.get(i);
			for(int j=0;j<l.size();j++){
				xDistFromCenter=Math.abs((float) l.get(j)[0] - (float) gamePosition.BOARD_SIZE/2);
				yDistFromCenter=Math.abs((float) l.get(j)[1] - (float) gamePosition.BOARD_SIZE/2);
				calcDistFromCenter= xDistFromCenter*xDistFromCenter + yDistFromCenter*yDistFromCenter;
				value -= calcDistFromCenter;
				maxDistFromCenterConnected= Math.max(maxDistFromCenterConnected,calcDistFromCenter);
			}

			value-= maxDistFromCenterConnected;
		}

		for(int i=0;i<notMyColorConnectedPieces.size();i++){
			l = notMyColorConnectedPieces.get(i);
			for(int j=0;j<l.size();j++){
				xDistFromCenter= Math.abs((float) l.get(j)[0] - (float) gamePosition.BOARD_SIZE/2);
				yDistFromCenter= Math.abs((float) l.get(j)[1] - (float) gamePosition.BOARD_SIZE/2);
				calcDistFromCenter= xDistFromCenter*xDistFromCenter + yDistFromCenter*yDistFromCenter;

				value+= calcDistFromCenter;
				maxDistFromCenterConnected= Math.max(maxDistFromCenterConnected,calcDistFromCenter);
			}

			value += maxDistFromCenterConnected;
		}

		float avgPositionX, avgPositionY;
		float tmp_avgPositionX, tmp_avgPositionY;

		float avgPositionXs[] = new float[myColorConnectedPieces.size()];
		float avgPositionYs[] = new float[myColorConnectedPieces.size()];

		for(int i=0;i<myColorConnectedPieces.size();i++){
			l=myColorConnectedPieces.get(i);
			avgPositionX= (float) l.get(0)[0];
			avgPositionY= (float) l.get(0)[1];
			for(int j=0;j<l.size()-1;++j){
				tmp_avgPositionX=(float) l.get(j)[0] + (float) l.get(j+1)[0]/2;
				tmp_avgPositionY=(float) l.get(j)[1] + (float) l.get(j+1)[1]/2;
				avgPositionX= (avgPositionY + tmp_avgPositionX)/2;
			}
			avgPositionXs[i]=avgPositionX;
			avgPositionYs[i]=avgPositionY;
		}

		for(int i=0;i<avgPositionXs.length;i++){
			for(int j=0;j<avgPositionXs.length;j++){
				if(i!=j){
					value += distance(avgPositionXs[i],avgPositionXs[j],avgPositionYs[i],avgPositionYs[j]);
				}
			}
		}


		if(value>=INF){
			value= INF -1;
		}else if(value<=-INF){
			value= -INF +1;
		}



		return (int) value;
	}

	// Return true if the two Pieces are adjacent to each other
	public boolean adjacent(byte[] piece1, byte[] piece2) {
		int x1=piece1[0];
		int y1=piece1[1];
		int x2=piece2[0];
		int y2=piece2[1];
		if ( (x1 != x2) && (x1+1 != x2) && (x1-1 != x2) ) {
			return false;
		}
		else if ( (y1 != y2) && (y1+1 != y2) && (y1-1 != y2) ) {
			return false;
		}

		return true;
	}

	private float distance(float x1,float x2, float y1, float y2){
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

}
