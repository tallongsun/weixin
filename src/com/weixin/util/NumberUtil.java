package com.weixin.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	/**
	 * 验证是否是4位纯数字
	 * 
	 * @param number
	 * @return
	 */
	public static boolean verifyNumber(String number) {
		boolean result = false;
		// 正则表达式规则：0-9之间的数字连续出现4次
		Pattern pattern = Pattern.compile("[0-9]{4}");
		Matcher matcher = pattern.matcher(number);
		// 匹配成功就表明是纯数字
		if (matcher.matches())
			result = true;

		return result;
	}

	/**
	 * 验证是否有重复字符
	 * 
	 * @param number
	 * @return
	 */
	public static boolean verifyRepeat(String number) {
		boolean result = false;
		// 从第2位数开始，每位都与它前面的所有数比较一遍
		for (int i = 1; i < number.length(); i++) {
			for (int j = 0; j < i; j++) {
				if (number.charAt(i) == number.charAt(j)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 生成不重复的4位随机数
	 * 
	 * @return String
	 */
	public static String generateRandNumber() {
		StringBuffer randBuffer = new StringBuffer();
		String scopeStr = "0123456789";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			int num = random.nextInt(scopeStr.length());
			randBuffer.append(scopeStr.charAt(num));
			// 将每次获取到的随机数从scopeStr中移除
			scopeStr = scopeStr.replace(String.valueOf(scopeStr.charAt(num)), "");
		}
		return randBuffer.toString();
	}
		
	/**
	 * 计算猜测结果
	 * 
	 * @param answer 正确答案
	 * @param number 猜测的数字
	 * @return xAyB
	 */
	public static String guessResult(String answer, String number) {
		// 位置与数字均相同
		int rightA = 0;
		// 数字存在但位置不对
		int rightB = 0;

		// 计算“A”的个数
		for (int i = 0; i < 4; i++) {
			// 位置与数字均相同
			if (number.charAt(i) == answer.charAt(i)) {
				rightA++;
			}
		}

		// 计算“B”的个数
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// 位置不相同
				if (i != j) {
					if (number.charAt(i) == answer.charAt(j)) {
						rightB++;
					}
				}
			}
		}
		return String.format("%sA%sB", rightA, rightB);
	}
}
