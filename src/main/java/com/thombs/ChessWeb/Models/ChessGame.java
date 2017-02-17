package com.thombs.ChessWeb.Models;
/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



 * NOTE: This is for use in the WEBSOCKS project (not the Spring portion)
 

 * This class holds the data to a single instance of a game played between two players.
 * 
 * NOTE: Do not confuse this with ChessBoard.java, which holds the logic behind move validation and basic game features.
 
public class ChessGame {
	private String player1;
	private String player2;
	private Side player1Side;
	private Side player2Side;
	private String winner;
	private int surrender;
	
	private Connection conn;
	
	public ChessGame(String p1, String p2, Side p1s, Side p2s,String w, int surr)
	{
		player1 = p1;
		player2 = p2;
		player1Side = p1s;
		player2Side = p2s;
		winner = w;
		surrender = surr;
		dbConnect();
	}
	
	//Connect to the database
	private void dbConnect()
	{
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/MAtest1?user=root&password=system");	
		}
		catch(Exception e)
		{
			System.err.println("ChessGame - dbConnect: " + e.getMessage());
			e.printStackTrace();
		}
		
		DatabaseManager dbMan = new DatabaseManager();
		conn = dbMan.getConnection2();
		System.out.println("---------------------------------------");
	}
	
	
	 * Put the game into the database
	 
	public void logGame(ArrayList<ChessPiece> moveList)
	{
		java.util.Date d = new java.util.Date();
		java.sql.Date sqlD = new java.sql.Date(d.getTime());
		
		
		
		UserLogin u1 = new UserLogin(player1, "");
		UserLogin u2 = new UserLogin(player2, "");
		PreparedStatement pstm;
		PreparedStatement pstm2;
		PreparedStatement pstm3;
		ResultSet rs;
		int win;
		int id1 = u1.getID();
		int id2 = u2.getID();
		u1.closeConn();
		u2.closeConn();
		
		if(winner.equals(player1))
			win = id1;
		else
			win = id2;
		
		try
		{
			//testing
			System.out.println(player1);
			System.out.println(player2);
			System.out.println("ID 1: " + id1);
			System.out.println("ID 2: " + id2);
			
			//Insert game
			pstm = conn.prepareStatement("INSERT INTO chess_game VALUES(NULL, ?, ?, ?, ?, ?)");
			if(player1Side == Side.WHITE)
			{
				pstm.setInt(1, id1);
				pstm.setInt(2, id2);
				pstm.setInt(3, win);
				pstm.setInt(4, surrender);
				pstm.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
			}
			else
			{
				pstm.setInt(1, id2);
				pstm.setInt(2, id1);
				pstm.setInt(3, win);
				pstm.setInt(4, surrender);
				pstm.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
			}
			
			pstm.executeUpdate();
			pstm.close();
			
			
			//insert move list
			pstm2 = conn.prepareStatement("SELECT gameID FROM chess_game WHERE playerWhite = ? AND playerBlack = ?");
			if(player1Side == Side.WHITE)	
			{
				pstm2.setInt(1, id1);
				pstm2.setInt(2, id2);
			}
			else
			{
				pstm2.setInt(1, id2);
				pstm2.setInt(2, id1);
			}
			
			rs = pstm2.executeQuery();
			
			int gameID = -1;
			
			//get the last game played ID
			rs.last();
			gameID = rs.getInt("gameID");
			pstm2.close();

			int moveNum = 0;
			
			
			for(ChessPiece p : moveList)
			{
				pstm3 = conn.prepareStatement("INSERT INTO chess_move_list VALUES(?, ?, ?)");
				pstm3.setInt(1, gameID);
				pstm3.setInt(2, moveNum);
				pstm3.setString(3, p.getID() + "|" + p.getRow() + "|" + p.getCol());
				pstm3.executeUpdate();
				pstm3.close();
				moveNum++;
			}
		}
		catch(Exception e)
		{
			System.err.println("ChessGame - logGame: " + e.getMessage());
		}
	}
	
	
	 * Close the database connection
	 
	public void closeConn()
	{
		try
		{
			conn.close();
		}
		catch(Exception e)
		{
			System.err.println("ChessStatsHelper - closeConn: " + e.getMessage());
		}
	}
	
}
*/