package com.thombs.ChessWeb.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "leaderboardView")
public class Leaderboard {
	
	@Id
	private int winner;
	private String username = "AI"; 
	private int wins;
	private int winPercentage;
	
	
	public int getWinner() {
		return winner;
	}
	public void setWinner(int winner) {
		this.winner = winner;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username == null) {
			this.username = "AI";
		}else {
			this.username = username;
		}
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getWinPercentage() {
		return winPercentage;
	}
	public void setWinPercentage(int winPercentage) {
		this.winPercentage = winPercentage;
	}
}
