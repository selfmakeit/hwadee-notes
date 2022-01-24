package com.hwadee.liferay.service.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.liferay.portal.kernel.util.Validator;

public class RuleValidator {
	private static final String BASE_CODE_STRING = "0123456789ABCDEFGHJKLMNPQRTUWXY";
    private static final char[] BASE_CODE_ARRAY = BASE_CODE_STRING.toCharArray();
    private static final List<Character> BASE_CODES = new ArrayList<>();
    private static final String BASE_CODE_REGEX = "[" + BASE_CODE_STRING + "]{18}";
    private static final int[] WEIGHT = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};

    static {
        for (char c : BASE_CODE_ARRAY) {
            BASE_CODES.add(c);
        }
    }
    
    /**
     * 是否是有效的统一社会信用代码
     *
     * @param socialCreditCode 统一社会信用代码
     * @return
     */
	  public static boolean isValidSocialCreditCode(String socialCreditCode) {
	        if (Validator.isNull(socialCreditCode) || !Pattern.matches(BASE_CODE_REGEX, socialCreditCode)) {
	            return false;
	        }
	        char[] businessCodeArray = socialCreditCode.toCharArray();
	        char check = businessCodeArray[17];
	        int sum = 0;
	        for (int i = 0; i < 17; i++) {
	            char key = businessCodeArray[i];
	            sum += (BASE_CODES.indexOf(key) * WEIGHT[i]);
	        }
	        int value = 31 - sum % 31;
	        return check == BASE_CODE_ARRAY[value % 31];
	    }

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
     * 大写金额转数字
     *
     */
    public static String chineseConvertToNumber(String chineseAmount) {
        if (chineseAmount == null || chineseAmount.length() <= 0 || chineseAmount == "") {
            return null;
        }
        //移除计算干扰文字
        chineseAmount = chineseAmount
                .replace("元", "")
                .replace("整", "");

        // 字符切割
        char[] wordCharArray = chineseAmount.toCharArray();

        //最终要返回的数字金额
        BigDecimal numberAmount = BigDecimal.ZERO;

        //金额位标志量
        //表示有分位
        boolean fen = false;
        //表示有角位
        boolean jiao = false;
        // 表示个位不为0
        boolean yuan = false;
        //表示有十位
        boolean shi = false;
        //表示有百位
        boolean bai = false;
        //表示有千位
        boolean qian = false;
        //表示有万位
        boolean wan = false;
        //表示有亿位
        boolean yi = false;

        //从低位到高位计算
        for (int i = (wordCharArray.length - 1); i >= 0; i--) {
            //当前位金额值
            BigDecimal currentPlaceAmount = BigDecimal.ZERO;

            //判断当前位对应金额标志量
            if (wordCharArray[i] == '分') {
                fen = true;
                continue;
            } else if (wordCharArray[i] == '角') {
                jiao = true;
                continue;
            } else if (wordCharArray[i] == '元') {
                yuan = true;
                continue;
            } else if (wordCharArray[i] == '拾') {
                shi = true;
                continue;
            } else if (wordCharArray[i] == '佰') {
                bai = true;
                continue;
            } else if (wordCharArray[i] == '仟') {
                qian = true;
                continue;
            } else if (wordCharArray[i] == '万') {
                wan = true;
                continue;
            } else if (wordCharArray[i] == '亿') {
                yi = true;
                continue;
            }

            //根据标志量转换金额为实际金额
            double t = 0;
            if (fen) {
                t = convertNameToSmall(wordCharArray[i]) * 0.01;
            } else if (jiao) {
                t = convertNameToSmall(wordCharArray[i]) * 0.1;
            } else if (shi) {
                t = convertNameToSmall(wordCharArray[i]) * 10;
            } else if (bai) {
                t = convertNameToSmall(wordCharArray[i]) * 100;
            } else if (qian) {
                t = convertNameToSmall(wordCharArray[i]) * 1000;
            } else {
                t = convertNameToSmall(wordCharArray[i]);
            }
            currentPlaceAmount = new BigDecimal(t);
            //每万位处理
            if (yi) {
                currentPlaceAmount = currentPlaceAmount.multiply(new BigDecimal(100000000));
            } else if (wan) {
                currentPlaceAmount = currentPlaceAmount.multiply(new BigDecimal(10000));
            }
            numberAmount = numberAmount.add(currentPlaceAmount);
            // 重置状态
            //yi = false; // 亿和万  不可重置 下次循环还会用到
            //wan = false;
            fen = false;
            jiao = false;
            shi = false;
            bai = false;
            qian = false;
            yuan = false;
        }
        return numberAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 转换中文数字为阿拉伯数字
     *
     * @param chinese
     * @return
     * @throws Exception
     */
    private static int convertNameToSmall(char chinese) {
        int number = 0;
        String s = String.valueOf(chinese);
        switch (s) {
            case "零":
                number = 0;
                break;
            case "壹":
                number = 1;
                break;
            case "贰":
                number = 2;
                break;
            case "叁":
                number = 3;
                break;
            case "肆":
                number = 4;
                break;
            case "伍":
                number = 5;
                break;
            case "陆":
                number = 6;
                break;
            case "柒":
                number = 7;
                break;
            case "捌":
                number = 8;
                break;
            case "玖":
                number = 9;
                break;
        }
        return number;
    }

//    public static void main(String[] args) {
//        String[] a = {
//                "陆仟肆佰壹拾贰元壹角",
//                "壹仟元整",
//                "壹佰元壹角贰分",
//                "壹万零伍拾元壹角",
//                "壹拾伍万陆仟零叁拾元整",
//                "壹佰贰拾叁元零壹分" // 123.01
//        };
//        //for (int i = 0; i < a.length; i++) {
//        //    System.out.println("CNNMFilter.chineseConvertToNumber: " + CNNMFilter.chineseConvertToNumber(a[i]));
//        //}
//        System.out.println("CNNMFilter.chineseConvertToNumber: " + chineseConvertToNumber("壹亿肆仟万元整元整"));
////返回结果：CNNMFilter.chineseConvertToNumber: 140000000.00
//    }

	
	
	
	
}
