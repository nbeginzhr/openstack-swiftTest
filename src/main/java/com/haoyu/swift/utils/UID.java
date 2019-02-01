package com.haoyu.swift.utils;

import java.security.SecureRandom;
import java.util.Calendar;

/**
 * UUID字符串生成类
 */
public class UID {

	// 62进制数
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z' };

	// 62进制个数
	private static final int LENGTH = DIGITS.length;
	// 两位62进制对应的十进制数最大值
	private static final int MAX = DIGITS.length * DIGITS.length - 1;
	// 随机数种子生成器
	private static final SecureRandom SEEDGENERATOR = new SecureRandom();
	// 随机数生成器1
	private static final SecureRandom GENERATORONE = new SecureRandom(SEEDGENERATOR.generateSeed(LENGTH));
	// 随机数生成器2
	private static final SecureRandom GENERATORTWO = new SecureRandom(SEEDGENERATOR.generateSeed(LENGTH));
	// 随机数生成器3
	private static final SecureRandom GENERATORTHREE = new SecureRandom(SEEDGENERATOR.generateSeed(LENGTH));
	// 随机数生成器4
	private static final SecureRandom GENERATORFOUR = new SecureRandom(SEEDGENERATOR.generateSeed(LENGTH));
	// 随机数生成器5
	private static final SecureRandom GENERATORFIVE = new SecureRandom(SEEDGENERATOR.generateSeed(LENGTH));
	// 随机数生成器6
	private static final SecureRandom GENERATORSIX = new SecureRandom(SEEDGENERATOR.generateSeed(LENGTH));
	// 起始时间
	private static Calendar INITTIME = null;

	/**
	 * 十进制数转62进制数，十进制数不能大于两位62进制对应的十进制数最大值
	 * 
	 * @param number
	 *            十进制数
	 * @return 两位62进制数
	 */
	private static char[] decTo62(int number) {
		if (number > MAX) {
			throw new IllegalArgumentException("超出系统最大值：" + MAX);
		}
		if (number < 0) {
			throw new IllegalArgumentException("不能小于0：" + number);
		}

		char[] chars = new char[] { '0', '0' };
		chars[1] = DIGITS[(number - (number / LENGTH) * LENGTH)];
		number = number / LENGTH;
		chars[0] = DIGITS[(number - (number / LENGTH) * LENGTH)];

		return chars;
	}

	/**
	 * 获取起始时间
	 * 
	 * @return Calendar 起始时间
	 */
	private static Calendar getInitTime() {
		if (INITTIME == null) {
			INITTIME = Calendar.getInstance();
			INITTIME.set(2016, 0, 1, 0, 0, 0);
		}
		return INITTIME;
	}

	/**
	 * 计算月份差，给定时时间不能在2016年之前
	 * 
	 * @param calendar
	 *            结束时间
	 * @return int 月份差
	 */
	private static int monthSpace(Calendar calendar) {
		int diffYear = calendar.get(Calendar.YEAR) - getInitTime().get(Calendar.YEAR);
		if (diffYear < 0) {
			throw new IllegalArgumentException("系统时间错误，不可能2016年之前！");
		}
		return calendar.get(Calendar.MONTH) - getInitTime().get(Calendar.MONTH) + diffYear * 12;
	}

	/**
	 * UUID字符串生成
	 * 
	 * @return String UUID字符串
	 */
	public static String generate() {
		Calendar now = Calendar.getInstance();
		char[] chars = new char[12];
		// 随机数
		chars[0] = DIGITS[GENERATORONE.nextInt(LENGTH)];
		// 分
		chars[1] = DIGITS[now.get(Calendar.MINUTE)];
		// 随机数
		chars[2] = DIGITS[GENERATORTWO.nextInt(LENGTH)];
		// 时
		chars[3] = DIGITS[now.get(Calendar.HOUR_OF_DAY)];
		// 随机数
		chars[4] = DIGITS[GENERATORTHREE.nextInt(LENGTH)];
		// 日
		chars[5] = DIGITS[now.get(Calendar.DAY_OF_MONTH)];
		// 随机数
		chars[6] = DIGITS[GENERATORFOUR.nextInt(LENGTH)];
		// 月
		char[] src = decTo62(monthSpace(now));
		System.arraycopy(src, 0, chars, 7, 2);
		// 随机数
		chars[9] = DIGITS[GENERATORFIVE.nextInt(LENGTH)];
		// 秒
		chars[10] = DIGITS[now.get(Calendar.SECOND)];
		// 随机数
		chars[11] = DIGITS[GENERATORSIX.nextInt(LENGTH)];
		return new String(chars);
	}

	public static String generate(int length) {
		char[] chars = new char[length];
		for (int i = 0; i != length; ++i) {
			chars[i] = DIGITS[SEEDGENERATOR.nextInt(LENGTH)];
		}
		return new String(chars);
	}

//	@Override
//	public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
//		return generate();
//	}

	/**
	 * 一千个测试
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; ++i) {
			System.out.println(generate());
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

}
