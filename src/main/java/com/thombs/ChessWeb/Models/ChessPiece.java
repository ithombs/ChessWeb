package com.thombs.ChessWeb.Models;

enum PieceType
{
	PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;
}
/*
enum Side
{
	WHITE, BLACK;
}
*/
/*
enum Space - wrong names anyway. If ever used again then columns and rows must be switched
{
	A1, A2, A3, A4, A5, A6, A7, A8,
	B1, B2, B3, B4, B5, B6, B7, B8,
	C1, C2, C3, C4, C5, C6, C7, C8,
	D1, D2, D3, D4, D5, D6, D7, D8,
	E1, E2, E3, E4, E5, E6, E7, E8,
	F1, F2, F3, F4, F5, F6, F7, F8,
	G1, G2, G3, G4, G5, G6, G7, G8,
	H1, H2, H3, H4, H5, H6, H7, H8;
}
*/

public class ChessPiece {
	private int id;
	private PieceType type;
	private Side side;
	private int row;
	private int col;
	private boolean isCaptured;
	
	public ChessPiece(int i, PieceType p, Side s, int r, int c, boolean captured)
	{
		id = i;
		type = p;
		side = s;
		row = r;
		col = c;
		isCaptured = captured;
	}
	
	public PieceType getType() {
		return type;
	}
	public void setType(PieceType type) {
		this.type = type;
	}
	public Side getSide() {
		return side;
	}
	public void setSide(Side side) {
		this.side = side;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getID()
	{
		return this.id;
	}
	public void setID(int id)
	{
		this.id = id;
	}
	public boolean isCaptured() {
		return isCaptured;
	}
	public void setCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	//Make a deep copy of a given ChessPiece
	public static ChessPiece CopyChessPiece(ChessPiece c)
	{
		return new ChessPiece(c.getID(), c.getType(), c.getSide(), c.getRow(), c.getCol(), c.isCaptured());
	}
	
	@Override
	public String toString()
	{
		return "PieceType: " + type + " - Side: " + side + " - Space: [" + row + "] [" + col + "]";
	}

	@Override
	public boolean equals(Object obj) {
		ChessPiece p = (ChessPiece)obj;
		if(this.id == p.getID())
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
	
}
