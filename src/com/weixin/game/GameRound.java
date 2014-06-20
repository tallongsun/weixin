package com.weixin.game;

public class GameRound {
	private int id;
	private int gameId;
	private String openId;
	private String guessNumber;
	private String guessTime;
	private String guessResult;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getGuessNumber() {
		return guessNumber;
	}
	public void setGuessNumber(String guessNumber) {
		this.guessNumber = guessNumber;
	}
	public String getGuessTime() {
		return guessTime;
	}
	public void setGuessTime(String guessTime) {
		this.guessTime = guessTime;
	}
	public String getGuessResult() {
		return guessResult;
	}
	public void setGuessResult(String guessResult) {
		this.guessResult = guessResult;
	}
	
	
}
