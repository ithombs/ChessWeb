package com.thombs.ChessWeb.Models;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.TextMessage;

public class ChessAI implements Runnable{

	private int difficulty;
	private ChessBoard board;
	//WebSocketSession s;
	String username;
	SimpMessagingTemplate msgTemplate;
	
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
			aiMove = aiMove2(board, 2);
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
			aiMove = aiMove2(board, 4);
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
			aiMove = aiMove2(board, 7);
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
		
		for(ChessPiece cp: b.getPossibleMoves(b.getTurn()))
		{
			ChessBoard bb = b.CopyChessBoard();
			bb.move(cp, true);
			int value = alphaBeta(bb.CopyChessBoard(), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
			
			if(value > num)
				num = pos;
			pos++;
		}
		if(num > -1)
			return b.getPossibleMoves(b.getTurn()).get(num);
		else
			return null;
	}
	
	public ChessPiece aiMove2(ChessBoard b, int depth)
	{
		BestMove best = null;
		
		best = AB2(b, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		return best.bestMove;
	}
	
	private int alphaBeta(ChessBoard b, int depth, int alpha, int beta, boolean computer)
	{
		/*
		01 function alphabeta(node, depth, a, b, maximizingPlayer)
		02      if depth = 0 or node is a terminal node
		03          return the heuristic value of node
		04      if maximizingPlayer
		05          v := -inf
		06          for each child of node
		07              v := max(v, alphabeta(child, depth - 1, a, b, FALSE))
		08              a := max(a, v)
		09              if b <= a
		10                  break (* b cut-off *)
		11          return v
		12      else
		13          v := inf
		14          for each child of node
		15              v := min(v, alphabeta(child, depth - 1, a, b, TRUE))
		16              b := min(b, v)
		17              if b >= a
		18                  break (* a cut-off *)
		19          return v
		*/
		
		// pawn = 1, knight = 3, bishop = 3, rook = 5, queen = 9
		if(depth == 0 || b.isGameOver())
		{
			if(b.isGameOver())
				return Integer.MAX_VALUE - 1000;
			
			int hueristicValue = 0;
			
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
		
		if(computer)
		{
			int value = Integer.MIN_VALUE;
			
			for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
			{
				ChessBoard newB = b.CopyChessBoard();
				newB.move(c, true);
				
				int score = alphaBeta(newB, depth - 1, alpha, beta, false);
				if(score > value)
					value = score;
				
				if(value > alpha)
					alpha = value;
				
				if(beta <= alpha)
					break;
				
				
			}
			return value;
		}
		else
		{
			int value = Integer.MAX_VALUE;
			
			for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
			{
				ChessBoard newB = b.CopyChessBoard();
				newB.move(c, true);
				
				int v = alphaBeta(newB, depth - 1, alpha, beta, true);
				if(v < value)
					value = v;
				
				if(value < beta)
					beta = value;
				
				if(beta >= alpha)
					break;
			}
			return value;
		}
	}
	
	private int EvaluateBoard(ChessBoard b)
	{
		int hueristicValue = 0;
		
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
			return new BestMove(EvaluateBoard(b), null);
		}
		else
		{
			if(computer)
			{
				ChessPiece bestMove = null;
				for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
				{
					ChessBoard bb = b.CopyChessBoard();
					bb.move(c, true);
					BestMove bm = AB2(bb, depth - 1, alpha, beta, false);
					//if(depth == 1)
						//System.out.println("Depth: " + depth + " - BM = (" + bm.value + ", " + bm.bestMove + ")");
					if(bm.value > alpha)
					{
						alpha = bm.value;
						bestMove = c;
						
						if(alpha >= beta)
							break;
					}
				}
				return new BestMove(alpha, bestMove);
			}
			else
			{
				ChessPiece bestMove = null;
				for(ChessPiece c : b.getPossibleMoves(b.getTurn()))
				{
					ChessBoard bb = b.CopyChessBoard();
					bb.move(c, true);
					BestMove bm = AB2(bb, depth - 1, alpha, beta, true);
					
					if(bm.value < beta)
					{
						beta = bm.value;
						bestMove = c;
						
						if(alpha >= beta)
							break;
					}
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
