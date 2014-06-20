package com.weixin.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.NewsResMessage;
import com.weixin.msg.res.TextResMessage;
import com.weixin.msg.res.NewsResMessage.Article;
import com.weixin.util.DBUtil;
import com.weixin.util.MessageUtil;
import com.weixin.util.NumberUtil;
import com.weixin.util.TimeUtil;

public class GameLogic {
	
	
	public static BaseResMessage process(String content){
		List<Article> articleList = new ArrayList<Article>();
		
		Article article = new Article();
		article.setTitle("雷电\n80后不得不玩的经典游戏");
		article.setDescription("");
		article.setPicUrl("http://resource.duopao.com/sg/image/20140306010349.png");
		article.setUrl("http://www.duopao.com/games/info?game_code=g20140120233048400063");
		articleList.add(article);

		article = new Article();
		article.setTitle("微信上也能斗地主");
		article.setDescription("");
		article.setPicUrl("http://resource.duopao.com/sg/image/20140119234306.png");
		article.setUrl("http://resource.duopao.com/duopao/games1/Doudizhu/index.html?ver=0.2&game_code=g20140102184019324462");
		articleList.add(article);
		
		article = new Article();
		article.setTitle("猜数字");
		article.setDescription(getGameRule());
		article.setPicUrl("");
		article.setUrl("");
		articleList.add(article);
		
        return new NewsResMessage(articleList.size(),articleList);  
        
	}
	
	public static BaseResMessage process(String content,String openId){
		if(NumberUtil.verifyRepeat(content)){
			return new TextResMessage("请输入4个不重复的数字，例如：0269");
		}
		
		// 获取用户最近一次创建的游戏
		Game game = DBUtil.getLastGame(openId);
		// 本局游戏的正确答案
		String gameAnswer = null;
		// 本回合的猜测结果
		String guessResult = null;
		// 本局游戏的id
		int gameId = -1;
		// 是否是新的一局（默认为false）
		boolean newGameFlag = (null == game || 0 != game.getGameStatus());
		if (newGameFlag) {// 新的一局
			// 生成不重复的4位随机数作为答案
			gameAnswer = NumberUtil.generateRandNumber();
			// 计算本回合“猜数字”的结果（xAyB）
			guessResult = NumberUtil.guessResult(gameAnswer, content);
			// 创建game
			game = new Game();
			game.setOpenId(openId);
			game.setGameAnswer(gameAnswer);
			game.setCreateTime(TimeUtil.getStdDateTime());
			game.setGameStatus(0);
			// 保存game并获取id
			gameId = DBUtil.saveGame(game);
		}else {// 不是新的一局
			gameAnswer = game.getGameAnswer();
			guessResult = NumberUtil.guessResult(game.getGameAnswer(), content);
			gameId = game.getGameId();
		}
		// 保存当前游戏回合
		GameRound gameRound = new GameRound();
		gameRound.setGameId(gameId);
		gameRound.setOpenId(openId);
		gameRound.setGuessNumber(content);
		gameRound.setGuessTime(TimeUtil.getStdDateTime());
		gameRound.setGuessResult(guessResult);
		DBUtil.saveGameRound(gameRound);
		// 查询本局游戏的所有回合
		List<GameRound> roundList = DBUtil.findAllRoundByGameId(gameId);
		// 遍历游戏回合，组装给用户回复的消息
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < roundList.size(); i++) {
			gameRound = roundList.get(i);
			buffer.append(String.format("第%d回合： %s    %s", i + 1, gameRound.getGuessNumber(), gameRound.getGuessResult())).append("\n");
		}
		
		if ("4A0B".equals(guessResult)) {// 猜对
			buffer.append("正确答案：").append(gameAnswer).append("\n");
			buffer.append("恭喜您猜对了！[强]").append("\n");
			buffer.append("重新输入4个不重复的数字开始新的一局。");
			DBUtil.updateGame(gameId, 1, TimeUtil.getStdDateTime());
		}else if (roundList.size() >= 10) {// 10回合仍没猜对
			buffer.append("正确答案：").append(gameAnswer).append("\n");
			buffer.append("唉，10回合都没猜出来，本局结束！[流泪]").append("\n");
			buffer.append("重新输入4个不重复的数字开始新的一局。");
			DBUtil.updateGame(gameId, 2, TimeUtil.getStdDateTime());
		}else {// 本回合没猜对
			buffer.append("请再接再励！");
		}
		return new TextResMessage(buffer.toString());
		
	}
	
	
	
	private static String getGameRule() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("《猜数字游戏玩法》").append("\n");
		buffer.append("系统设定一个没有重复数字的4位数，由玩家来猜，每局10次机会。").append("\n");
		buffer.append("每猜一次，系统会给出猜测结果xAyB，x表示数字与位置均正确的数的个数，y表示数字正确但位置不对的数的个数。").append("\n");
		buffer.append("玩家根据猜测结果xAyB一直猜，直到猜中(4A0B)为止。").append("\n");
		buffer.append("如果10次都没猜中，系统会公布答案，游戏结束。").append("\n");
		buffer.append("玩家任意输入一个没有重复数字的4位数即开始游戏，例如：7890");
		return buffer.toString();
	}
	
	public static String getUserScore(String openId) {
		StringBuffer buffer = new StringBuffer();
		HashMap<Integer, Integer> scoreMap = DBUtil.getScoreByOpenId(openId);
		if (null == scoreMap || 0 == scoreMap.size()) {
			buffer.append("您还没有玩过猜数字游戏！").append("\n");
			buffer.append("请输入4个不重复的数字开始新的一局，例如：0269");
		} else {
			buffer.append("您的游戏战绩如下：").append("\n");
			for (Integer status : scoreMap.keySet()) {
				if (0L == status) {
					buffer.append("游戏中：").append(scoreMap.get(status)).append("\n");
				} else if (1L == status) {
					buffer.append("胜利局数：").append(scoreMap.get(status)).append("\n");
				} else if (2L == status) {
					buffer.append("失败局数：").append(scoreMap.get(status)).append("\n");
				}
			}
			buffer.append("请输入4个不重复的数字开始新的一局，例如：0269");
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		String resStr = MessageUtil.messageToXml(process(""));  
		System.out.println(resStr);
	}
}
