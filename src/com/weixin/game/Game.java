package com.weixin.game;

public class Game {
	private int gameId;
	private String openId;
	private String gameAnswer;
	private String createTime;
	private int gameStatus;
	private String finishTime;
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
	public String getGameAnswer() {
		return gameAnswer;
	}
	public void setGameAnswer(String gameAnswer) {
		this.gameAnswer = gameAnswer;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	
	
}
