package com.thombs.ChessWeb.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class ChessAI implements Runnable{

	private int difficulty;
	private ChessBoard board;
	String username;
	SimpMessagingTemplate msgTemplate;
	private static final Logger log = LoggerFactory.getLogger(ChessAI.class);
	
	private ChessMatchmaking chessMM;
	
	public ChessAI(int diff, ChessBoard b, SimpMessagingTemplate msgTemplate, String username, ChessMatchmaking chessMM)
	{
		difficulty = diff;
		board = b;
		this.msgTemplate = msgTemplate;
		this.username = username;
		this.chessMM = chessMM;
	}
	
	public void makeMove(){
		Thread t = new Thread(this, "AI move - " + username);
		t.start();
	}
	
	@Override
	public void run() 
	{
		ChessPiece aiMove = null;
		int prevR = -1, prevC = -1;
		
		//random move level
		if(difficulty == 0)
		{
			aiMove = getRandomMove(board.getPossibleMoves(board.getTurn()));
			if(aiMove != null)
			{
				prevR = board.getPiece(aiMove.getID()).getRow();
				prevC = board.getPiece(aiMove.getID()).getCol();
				board.move(aiMove, true);
			}
			else
				return;
		}
		else if(difficulty == 1)
		{		
			aiMove = findBestMove(board);
			if(aiMove != null)
			{
				prevR = board.getPiece(aiMove.getID()).getRow();
				prevC = board.getPiece(aiMove.getID()).getCol();
				board.move(aiMove, true);	
			}
			else
				return;
		}
		else if(difficulty == 2)
		{
			aiMove = findBestMove(board);
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
			aiMove = findBestMove(board);
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
				
				board.setWinner("AI");
				chessMM.saveChessGame(board);
			}
		}
		catch(Exception e)
		{
			System.err.println("ChessAI - run: " + e.getMessage());
			e.printStackTrace();
			if(board.isGameOver())
				System.out.println("AI move failed due to game being over.");
		}
	}
	
	//Very basic 'AI'. Picks a random valid move and takes it
	public ChessPiece getRandomMove(ArrayList<ChessPiece> possibleMoves)
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
	public ChessPiece findBestMove(ChessBoard b)
	{
		List<ChessPiece> possibleMoves = b.getPossibleMoves(b.getTurn());
		Collections.shuffle(possibleMoves);
		List<ScoredChessMove> moveScores = new ArrayList<ScoredChessMove>(possibleMoves.size());
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		for(ChessPiece cp: possibleMoves)
		{
			executorService.execute(new SubMove(moveScores, cp, b.CopyChessBoard(), 3));
		}
		
		try{
			executorService.shutdown();
			executorService.awaitTermination(3, TimeUnit.MINUTES);
		}catch(Exception e){
			log.info("Problem with ExecutorService!");
		}
		
		Collections.sort(moveScores, (m1, m2) -> m2.score - m1.score);
		
		/*
		 * Choose a move based on the level of the AI
		 * 
		 * Level 3 - Always choose the best move
		 * Level 2 - Choose the best move half the time
		 * Level 1 - 25% chance to choose the best move
		 */
		ChessPiece move = null;
		if(b.getAiLevel() >= 3) {
			move = moveScores.get(0).move;
		}else if(b.getAiLevel() == 2) {
			int rand = ThreadLocalRandom.current().nextInt(2);
			if(rand == 0) {
				move = moveScores.get(0).move;
			}else {
				rand = ThreadLocalRandom.current().nextInt(moveScores.size() / 2);
				move = moveScores.get(rand).move;
			}
		}else if(b.getAiLevel() == 1) {
			int rand = ThreadLocalRandom.current().nextInt(4);
			if(rand == 0) {
				move = moveScores.get(0).move;
			}else {
				rand = ThreadLocalRandom.current().nextInt(moveScores.size());
				move = moveScores.get(rand).move;
			}
		}
		
		return move;
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
	}

	
	
}

class ScoredChessMove
{
	int score;
	ChessPiece move;
	
	ScoredChessMove(int v, ChessPiece move)
	{
		score = v;
		this.move = move;
	}
}


class SubMove implements Runnable{
	List<ScoredChessMove> moves;
	ChessPiece move;
	ChessBoard board;
	int depth;
	private static final Logger log = LoggerFactory.getLogger(SubMove.class);
	
	SubMove(List<ScoredChessMove>moves, ChessPiece move, ChessBoard board, int depth){
		this.moves = moves;
		this.move = move;
		this.board = board;
		this.depth = depth;
	}
	
	@Override
	public void run() {
		board.move(move, true);
		int value = alphaBeta(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
		ScoredChessMove scoredMove = new ScoredChessMove(value, move);
		//log.info(move + " === " + scoredMove.score);
		moves.add(scoredMove);
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
}