package com.thombs.ChessWeb.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/* ---Notes---
 * The coordinates of the chess pieces are from normal array representation(0,0 being the top left of the board)
 * rather than what a chess board is normal read as(a/0,0 being the bottom left	) 
 * 
 * 
 */

public class ChessBoard {
	private ChessPiece[] board;
	private ArrayList<ChessPiece> moveList;
	private Side turn;
	private boolean gameOver;
	private String player1;
	private String player2;
	private String playerWhite;
	private String playerBlack;
	private String playerTurn;
	private int aiLevel;
	
	public ChessBoard()
	{
		board = new ChessPiece[32];
		moveList = new ArrayList<ChessPiece>();
		turn = Side.WHITE;
		gameOver = false;
		initBoard();
	}
	
	public ChessBoard(String player1, String player2)
	{
		board = new ChessPiece[32];
		moveList = new ArrayList<ChessPiece>();
		turn = Side.WHITE;
		gameOver = false;
		initBoard();
		
		this.player1 = player1;
		this.player2 = player2;
		
		setRandomPlayerColors();
	}
	
	public ChessBoard(String player1, int level)
	{
		board = new ChessPiece[32];
		moveList = new ArrayList<ChessPiece>();
		turn = Side.WHITE;
		gameOver = false;
		initBoard();
		
		this.player1 = player1;
		this.player2 = "AI";
		this.aiLevel = level;
		
		setRandomPlayerColors();
	}
	
	private void setRandomPlayerColors(){
		Random r = new Random();
		if(r.nextInt(2) == 0){
			this.playerTurn = this.player1;
			this.playerWhite = this.player1;
			this.playerBlack = this.player2;
		}else{
			this.playerTurn = this.player2;
			this.playerWhite = this.player2;
			this.playerBlack = this.player1;
		}
	}
	
	public void setAiLevel(int level){
		aiLevel = level;
	}
	
	public int getAiLevel(){
		return aiLevel;
	}
	
	public String getPlayerWhite(){
		return this.playerWhite;
	}
	
	public void setPlayerWhite(String username){
		this.playerWhite = username;
	}
	
	public String getPlayerBlack(){
		return this.playerBlack;
	}
	
	public void setPlayerBlack(String username){
		this.playerBlack = username;
	}
	
	public String getPlayerTurn(){
		return this.playerTurn;
	}
	
	public String getPlayer1(){
		return this.player1;
	}
	
	public void setPlayer1(String username){
		this.player1 = username;
	}
	
	public String getPlayer2(){
		return this.player2;
	}
	
	public void setPlayer2(String username){
		this.player2 = username;
	}
	
	public ArrayList<ChessPiece> getMoveList()
	{
		return moveList;
	}
	
	public Side getTurn()
	{
		return turn;
	}
	
	public ChessBoard CopyChessBoard()
	{
		ChessBoard b = new ChessBoard();
		
		//set new attributes according to this board
		//board array
		ChessPiece array[] = new ChessPiece[32];
		for(int i = 0; i < 32; i++)
		{
			array[i] = makeCopy(this.getPiece(i));
		}
		b.setBoard(array);
		
		//move list
		b.setMoveList(new ArrayList<ChessPiece>(this.moveList));
		
		//current turn
		b.setTurn(this.turn);
		
		//game over status
		b.setGameOver(this.gameOver);
		
		
		return b;
	}
	
	
	public ChessPiece[] getBoard() {
		return board;
	}

	public void setBoard(ChessPiece[] board) {
		this.board = board;
	}

	public void setMoveList(ArrayList<ChessPiece> moveList) {
		this.moveList = moveList;
	}

	public void setTurn(Side turn) {
		this.turn = turn;
	}
	
	public boolean isGameOver()
	{
		return gameOver;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public ChessPiece getPiece(int id)
	{
		return board[id];
	}
	//Initialize the chess board with pieces in their starting places	
	private void initBoard()
	{
		//ChessPiece(id, PieceType, Side, row, col)
		
		//Place back row black side(top of board)
		board[0] = (new ChessPiece(0, PieceType.ROOK, Side.BLACK, 0, 0));
		board[1] = (new ChessPiece(1, PieceType.KNIGHT, Side.BLACK, 0, 1));
		board[2] = (new ChessPiece(2, PieceType.BISHOP, Side.BLACK, 0, 2));
		board[3] = (new ChessPiece(3, PieceType.QUEEN, Side.BLACK, 0, 3));
		board[4] = (new ChessPiece(4, PieceType.KING, Side.BLACK, 0, 4));
		board[5] = (new ChessPiece(5, PieceType.BISHOP, Side.BLACK, 0, 5));
		board[6] = (new ChessPiece(6, PieceType.KNIGHT, Side.BLACK, 0, 6));
		board[7] = (new ChessPiece(7, PieceType.ROOK, Side.BLACK, 0, 7));
		
		//Place front row black side
		board[8] = (new ChessPiece(8, PieceType.PAWN, Side.BLACK, 1, 0));
		board[9] = (new ChessPiece(9, PieceType.PAWN, Side.BLACK, 1, 1));
		board[10] = (new ChessPiece(10, PieceType.PAWN, Side.BLACK, 1, 2));
		board[11] = (new ChessPiece(11, PieceType.PAWN, Side.BLACK, 1, 3));
		board[12] = (new ChessPiece(12, PieceType.PAWN, Side.BLACK, 1, 4));
		board[13] = (new ChessPiece(13, PieceType.PAWN, Side.BLACK, 1, 5));
		board[14] = (new ChessPiece(14, PieceType.PAWN, Side.BLACK, 1, 6));
		board[15] = (new ChessPiece(15, PieceType.PAWN, Side.BLACK, 1, 7));
		
		//Place front row white side
		board[16] = (new ChessPiece(16, PieceType.PAWN, Side.WHITE, 6, 0));
		board[17] = (new ChessPiece(17, PieceType.PAWN, Side.WHITE, 6, 1));
		board[18] = (new ChessPiece(18, PieceType.PAWN, Side.WHITE, 6, 2));
		board[19] = (new ChessPiece(19, PieceType.PAWN, Side.WHITE, 6, 3));
		board[20] = (new ChessPiece(20, PieceType.PAWN, Side.WHITE, 6, 4));
		board[21] = (new ChessPiece(21, PieceType.PAWN, Side.WHITE, 6, 5));
		board[22] = (new ChessPiece(22, PieceType.PAWN, Side.WHITE, 6, 6));
		board[23] = (new ChessPiece(23, PieceType.PAWN, Side.WHITE, 6, 7));
		
		//Place back row white side
		board[24] = (new ChessPiece(24, PieceType.ROOK, Side.WHITE, 7, 0));
		board[25] = (new ChessPiece(25, PieceType.KNIGHT, Side.WHITE, 7, 1));
		board[26] = (new ChessPiece(26, PieceType.BISHOP, Side.WHITE, 7, 2));
		board[27] = (new ChessPiece(27, PieceType.QUEEN, Side.WHITE, 7, 3));
		board[28] = (new ChessPiece(28, PieceType.KING, Side.WHITE, 7, 4));
		board[29] = (new ChessPiece(29, PieceType.BISHOP, Side.WHITE, 7, 5));
		board[30] = (new ChessPiece(30, PieceType.KNIGHT, Side.WHITE, 7, 6));
		board[31] = (new ChessPiece(31, PieceType.ROOK, Side.WHITE, 7, 7));
	}
	
	
	
	
	//Move a piece by taking in the ID of the piece being moved and the row/col of the tile it is being moved to.
	//@RETURNS TRUE if the move passes all validation tests.
	public boolean receiveMove(int id, int toRow, int toCol)
	{
		ChessPiece p = null;
		
		//find piece that is being moved
		//NOTE: a new ChessPiece is created due to the fact that setting p = board[id] and subsequently changing p's properties will effect the board piece.
		p = new ChessPiece(id, board[id].getType(), board[id].getSide(), 0,0);
		//System.out.println("RECEIVE MOVE - Original Piece: "+ p.getType() + " " + p.getRow() + " " + p.getCol());
		//If the player moved a valid piece then send it to the move method for additional validity checks
		//This also makes sure the player moves their own piece
		
		if(getPossibleMoves(turn).size() == 0)
		{
			System.out.println("CHECKMATE!");
			gameOver = true;
		}
		if(p != null && (p.getSide() == turn) && !gameOver)
		{
			p.setRow(toRow);
			p.setCol(toCol);
			//System.out.println("RECEIVE MOVE - Moved Piece: " + p.getType() + " " + p.getRow() + " " + p.getCol());
			//check all possible moves from the moved piece. If it was valid then it returns TRUE.
			//System.out.println("RECEIVE MOVE - board piece: " + board[id].toString());
			
			
			ChessPiece c = isOccupied2(toRow, toCol);
			if(move(p, true))
			{
				//System.out.println("---START THREATENED CHECK---");
				//System.out.println(turn.toString() + " is in check: " + checkCheck());
				//System.out.println("---END THREATENED CHECK---");
				moveList.add(p);
				return true;
			}
			else
				return false;
		}
		else
		{
			return false;
		}
			
	}
	
	//Make a copy of a given piece
	private ChessPiece makeCopy(ChessPiece c)
	{
		return new ChessPiece(c.getID(), c.getType(),c.getSide(), c.getRow(), c.getCol());
	}
	
	//Terrible name, must rename it. This method looks for the Check state.
	//@RETURNS TRUE if the side currently going is in CHECK.
	private boolean checkCheck(Side side)
	{
		int row, col;
		if(side == Side.BLACK)
		{
			row = board[4].getRow();
			col = board[4].getCol();
			return isThreatened(row, col, Side.BLACK);
		}
		else
		{
			row = board[28].getRow();
			col = board[28].getCol();
			return isThreatened(row, col, Side.WHITE);
		}
		
	}
	
	//Check if a tile is threatened
	//Mock a move to the tile of the given coordinates from the parameters.
	//@RETURNS TRUE if the space is threatened by any enemy piece.
	private boolean isThreatened(int row, int col, Side s)
	{
		//create a dummy piece to check if it is threatened by any piece on the opposing side
		ChessPiece dummy = new ChessPiece(-1, PieceType.KING, Side.BLACK, row, col);
		

			for(ChessPiece p : board)
			{
				//skip this piece if its the same side as the turn
				if(p.getSide() == s || (p.getRow() == row && p.getCol() == col) || p.isCaptured())
					continue;
				
				dummy.setID(p.getID());
				dummy.setType(p.getType());
				dummy.setSide(p.getSide());
				dummy.setRow(row);
				dummy.setCol(col);
				
				if(p.getType() == PieceType.PAWN)
				{
					if(p.getSide() == Side.BLACK)
					{
						if(p.getRow() + 1 == row && (p.getCol() + 1 == col || p.getCol() - 1 == col))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}
					else
					{
						if(p.getRow() - 1 == row && (p.getCol() + 1 == col || p.getCol() - 1 == col))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}
				}
				else if(p.getType() == PieceType.ROOK)
				{
					if(p.getCol() == col)
					{
						if(!checkVerticalMovement(dummy))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}
					else if(p.getRow() == row)
					{
						if(!checkHorizontalMovement(dummy))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}
				}
				else if(p.getType() == PieceType.BISHOP)
				{
					if(row - col == p.getRow() - p.getCol() || row + col == p.getRow() + p.getCol())
					{
						if(!checkDiagonalMovement(dummy))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}
				}
				else if(p.getType() == PieceType.KNIGHT)
				{
					if(checkKnightMovement(dummy))
					{
						//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
						return true;
					}
				}
				else if(p.getType() == PieceType.QUEEN)
				{
					//Queen horizontal move
					if(row == p.getRow())
					{
						if(!checkHorizontalMovement(dummy))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}				
					//QUEEN vertical move
					else if(col == p.getCol())
					{
						if(!checkVerticalMovement(dummy))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}
					//Queen diagonal move
					else if(row - col == p.getRow() - p.getCol() || row + col == p.getRow() + p.getCol())
					{
						if(!checkDiagonalMovement(dummy))
						{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
						}
					}
				}//end of QUEEN check
				else if(p.getType() == PieceType.KING)
				{
					if(row == p.getRow() && col + 1 == p.getCol() ||
					   row == p.getRow() && col - 1 == p.getCol() ||
					   row + 1 == p.getRow() && col == p.getCol() ||
					   row - 1 == p.getRow() && col == p.getCol() ||
					   row + 1 == p.getRow() && col + 1 == p.getCol() ||
					   row + 1 == p.getRow() && col - 1 == p.getCol() ||
					   row - 1 == p.getRow() && col + 1 == p.getCol() ||
					   row - 1 == p.getRow() && col - 1 == p.getCol())
					{
							//System.out.println(p.getSide().toString() + " " + p.getType().toString() + " threatens.");
							return true;
					}
				}
			}
			
			//If none of the pieces can hit that tile, return FALSE
			return false;
	}
	
	//Check if a spot on the board is occupied
	//@RETURN TRUE if the tile is occupied
	private boolean isOccupied(int r, int c)
	{	
		for(ChessPiece p : board)
		{
			if((p.getRow() == r && p.getCol() == c) && !p.isCaptured())
				return true;
		}
		return false;
	}
	
	//Check if a spot on the board is occupied using
	//@RETURN the ChessPiece that occupies the tile - can be NULL
	private ChessPiece isOccupied2(int r, int c)
	{
		for(ChessPiece p : board)
		{
			if((p.getRow() == r && p.getCol() == c) && !p.isCaptured())
				return p;
		}
		
		return null;
	}
	
	//Check for a pawn reaching the opposite end of the board and promote it to a queen.
	private void pawnPromotion(ChessPiece c)
	{
		if(turn == Side.WHITE)
		{
			if(c.getRow() == 0 && c.getType() == PieceType.PAWN)
			{
				c.setType(PieceType.QUEEN);
			}
		}
		else
		{
			if(c.getRow() == 7 && c.getType() == PieceType.PAWN)
			{
				c.setType(PieceType.QUEEN);
			}
		}
	}
	
	//Get all possible moves that the current player/side can make.
	//@RETURNS a list of good moves that do not leave the player in check
	public ArrayList<ChessPiece> getPossibleMoves(Side s)
	{
		ArrayList<ChessPiece> goodMoves = new ArrayList<ChessPiece>();
		
		for(ChessPiece c : board)
		{
			if(c.getSide() == s && !c.isCaptured())
			{
				goodMoves.addAll(checkPossibleMovements(c));
			}
		}
		
		return goodMoves;
	}
	
	//If the opposing side does not have a valid move then the game is over and checkmate is declared
	public boolean isCheckmate(){
		boolean checkmate = false;
		
		if(turn.equals(Side.BLACK)){
			int moves = this.getPossibleMoves(Side.WHITE).size();
			if(moves == 0){
				checkmate = true;
			}
		}else{
			int moves = this.getPossibleMoves(Side.BLACK).size();
			if(moves == 0){
				checkmate = true;
			}
		}
		
		return checkmate;
	}
	
	//This method will fill a list with valid moves of a given piece
	public ArrayList<ChessPiece> checkPossibleMovements(ChessPiece c)
	{
		ChessPiece dummy = makeCopy(c);
		ChessPiece moveCheck = makeCopy(c);
		ArrayList<ChessPiece> validMoves = new ArrayList<ChessPiece>();
		
		//references suck! ChessPiece attributes cannot be changed or the copy in the list will change as well
		if(c.getType() == PieceType.PAWN)
		{
			if(c.getSide() == Side.WHITE)
			{
				//Check forward movement 2 tiles
				moveCheck.setRow(dummy.getRow() - 2);
				if(move(moveCheck, false))
				{
					//System.out.println("double pawn move");
					//System.out.println(moveCheck.toString());
					validMoves.add(makeCopy(moveCheck));
					//System.out.println(validMoves.get(0).toString());
				}
				
				//Check forward movement 1 tile
				moveCheck.setRow(dummy.getRow() - 1);
				if(move(moveCheck, false))
				{
					//System.out.println("single pawn move");
					//System.out.println(moveCheck.toString());
					validMoves.add(makeCopy(moveCheck));
					//System.out.println(validMoves.get(0).toString());
				}

				
				//check attack diagonal left
				moveCheck.setCol(dummy.getCol() - 1);
				if(move(moveCheck, false))
				{
					//System.out.println("pawn attack left");
					validMoves.add(makeCopy(moveCheck));
				}
				
				//check attack diagonal right
				moveCheck.setCol(dummy.getCol() + 1);
				if(move(moveCheck, false))
				{
					//System.out.println("pawn attack right");
					validMoves.add(makeCopy(moveCheck));
				}
				
				
			}
			else
			{
				//Check forward movement 2 tiles
				moveCheck.setRow(dummy.getRow() + 2);
				if(move(moveCheck, false))
					validMoves.add(makeCopy(moveCheck));
				
				//Check forward movement 1 tile
				moveCheck.setRow(dummy.getRow() + 1);
				if(move(moveCheck, false))
					validMoves.add(makeCopy(moveCheck));

				
				//check attack diagonal left
				moveCheck.setCol(dummy.getCol() - 1);
				if(move(moveCheck, false))
					validMoves.add(makeCopy(moveCheck));
				
				//check attack diagonal right
				moveCheck.setCol(dummy.getCol() + 1);
				if(move(moveCheck, false))
					validMoves.add(makeCopy(moveCheck));
			}
			
		}
		else if(c.getType() == PieceType.ROOK)
		{
			validMoves.addAll(getVerticalMoves(c));
			validMoves.addAll(getHorizontalMoves(c));
		}
		else if(c.getType() == PieceType.BISHOP)
		{
			validMoves.addAll(getDiagonalMoves(c));
		}
		else if(c.getType() == PieceType.KNIGHT)
		{
			moveCheck.setRow(dummy.getRow() - 2);
			moveCheck.setCol(dummy.getCol() - 1);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() - 1);
			moveCheck.setCol(dummy.getCol() - 2);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() + 1);
			moveCheck.setCol(dummy.getCol() - 2);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() + 2);
			moveCheck.setCol(dummy.getCol() - 1);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() + 2);
			moveCheck.setCol(dummy.getCol() + 1);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() + 1);
			moveCheck.setCol(dummy.getCol() + 2);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() - 1);
			moveCheck.setCol(dummy.getCol() + 2);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() - 2);
			moveCheck.setCol(dummy.getCol() + 1);
			if((moveCheck.getRow() >= 0 && moveCheck.getRow() <=7) && (moveCheck.getCol() >= 0 && moveCheck.getCol() <=7) && move(moveCheck,false))
				validMoves.add(makeCopy(moveCheck));
			
			//System.out.println("POSSIBLE KNIGHT MOVES: " + validMoves.size());
		}
		else if(c.getType() == PieceType.QUEEN)
		{
			validMoves.addAll(getVerticalMoves(c));
			validMoves.addAll(getHorizontalMoves(c));
			validMoves.addAll(getDiagonalMoves(c));
		}
		else if(c.getType() == PieceType.KING)
		{
			moveCheck.setRow(dummy.getRow() + 1);
			moveCheck.setCol(dummy.getCol());
			if(moveCheck.getRow() <= 7 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() + 1);
			moveCheck.setCol(dummy.getCol() + 1);
			if(moveCheck.getRow() <= 7 && moveCheck.getCol() <= 7 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() - 1);
			moveCheck.setCol(dummy.getCol());
			if(moveCheck.getRow() >= 0 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow());
			moveCheck.setCol(dummy.getCol() + 1);
			if(moveCheck.getCol() <= 7 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow());
			moveCheck.setCol(dummy.getCol() - 1);
			if(moveCheck.getCol() >= 0 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() - 1);
			moveCheck.setCol(dummy.getCol() - 1);
			if(moveCheck.getRow() >= 0 && moveCheck.getCol() >= 0 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			
			moveCheck.setRow(dummy.getRow() + 1);
			moveCheck.setCol(dummy.getCol() - 1);
			if(moveCheck.getRow() <= 7 && moveCheck.getCol() >= 0 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			moveCheck.setRow(dummy.getRow() - 1);
			moveCheck.setCol(dummy.getCol() + 1);
			if(moveCheck.getRow() >= 0 && moveCheck.getCol() <= 7 && move(moveCheck, false))
				validMoves.add(makeCopy(moveCheck));
			
			//System.out.println("POSSIBLE KING MOVES: " + validMoves.size());
		}
		
		
		return validMoves;
	}
	
	//Check specific tiles a knight can move to
	//@RETURN - TRUE if the move is valid
	private boolean checkKnightMovement(ChessPiece c)
	{
		boolean goodMove = false;
		ChessPiece p = null;
		
		if(board[c.getID()].getRow() - 2 == c.getRow() && board[c.getID()].getCol() - 1 == c.getCol())
		{
			goodMove = true;
			//System.out.println("1");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else if(board[c.getID()].getRow() - 1 == c.getRow() && board[c.getID()].getCol() - 2 == c.getCol())
		{
			goodMove = true;
			//System.out.println("2");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else if(board[c.getID()].getRow() + 1 == c.getRow() && board[c.getID()].getCol() - 2 == c.getCol())
		{
			goodMove = true;
			//System.out.println("3");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else if(board[c.getID()].getRow() + 2 == c.getRow() && board[c.getID()].getCol() - 1 == c.getCol())
		{
			goodMove = true;
			//System.out.println("4");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else if(board[c.getID()].getRow() + 2 == c.getRow() && board[c.getID()].getCol() + 1 == c.getCol())
		{
			goodMove = true;
			//System.out.println("5");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else if(board[c.getID()].getRow() + 1 == c.getRow() && board[c.getID()].getCol() + 2 == c.getCol())
		{
			goodMove = true;
			//System.out.println("6");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else if(board[c.getID()].getRow() - 1 == c.getRow() && board[c.getID()].getCol() + 2 == c.getCol())
		{
			goodMove = true;
			//System.out.println("7");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else if(board[c.getID()].getRow() - 2 == c.getRow() && board[c.getID()].getCol() + 1 == c.getCol())
		{
			goodMove = true;
			//System.out.println("8");
			p = isOccupied2(c.getRow(), c.getCol());
		}
		else
		{
			//System.out.println("This is what I hit in the knight movement.");
			//return false;
		}
		
		
		if((p == null || p.getSide() != c.getSide()) && goodMove)
		{
			//System.out.println("good KNIGHT");
			return true;
		}
		else
		{
			//System.out.println("bad KNIGHT");
			return false;
		}
	}
	
	/*
	 * Checks both diagonal directions from the given piece to the desired tile.
	 * If a piece is found between the starting location and the ending location then TRUE is returned.
	 * @PARAMS: The ChessPiece object that is the desired move.
	 * @RETURNS: TRUE if an obstruction is found between the starting tile and ending tile (ending tile excluded).
	 */
	private boolean checkDiagonalMovement(ChessPiece c)
	{
		int row;
		//Check diagonal movement
		//Check if any tiles are occupied between the current position and the new position.
		//If any pieces are found, the move will fail.
		boolean intercepted = false;
		

		//check every space in between the current column and the new location
		//Check lower right diag
		if(c.getCol() > board[c.getID()].getCol() && c.getRow() > board[c.getID()].getRow())
		{
			row = board[c.getID()].getRow() + 1;
			
			for(int i = board[c.getID()].getCol() + 1; i < c.getCol(); i++)
			{
				if(isOccupied(row, i))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					intercepted = true;
					break;
				}
				row++;
			}
			
		}
		//check upper right diag
		else if(c.getCol() > board[c.getID()].getCol() && c.getRow() < board[c.getID()].getRow())
		{
			row = board[c.getID()].getRow() - 1;
			for(int i = board[c.getID()].getCol() + 1; i < c.getCol(); i++)
			{
				if(isOccupied(row, i))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					intercepted = true;
					break;
				}
				row--;
			}
		}
		//Check upper left diag
		else if(c.getCol() < board[c.getID()].getCol() && c.getRow() < board[c.getID()].getRow())
		{
			row = board[c.getID()].getRow() - 1;
			for(int i = board[c.getID()].getCol() - 1; i > c.getCol(); i--)
			{
				if(isOccupied(row, i))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					//NOTE: possibly allow this if the first piece met is an opposing piece.
					intercepted = true;
					break;
				}
				row--;
			}
		}
		//check lower left
		else if(c.getCol() < board[c.getID()].getCol() && c.getRow() > board[c.getID()].getRow())
		{
			row = board[c.getID()].getRow() + 1;
			
			for(int i = board[c.getID()].getCol() - 1; i > c.getCol(); i--)
			{
				if(isOccupied(row, i))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					intercepted = true;
					break;
				}
				row++;
			}
		}
		
		return intercepted;
	}
	
	private ArrayList<ChessPiece> getDiagonalMoves(ChessPiece c)
	{
		ArrayList<ChessPiece> validMoves = new ArrayList<ChessPiece>();
		ChessPiece dummy = makeCopy(c);
		int row;


		row = board[c.getID()].getRow() + 1;
		//check lower right
		for(int i = board[c.getID()].getCol() + 1; i <= 7; i++)
		{
			if(row > 7 || row < 0)
				break;
			dummy.setRow(row);
			dummy.setCol(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
			else
				break;
			row++;
			//System.out.println("added DIAG LOWER RIGHT");
		}
		

		//check upper right diag
		row = board[c.getID()].getRow() - 1;
		for(int i = board[c.getID()].getCol() + 1; i <= 7; i++)
		{
			if(row > 7 || row < 0)
				break;
			dummy.setRow(row);
			dummy.setCol(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
			else
				break;
			row--;
			//System.out.println("added DIAG UPPER RIGHT");
		}

		//Check upper left diag
		row = board[c.getID()].getRow() - 1;
		for(int i = board[c.getID()].getCol() - 1; i >= 0; i--)
		{
			if(row > 7 || row < 0)
				break;
			dummy.setRow(row);
			dummy.setCol(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
			else
				break;
			row--;
			//System.out.println("added DIAG UPPER LEFT");
		}

	
		//check lower left
		row = board[c.getID()].getRow() + 1;
		for(int i = board[c.getID()].getCol() - 1; i >= 0; i--)
		{
			if(row > 7 || row < 0)
				break;
			dummy.setRow(row);
			dummy.setCol(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
			else
				break;
			row++;
			//System.out.println("added DIAG LOWER LEFT");
		}
		
		//System.out.println("POSSIBLE DIAGONAL MOVES: " + validMoves.size());
		return validMoves;
	}
	
	private ArrayList<ChessPiece> getHorizontalMoves(ChessPiece c)
	{	
		ChessPiece dummy = makeCopy(c);
		ArrayList<ChessPiece> validMoves = new ArrayList<ChessPiece>();
		//Check vertical movement

		//check every space in between the current row and the end of the board

		for(int i = board[c.getID()].getCol() + 1; i <= 7; i++)
		{
			/*
			if(isOccupied(i, c.getCol()))
			{
				if(isOccupied2(i, c.getCol()).getSide() != turn)
					validMoves.add(c);
				break;
			}
			*/
			dummy.setCol(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
			else
				break;
		}
		

		for(int i = board[c.getID()].getCol() - 1; i >= 0; i--)
		{
			/*
			if(isOccupied(i, c.getCol()))
			{
				if(isOccupied2(i, c.getCol()).getSide() != turn)
					validMoves.add(c);
				break;
			}
			*/
			dummy.setCol(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
			else
				break;
		}
		//System.out.println("POSSIBLE HORIZONTAL MOVES: " + validMoves.size());
		
		
		return validMoves;
	}
	
	//Return all possible vertical moves for a piece 
	//NOTE: not used for pawns
	private ArrayList<ChessPiece> getVerticalMoves(ChessPiece c)
	{
		ChessPiece dummy = makeCopy(c);
		ArrayList<ChessPiece> validMoves = new ArrayList<ChessPiece>();
		//Check vertical movement

		//check every space in between the current row and the end of the board

		for(int i = board[c.getID()].getRow() + 1; i <= 7; i++)
		{
			/*
			if(isOccupied(i, c.getCol()))
			{
				if(isOccupied2(i, c.getCol()).getSide() != turn)
					validMoves.add(c);
				break;
			}
			*/
			dummy.setRow(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
		}
		

		for(int i = board[c.getID()].getRow() - 1; i >= 0; i--)
		{
			/*
			if(isOccupied(i, c.getCol()))
			{
				if(isOccupied2(i, c.getCol()).getSide() != turn)
					validMoves.add(c);
				break;
			}
			*/
			dummy.setRow(i);
			if(move(dummy, false))
				validMoves.add(makeCopy(dummy));
		}
		//System.out.println("POSSIBLE VERTICAL MOVES: " + validMoves.size());
		return validMoves;
	}
	
	/*
	 * Checks both vertical directions from the given piece to the desired tile.
	 * If a piece is found between the starting location and the ending location then TRUE is returned.
	 * @PARAMS: The ChessPiece object that is the desired move.
	 * @RETURNS: TRUE if an obstruction is found between the starting tile and ending tile (ending tile excluded).
	 */
	private boolean checkVerticalMovement(ChessPiece c)
	{
		//Check vertical movement
		//Check if any tiles are occupied between the current position and the new position.
		//If any pieces are found, the move will fail.
		boolean intercepted = false;

		//check every space in between the current row and the new location
		if(c.getRow() > board[c.getID()].getRow())
		{
			for(int i = board[c.getID()].getRow() + 1; i < c.getRow(); i++)
			{
				if(isOccupied(i, c.getCol()))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					intercepted = true;
					break;
				}
			}
			
		}
		else
		{
			for(int i = board[c.getID()].getRow() - 1; i > c.getRow(); i--)
			{
				if(isOccupied(i, c.getCol()))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					//NOTE: possibly allow this if the first piece met is an opposing piece.
					intercepted = true;
					break;
				}
			}
		}
		
		return intercepted;
	}
	
	/*
	 * Checks both horizontal directions from the given piece to the desired tile.
	 * If a piece is found between the starting location and the ending location then TRUE is returned.
	 * @PARAMS: The ChessPiece object that is the desired move.
	 * @RETURNS: TRUE if an obstruction is found between the starting tile and ending tile (ending tile excluded).
	 */
	private boolean checkHorizontalMovement(ChessPiece c)
	{
		//Check lateral movement
		//Check if any tiles are occupied between the current position and the new position.
		//If any pieces are found, the move will fail.
		boolean intercepted = false;

		//check every space in between the current column and the new location
		if(c.getCol() > board[c.getID()].getCol())
		{
			for(int i = board[c.getID()].getCol() + 1; i < c.getCol(); i++)
			{
				if(isOccupied(c.getRow(), i))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					intercepted = true;
					break;
				}
			}
			
		}
		else
		{
			for(int i = board[c.getID()].getCol() - 1; i > c.getCol(); i--)
			{
				if(isOccupied(c.getRow(), i))
				{
					//A space between the desired tile and the origin tile has been found to be occupied. The move is illegal.
					//NOTE: possibly allow this if the first piece met is an opposing piece.
					intercepted = true;
					break;
				}
			}
		}
		
		return intercepted;
	}
	//Method to move a piece after it has been sent from the client
	public boolean move(ChessPiece c, boolean actualMove)
	{
		if(gameOver){
			return false;
		}
		
		boolean validMove = false;
		boolean canAttack = false;
		ChessPiece p = isOccupied2(c.getRow(), c.getCol());
		
		//System.out.println("MOVE - Previous: " + board[c.getID()].toString());
		//System.out.println("MOVE - Given Move: " + c.toString());
		
		
		//Pawn move checks
		if(c.getType() == PieceType.PAWN)
		{
			
			if(c.getSide() == Side.WHITE)
			{
				//check for basic forward movement (single space movement) or a double move from starting position
				//System.out.println("ChessBoard - Pawn Validation: basic forward movement WHITE.");
				//make sure pawn wasn't moved laterally 
				if(board[c.getID()].getCol() == c.getCol())
				{
					//System.out.println("ChessBoard - Pawn Validation: basic forward movement WHITE COL IS GOOD.");
					//check if only one space was moved
					if(board[c.getID()].getRow() - 1 == c.getRow() && !isOccupied(c.getRow(), c.getCol()))
					{
						//System.out.println("ChessBoard - Pawn Validation: basic forward movement WHITE 1 TILE.");
						validMove = true;
						//board[c.getID()] = c;
					}
					else if((board[c.getID()].getRow() == 6 && c.getRow() == 4) && !isOccupied(c.getRow() + 1, c.getCol()) && !isOccupied(c.getRow(), c.getCol())) //double movement from starting position
					{
						validMove = true;
						//board[c.getID()] = c;
					}
				}
				else //check for diagonal attack movement
				{
					if(board[c.getID()].getRow() - 1 == c.getRow())
					{
						//check if the piece is one column left or right
						if(board[c.getID()].getCol() + 1 == c.getCol() || board[c.getID()].getCol() - 1 == c.getCol())
						{
							p = isOccupied2(c.getRow(), c.getCol());
							
							if(p != null && p.getSide() != c.getSide())
							{
								//p.setCaptured(true);
								validMove = true;
								//board[c.getID()] = c;
							}
						}
					}
				}
					
			}
			else //BLACK turn - PAWN
			{
				//check for basic forward movement (single space movement) or a double move from starting position
				//System.out.println("ChessBoard - Pawn Validation: basic forward movement BLACK.");
				//make sure pawn wasn't moved laterally 
				if(board[c.getID()].getCol() == c.getCol())
				{
					//System.out.println("ChessBoard - Pawn Validation: basic forward movement BLACK COL IS GOOD.");
					//check if only one space was moved
					if(board[c.getID()].getRow() + 1 == c.getRow()  && !isOccupied(c.getRow(), c.getCol()))
					{
						//System.out.println("ChessBoard - Pawn Validation: basic forward movement BLACK 1 TILE.");
						validMove = true;
						//board[c.getID()] = c;
					}
					else if((board[c.getID()].getRow() == 1 && c.getRow() == 3) && !isOccupied(c.getRow() - 1, c.getCol()) && !isOccupied(c.getRow(), c.getCol()))
					{
						validMove = true;
						//board[c.getID()] = c;
					}
				}
				else //check for diagonal attack movement
				{
					if(board[c.getID()].getRow() + 1 == c.getRow())
					{
						//check if the piece is one column left or right
						if(board[c.getID()].getCol() + 1 == c.getCol() || board[c.getID()].getCol() - 1 == c.getCol())
						{
							
							
							if(p != null && p.getSide() != Side.BLACK)
							{
								//p.setCaptured(true);
								validMove = true;
								//board[c.getID()] = c;
							}
						}
					}
				}
			}
			//Check for pawn promotion
			
		}// End of PAWN movement validation
		
		//ROOK move checks
		if(c.getType() == PieceType.ROOK)
		{
			//boolean intercepted = false;
			//System.out.println("MOVE - ROOK VALIDATION");
			//Check white side rook movements
			if(c.getSide() == Side.WHITE)
			{
				//System.out.println("MOVE - ROOK VALIDATION - WHITE SIDE");
				//Check lateral movement
				//Check if any tiles are occupied between the current position and the new position.
				//If any pieces are found, the move will fail.
				//ChessPiece p = isOccupied2(c.getRow(), c.getCol());
				if(board[c.getID()].getRow() == c.getRow())
				{
					//System.out.println("MOVE - ROOK VALIDATION - WHITE SIDE - LATERAL MOVE");
					
					
					//TODO: Refactor to remove the initial SIDE check as they are unnecessary
					if(p != null && p.getSide() != c.getSide()) //Attack move check
					{
						if(!checkHorizontalMovement(c))
						{
							//p.setCaptured(true);
							validMove = true;
							//board[c.getID()] = c;
						}
					}
					else if(p == null)
					{
						if(!checkHorizontalMovement(c))
						{
							//System.out.println("MOVE - ROOK VALIDATION - WHITE SIDE - NON CAPTURE LATERAL");
							validMove = true;
							//board[c.getID()] = c;
						}
					}				
				}
				//white ROOK vertical move
				else if(board[c.getID()].getCol() == c.getCol())
				{
					if(p != null && p.getSide() != c.getSide()) //Attack move check
					{
						if(!checkVerticalMovement(c))
						{
							//p.setCaptured(true);
							validMove = true;
							//board[c.getID()] = c;
						}
					}
					else if(p == null)
					{
						if(!checkVerticalMovement(c))
						{
							validMove = true;
							//board[c.getID()] = c;
						}
					}
				}
			}
			else //check black side ROOK movements
			{
				//System.out.println("MOVE - ROOK VALIDATION - BLACK SIDE");
				//ChessPiece p = isOccupied2(c.getRow(), c.getCol());
				
				if(board[c.getID()].getRow() == c.getRow())
				{
					//System.out.println("MOVE - ROOK VALIDATION - BLACK SIDE - LATERAL MOVE");
					//check every space in between the current column and the new location
					
					
					if(p != null && p.getSide() != c.getSide()) //Attack move check
					{
						if(!checkHorizontalMovement(c))
						{
							//p.setCaptured(true);
							validMove = true;
							//board[c.getID()] = c;
						}
					}
					else if(p == null)
					{
						if(!checkHorizontalMovement(c))
						{
							validMove = true;
							//board[c.getID()] = c;
						}
					}
				}
				//Black side ROOK vertical movements
				else if(board[c.getID()].getCol() == c.getCol())
				{
					if(p != null && p.getSide() != c.getSide()) //Attack move check
					{
						if(!checkVerticalMovement(c))
						{
							//p.setCaptured(true);
							validMove = true;
							//board[c.getID()] = c;
						}
					}
					else if(p == null)
					{
						if(!checkVerticalMovement(c))
						{
							validMove = true;
							//board[c.getID()] = c;
						}
					}
				}
			}
		}
		
		//BISHOP move checks
		if(c.getType() == PieceType.BISHOP)
		{
			//ChessPiece p = isOccupied2(c.getRow(), c.getCol());
			
			//check diagonal movements
			if(board[c.getID()].getRow() - board[c.getID()].getCol() == c.getRow() - c.getCol() || board[c.getID()].getRow() + board[c.getID()].getCol() == c.getRow() + c.getCol())
			{
				if(p != null && p.getSide() != c.getSide())
				{
					if(!checkDiagonalMovement(c))
					{
						//p.setCaptured(true);
						validMove = true;
						//board[c.getID()] = c;
					}
				}
				else if(p == null)
				{
					if(!checkDiagonalMovement(c))
					{
						validMove = true;
						//board[c.getID()] = c;
					}
				}
			}
		}
		
		//KNIGHT move checks
		if(c.getType() == PieceType.KNIGHT)
		{
			//ChessPiece p = isOccupied2(c.getRow(), c.getCol());
			
			
			if(checkKnightMovement(c))
			{
				//if(p != null && p.getSide() != c.getSide())
					//p.setCaptured(true);
				validMove = true;
				//board[c.getID()] = c;
			}
		}
		
		//QUEEN move checks
		if(c.getType() == PieceType.QUEEN)
		{
			//ChessPiece p = isOccupied2(c.getRow(), c.getCol());
			
			//QUEEN horizontal move
			if(board[c.getID()].getRow() == c.getRow())
			{
				//System.out.println("MOVE - QUEEN VALIDATION - LATERAL MOVE");
				
				
				//TODO: Refactor to remove the initial SIDE check as they are unnecessary
				if(p != null && p.getSide() != c.getSide()) //Attack move check
				{
					if(!checkHorizontalMovement(c))
					{
						//p.setCaptured(true);
						validMove = true;
						//board[c.getID()] = c;
					}
				}
				else if(p == null)
				{
					if(!checkHorizontalMovement(c))
					{
						//System.out.println("MOVE - QUEEN VALIDATION - NON CAPTURE LATERAL");
						validMove = true;
						//board[c.getID()] = c;
					}
				}				
			}
			//QUEEN vertical move
			else if(board[c.getID()].getCol() == c.getCol())
			{
				if(p != null && p.getSide() != c.getSide()) //Attack move check
				{
					if(!checkVerticalMovement(c))
					{
						//p.setCaptured(true);
						validMove = true;
						//board[c.getID()] = c;
					}
				}
				else if(p == null)
				{
					if(!checkVerticalMovement(c))
					{
						validMove = true;
						//board[c.getID()] = c;
					}
				}
			}
			else if(board[c.getID()].getRow() - board[c.getID()].getCol() == c.getRow() - c.getCol() || board[c.getID()].getRow() + board[c.getID()].getCol() == c.getRow() + c.getCol())
			{
				if(p != null && p.getSide() != c.getSide())
				{
					if(!checkDiagonalMovement(c))
					{
						//p.setCaptured(true);
						validMove = true;
						//board[c.getID()] = c;
					}
				}
				else if(p == null)
				{
					if(!checkDiagonalMovement(c))
					{
						validMove = true;
						//board[c.getID()] = c;
					}
				}
			}
		}//end of QUEEN check
		
		if(c.getType() == PieceType.KING)
		{
			//check one square in every direction
			
			
			if(board[c.getID()].getRow() == c.getRow() && board[c.getID()].getCol() + 1 == c.getCol() ||
			   board[c.getID()].getRow() == c.getRow() && board[c.getID()].getCol() - 1 == c.getCol() ||
			   board[c.getID()].getRow() + 1 == c.getRow() && board[c.getID()].getCol() == c.getCol() ||
			   board[c.getID()].getRow() - 1 == c.getRow() && board[c.getID()].getCol() == c.getCol() ||
			   board[c.getID()].getRow() + 1 == c.getRow() && board[c.getID()].getCol() + 1 == c.getCol() ||
			   board[c.getID()].getRow() + 1 == c.getRow() && board[c.getID()].getCol() - 1 == c.getCol() ||
			   board[c.getID()].getRow() - 1 == c.getRow() && board[c.getID()].getCol() + 1 == c.getCol() ||
			   board[c.getID()].getRow() - 1 == c.getRow() && board[c.getID()].getCol() - 1 == c.getCol())
			{
				//ChessPiece p = isOccupied2(c.getRow(), c.getCol());
				
				if(p == null)
				{
					validMove = true;
					//board[c.getID()] = c;
				}
				else if(p != null && p.getSide() != c.getSide())
				{
					//p.setCaptured(true);
					validMove = true;
					//board[c.getID()] = c;
				}
			}
			
		}
		
		
		//Check if the given move removes the CHECK state
		ChessPiece previousP = makeCopy(board[c.getID()]);
		//boolean inCheck = false;
		
		//If its not an actual move, return if the potential move is valid
		if(!actualMove && validMove)
		{
			if(p != null)
				p.setCaptured(true);
			board[c.getID()] = c;
			
			if(checkCheck(c.getSide()))
			{
				board[c.getID()] = previousP;
				if(p != null)
					p.setCaptured(false);
				return false;
			}
			else
			{
				board[c.getID()] = previousP;
				if(p != null)
					p.setCaptured(false);
				return true;
			}
		}
			
		
		//END OF MOVE CHECK
		if(validMove == true)
		{
			if(p != null)
				p.setCaptured(true);
			board[c.getID()] = c;
			pawnPromotion(c);
			//Make sure the move doesn't put the player into Check
			if(checkCheck(c.getSide()))
			{
				board[c.getID()] = previousP;
				if(p != null)
					p.setCaptured(false);
				return false;
			}
			
			//Check if the move ended the game
			gameOver = isCheckmate();
			//Change the turn over upon successfully completing a move
			if(turn == Side.BLACK)
				turn = Side.WHITE;
			else
				turn = Side.BLACK;
			
			if(playerTurn.equals(player1)){
				playerTurn = player2;
			}else{
				playerTurn = player1;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}
