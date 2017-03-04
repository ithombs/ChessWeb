package com.thombs.ChessWeb.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class ChessAI implements Runnable{

	private int difficulty;
	private ChessBoard board;
	//WebSocketSession s;
	String username;
	SimpMessagingTemplate msgTemplate;
	private static final Logger log = LoggerFactory.getLogger(ChessAI.class);
	
	public ChessAI(int diff, ChessBoard b, SimpMessagingTemplate msgTemplate, String username)
	{
		difficulty = diff;
		board = b;
		this.msgTemplate = msgTemplate;
		this.username = username;
	}
	
	public void makeMove(){
		Thread t = new Thread(this, "AI move - " + username);
		t.start();
	}
	
	@Override
	public void run() 
	{
		//System.out.println("---Entered AI move---");
		ChessPiece aiMove = null;
		int prevR = -1, prevC = -1;
		
		//DEBUG - print out all possible moves for this turn
		/*
		for(ChessPiece p : board.getPossibleMoves(board.getTurn()))
		{
			//System.out.println("-" + p.toString());
		}
		*/
		//random move level
		if(difficulty == 0)
		{
			aiMove = randomMove(board.getPossibleMoves(board.getTurn()));
			if(aiMove != null)
			{
				prevR = board.getPiece(aiMove.getID()).getRow();
				prevC = board.getPiece(aiMove.getID()).getCol();
				board.move(aiMove, true);
			}
			else
				return;
		}
		//Alpha beta pruning depth 4
		else if(difficulty == 1)
		{		
			aiMove = aiMove1(board, 3);
			if(aiMove != null)
			{
				prevR = board.getPiece(aiMove.getID()).getRow();
				prevC = board.getPiece(aiMove.getID()).getCol();
				board.move(aiMove, true);	
			}
			else
				return;
		}
		//depth of 8
		else if(difficulty == 2)
		{
			aiMove = aiMove1(board, 5);
			if(aiMove != null)
			{
				prevR = board.getPiece(aiMove.getID()).getRow();
				prevC = board.getPiece(aiMove.getID()).getCol();
				board.move(aiMove, true);	
			}
			else
				return;
		}
		else if(difficulty == 3)
		{
			aiMove = aiMove1(board, 7);
			if(aiMove != null)
			{
				prevR = board.getPiece(aiMove.getID()).getRow();
				prevC = board.getPiece(aiMove.getID()).getCol();
				board.move(aiMove, true);	
			}
			else
				return;
		}
		//Send the move to the client
		try
		{
			//ChessPiece p = board.getPiece(aiMove.getID());
			JSONObject json = new JSONObject();
			json.put("chessCommand", "move");
			json.put("pieceID", aiMove.getID());
			json.put("row", aiMove.getRow());
			json.put("col", aiMove.getCol());
			json.put("ml1", prevR + "|" + prevC);
			json.put("ml2", aiMove.getRow() + "|" + aiMove.getCol());
			
			msgTemplate.convertAndSendToUser(username, "/queue/chessMsg", json.toString());
			if(board.isGameOver()){
				JSONObject jsonGameOver = new JSONObject();
				jsonGameOver.put("chessCommand", "gameOver");
				jsonGameOver.put("winner", "AI");
				msgTemplate.convertAndSendToUser(username, "/queue/chessMsg", jsonGameOver.toString());
			}
		}
		catch(Exception e)
		{
			System.err.println("ChessAI - run: " + e.getMessage());
			e.printStackTrace();
			if(board.isGameOver())
				System.out.println("AI move failed due to game being over.");
		}
		//System.out.println("---Exit AI move---");
	}
	
	//Very basic 'AI'. Picks a random valid move and takes it
	public ChessPiece randomMove(ArrayList<ChessPiece> possibleMoves)
	{
		ChessPiece move = null;
		
		if(possibleMoves.size() > 0)
		{
			Random r = new Random();
			int num = r.nextInt(possibleMoves.size());
			
			move = possibleMoves.get(num);
		}
		return move;
	}
	
	//ACTUAL AI METHOD
	public ChessPiece aiMove1(ChessBoard b, int depth)
	{
		int pos = 0;
		int num = -1;
		int highScore = -1;
		List<ChessPiece> bestMoves = new ArrayList<ChessPiece>();
		List<ChessPiece> possibleMoves = b.getPossibleMoves(b.getTurn());
		
		for(ChessPiece cp: possibleMoves)
		{
			ChessBoard bb = b.CopyChessBoard();
			bb.move(cp, true);
			int value = alphaBeta(bb, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
			log.info("Calculating move["+ pos +"/"+ (possibleMoves.size() - 1) +"] - " + cp + " - [score = "+ value +"]");
			if(value > highScore){
				highScore = value;
				num = pos;
				bestMoves.clear();
				bestMoves.add(b.getPossibleMoves(b.getTurn()).get(num));
			}else if(value == highScore){
				bestMoves.add(b.getPossibleMoves(b.getTurn()).get(pos));
			}
			pos++;
		}
		
		if(possibleMoves.size() > 0){
			if(bestMoves.size() == 1){
				return bestMoves.get(0);
			}else{
				Random r = new Random();
				return bestMoves.get(r.nextInt(bestMoves.size() - 1));
			}
		}else{
			return null;
		}	
	}
	
	public ChessPiece aiMove2(ChessBoard b, int depth)
	{
		BestMove best = null;
		
		best = AB2(b, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		
		if(best.value == 0){
			best.bestMove = randomMove(b.getPossibleMoves(b.getTurn()));
		}
		return best.bestMove;
	}
	
	private int alphaBeta(ChessBoard b, int depth, int alpha, int beta, boolean computer)
	{
		int bestVal;
		if(depth == 0 || b.isGameOver())
		{
			if(b.isGameOver())
			{
				return Integer.MAX_VALUE - 1000;
			}else{
				return evaluateBoard(b);
			}
		}
		
		if(computer)
		{
			bestVal = alpha;
			
			for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
			{
				ChessBoard newB = b.CopyChessBoard();
				newB.move(c, true);
				
				int score = alphaBeta(newB, depth - 1, bestVal, beta, false);
				bestVal = Math.max(score, bestVal);
				
				if(beta <= bestVal)
					break;
			}
		}
		else
		{
			bestVal = beta;
			
			for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
			{
				ChessBoard newB = b.CopyChessBoard();
				newB.move(c, true);
				
				int score = alphaBeta(newB, depth - 1, alpha, bestVal, true);
				bestVal = Math.min(score, bestVal);
				
				if(bestVal <= alpha)
					break;
			}
		}
		
		return bestVal;
	}
	
	private int evaluateBoard(ChessBoard b)
	{
		int hueristicValue = 0;
		
		if(b.isCheckmate()){
			hueristicValue = Integer.MAX_VALUE - 100;
		} else if(b.isOpponentInCheck()){
			hueristicValue = 1000;
		}
		
		for(ChessPiece c : b.getBoard())
		{
			if(c.isCaptured())
				continue;
			if(c.getSide() == b.getTurn())
			{
				if(c.getType() == PieceType.PAWN)
				{
					hueristicValue += 1;
				}
				else if(c.getType() == PieceType.KNIGHT)
				{
					hueristicValue += 3;
				}
				else if(c.getType() == PieceType.BISHOP)
				{
					hueristicValue += 3;
				}
				else if(c.getType() == PieceType.ROOK)
				{
					hueristicValue += 5;
				}
				else if(c.getType() == PieceType.QUEEN)
				{
					hueristicValue += 9;
				}
			}
			else
			{
				if(c.getType() == PieceType.PAWN)
				{
					hueristicValue -= 1;
				}
				else if(c.getType() == PieceType.KNIGHT)
				{
					hueristicValue -= 3;
				}
				else if(c.getType() == PieceType.BISHOP)
				{
					hueristicValue -= 3;
				}
				else if(c.getType() == PieceType.ROOK)
				{
					hueristicValue -= 5;
				}
				else if(c.getType() == PieceType.QUEEN)
				{
					hueristicValue -= 9;
				}
			}
		}
		
		return hueristicValue;
	}
	
	private BestMove AB2(ChessBoard b, int depth, int alpha, int beta, boolean computer)
	{
		if(depth == 0 || b.isGameOver())
		{
			return new BestMove(evaluateBoard(b), null);
		}
		else
		{
			if(computer)
			{
				ChessPiece bestMove = null;
				int bestVal = alpha;
				for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
				{
					ChessBoard bb = b.CopyChessBoard();
					bb.move(c, true);
					BestMove bm = AB2(bb, depth - 1, alpha, beta, false);
					bm.value = Math.max(alpha, bm.value);
				
					if(beta <= bm.value){
						break;
					}else{
						bestMove = c;
					}	
				}
				return new BestMove(alpha, bestMove);
			}
			else
			{
				ChessPiece bestMove = null;
				int bestVal = beta;
				for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
				{
					ChessBoard bb = b.CopyChessBoard();
					bb.move(c, true);
					BestMove bm = AB2(bb, depth - 1, alpha, beta, true);
					
					if(bm.value < beta)
					{
						beta = bm.value;
					}
					bestMove = c;
					if(alpha >= beta)
						break;
				}
				return new BestMove(beta, bestMove);
			}
		}
	}
	
	
	//For testing
	public static void main(String args[])
	{
		ChessBoard b = new ChessBoard();
		ChessBoard c = b.CopyChessBoard();
		c.getPiece(8).setCaptured(true);
		//ChessAI test = new ChessAI()
		
		System.out.println(b.getPiece(8).isCaptured());
		System.out.println(c.getPiece(8).isCaptured());
		
		//System.out.println(aiMove2(b, 5));
		
		/*
		for(ChessPiece cp: b.getPossibleMoves(b.getTurn()))
		{
			ChessBoard bb = b.CopyChessBoard();
			bb.move(cp, true);
			System.out.println(alphaBeta(bb.CopyChessBoard(), 8, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
		}
		*/
		//System.out.println(aiMove1(b));
		
		
		/*
		for(ChessPiece c : b.checkPossibleMovements(b.getPiece(16)))
		{
			System.out.println(c.getID() + "|" + c.getRow() + "|" + c.getCol());
		}
		*/
		//ChessAI.randomMove(b.getPossibleMoves(b.getTurn()));
	}

	
	
}

class BestMove
{
	int value;
	ChessPiece bestMove;
	
	BestMove(int v, ChessPiece best)
	{
		value = v;
		bestMove = best;
	}
}
