package com.fclub.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;




/**
 * String 工具类
 * @author zt
 * @since 1.0
 * @version 2012-11-4
 * @Copyright (C)2012 , Suntektech
 */
public class StringUtil
{
	
	/**
	 * 判断字符串是否为null（为空包括null | '' | -1。 -1 为兼容 Select 下拉框默认选项值为 -1 的情况）
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str)
	{
		return (str == null || str.trim().equals("") || str.equals("-1"));
	}
	
	/**
	 * 判断对象是否为空（为空包括null | "" | "null"|"NULL"。 
	 * @return
	 */
	public static boolean isObjectNull(Object obj)
	{
		if(obj == null) {
			return true;
		} else {
			String str = obj.toString().trim();
			
			if("".equals(str) || "null".equalsIgnoreCase(str)
					|| "\"null\"".equalsIgnoreCase(str) || "'null'".equalsIgnoreCase(str)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 字符串为空时取默认值
	 * @param str 被检查串
	 * @param def 默认值
	 * @return
	 */
	public static String nvl(String str, String def)
	{
		if (isNull(str)) 
			return def;
		else 
			return str;
	}
	
	/**
	 * 跟踪打印参数化 SQL
	 * @param sql SQL语句
	 * @param strings SQL 参数
	 * @return
	 */
	public static String traceSql(String sql, String... strings)
	{
		for (int i=0; i<strings.length; i++) {
			if (strings[i] == null) {
				sql = sql.replaceFirst("\\?", "NULL");
			} else {
		//		sql = sql.replaceFirst("\\?", strings[i]);
				String replacement = strings[i].toString();
				replacement = replacement.replaceAll("\\\\", "SLASH_CHAR_DOLLAR");// encode replacement;
				replacement = replacement.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;

				sql = sql.replaceFirst("\\?", replacement);
				sql = sql.replaceAll("SLASH_CHAR_DOLLAR", "\\\\");// decode replacement;
				sql = sql.replaceAll("RDS_CHAR_DOLLAR", "\\$");// decode replacement;
			}
		}
		

		return sql;
	}
	
	/**
	 * 跟踪打印参数化 SQL
	 * @param sql SQL语句
	 * @param strings SQL 参数
	 * @return
	 */
	public static String traceSql(String sql, Object... strings)
	{
		for (int i=0; i<strings.length; i++) {
			if (strings[i] == null) {
				sql = sql.replaceFirst("\\?", "NULL");
			} else {
				String replacement = strings[i].toString();

				replacement = replacement.replaceAll("\\\\", "SLASH_CHAR_DOLLAR");// encode replacement;
				replacement = replacement.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;

				sql = sql.replaceFirst("\\?", replacement);
				sql = sql.replaceAll("SLASH_CHAR_DOLLAR", "\\\\");// decode replacement;
				sql = sql.replaceAll("RDS_CHAR_DOLLAR", "\\$");// decode replacement;
			}
		}
		
		return sql;
	}
	
	/**
	 * 以 ':' 为连接符，拼接字符串
	 * @param strings
	 * @return
	 */
	public static String join(String... strings)
	{
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<strings.length; i++) {
			if (!isNull(strings[i])) {
				sb.append(strings[i]).append(":");
			}
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/**
	 * 重复填充字符串到len长度
	 * @param len 叠加次数
	 * @param str 字符串
	 * @return
	 */
	public static String copyString(int len, String str) 
	{
		String fStr = "";
		
		for (int i = 0; i < len; i++) {
			fStr += str;
		}
		
		return fStr;
	} 
	
	/**
	 * 当 obj 不为空时，转为字符串类型；为空时返回 ''
	 * @param obj 要转换的对象
	 * @return 转换后的字符串
	 */
	public static String toString(Object obj) 
	{
		return (obj == null?"":trim(obj.toString()));
	}
	
	/**
	 * obj 转字符串，为 null 时，替换为 defaultValue
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static String toString(Object obj, String defaultValue) 
	{
		return  (isObjectNull(obj))? defaultValue : obj.toString();
	}
	
	/**
	 * Object 转int
	 * @param obj
	 * @param defaultValue 转换失败给定的默认值
	 * @return
	 */
	public static int toInteger(Object obj, int defaultValue) {
		try {
			return Integer.parseInt(toString(obj, ""));
		} catch (Exception e) {

		}
		return defaultValue;
	}

	/**
	 * Object 转long
	 * @param obj
	 * @param defaultValue 转换失败给定的默认值
	 * @return
	 */
	public static long toLong(Object obj, long defaultValue) {
		try {
			return Long.parseLong(toString(obj, ""));
		} catch (Exception e) {

		}
		return defaultValue;
	}

	/**
	 * Object 转double
	 * @param obj
	 * @param defaultValue 转换失败给定的默认值
	 * @return
	 */
	public static double toDouble(Object obj, double defaultValue) {
		try {
			return Double.parseDouble(toString(obj, ""));
		} catch (Exception e) {

		}
		return defaultValue;
	}

	/**
	 * Object 转float
	 * @param obj
	 * @param defaultValue 转换失败给定的默认值
	 * @return
	 */
	public static float toFloat(Object obj, float defaultValue) {
		try {
			return Float.parseFloat(toString(obj, ""));
		} catch (Exception e) {

		}
		return defaultValue;
	}

	/**
	 * 若输入为字符为null,则输出forNull
	 */
	public static String trim(String str, String forNull) 
	{
		if(str==null) {
			return forNull;
		} else {
			return str.trim();
		}
	}
	
	/**
	 * 若输入为字符为null,则输出""
	 */
	public static String trim(String str) 
	{
		return trim(str, "");
	}
	
	/**
	 * 清除MAP所有为 NULL 的元素
	 * @param map MAP集合
	 * @return    除空后的集合
	 */
	public static Map<String, Object> trimNull(Map<String, Object> map)
	{
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = (Entry<String, Object>) it.next();
			if (entry.getValue() == null) {
				it.remove();
			}
		}
		return map;
	}
	
	/**
	 * 清除 List 所有为 NULL 的元素
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> trimNull(List<Map<String, Object>> list)
	{
		for (Map<String, Object> map: list)	{
			trimNull(map);
		}
		return list;
	}
	
	/**
	 * 按指定长度格式化输出整型字符串，不够位数则前补 0
	 * @return
	 */
	public static String format(long number, int length)
	{
		return String.format("%0" + length + "d", number);
	}
	
	/**
	 * 编码去除补0方法 (编码长度必须为偶数)
	 * @author mhq
	 */
	public static String remove00(String str) {
		char[] charArray = str.toCharArray();
		int index = -1;
		for (int i = charArray.length - 1; i > 0; i = i - 2) {
			if (charArray[i] != '0' || charArray[i - 1] != '0') {
				index = i;
				break;
			}
		}
		if (index < 0)
			return "";
		return String.valueOf(charArray, 0, index + 1);
	}
	
	/**
	 * 判断是否是空串，"   \r   \n  \r\n" 也算是空串
	 */
	public static boolean isEmpty(String input)
	{
		boolean isEmpty = true;		//默认是空串
		
		if(input!=null)
		{
			for(int i=0;i<input.length();i++)	//只要有一个字符不是 ' '，'\r','\n'，那它就是非空的
			{
				char c = input.charAt(i); 
				if( c!=' ' && c!='\r' && c!='\n' )
				{
					return false;			
				}
			}
		}
		
		return isEmpty;
	}
	
	/**
	 * lwh 
	 * 使用正则表达式判断是否为整数
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) 
	{
		String pattern = "^-?\\d+$";
		Pattern p = Pattern.compile(pattern); 
        Matcher m = p.matcher(str); 
        return m.find();
	}
	
	/**
	 * 判断字符串是否为浮点数
	 * @param str
	 * @return
	 */
	public static boolean isFloat(String str) 
	{
		String pattern = "^(-?\\d+)(\\.\\d+)?$";
		Pattern p = Pattern.compile(pattern); 
        Matcher m = p.matcher(str); 
        return m.find();
	}
	
	/**
	 * 检测有无特殊字符
	 * @param str  给定检测字符串
	 * @return     true:有特殊字符；false:无特殊字符  
	 */
	public static boolean isValidate(String str)
	{
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/\\\\?]";
        Pattern   p   =   Pattern.compile(regEx);       
        Matcher   m   =   p.matcher(str); 
        return m.find();
	}
	
	/**
	 * 判断是否是ip
	 * @param str 需要判断的字符串
	 * @return    true:ip地址，false:不是ip地址
	 */
	public static boolean isIP(String str)
	{
		String[] arr = str.split("\\.");
		if(arr.length!=4) {
			return false;
		}
		for(int i=0;i<arr.length;i++) {
			try {
				int num = Integer.parseInt(arr[i]);
				if(num<0||num>255) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 将对象转换成数字，转化失败将返回数字0
	 * @param o
	 * @return
	 */
	public static int nullToZero(Object o) {
		if(o==null){
			return 0;
		}
		int i = 0;
		try {
			i = Integer.valueOf(Integer.parseInt(o.toString()));
		} catch (Exception e) {
		}
		return i;
	}
	
	/**
	 * 将字符串进行BASE64 编码
	 * @param s
	 * @return
	 */
	public static String getBASE64(String s){
		if(s==null)return "";
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}
	
	/**
	 * 将BASE64 编码的字符串进行解码
	 * @param s
	 * @return
	 */
	public static String getFormBASE64(String s){
		if(s==null)return "";
		BASE64Decoder decoder = new BASE64Decoder();
		try{
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * 将时间转换成Quartz定时任务表达式
	 * @param timeStr 00:00:00 ~ 23:59:59 范围的时间字符串
	 * @return
	 */
	public static String time2CronExpression(String timeStr){
		String cronExpression = "";
		String [] time = timeStr.split(":");
		for(int i=time.length-1;i>=0;i--){
			cronExpression = cronExpression+Integer.valueOf(time[i].trim())+" ";
		}
		return cronExpression+" * * ?";
	}
	
	/**
	 * 获取下一个2为一级的编码 0-9 A-Z
	 * @param curCode
	 * @return
	 */
	public static String getNext2DigitCode(String curCode){
		
		char[] codeChar = curCode.toCharArray();
		int firstCode = (int)codeChar[0];
		int secondCode = (int)codeChar[1];
		
		if(secondCode + 1 > 57 && secondCode < 65){
			secondCode = 65;
		}else if(secondCode + 1 > 90){
			secondCode = 48;
			firstCode ++;
		}else{
			secondCode ++;
		}
		
		if(firstCode > 57 && firstCode < 65){
			firstCode = 65;
		}else if(firstCode > 90){
			firstCode = 48;//到了Z了,回到原位，理论不会有这么多编码了
		}
		byte[] asicllBtye = {(byte)firstCode, (byte) secondCode};
		
		return new String(asicllBtye);
	} 
	
	/**
	 * 获取下一个4为一级的编码 0-9 A-Z
	 * @param curCode 0001  000A 000E 0999
	 * @return
	 */
	public static String getNext4DigitCode(String curCode){
		
		char[] codeChar = curCode.toCharArray();
		int firstCode = (int)codeChar[0];
		int secondCode = (int)codeChar[1];
		int thirdCode = (int)codeChar[2];
		int lastCode = (int)codeChar[3];
		
		if (lastCode + 1 > 57 && lastCode < 65) {
			lastCode = 65;
		} else if (lastCode + 1 > 90) {
			lastCode = 48;
			thirdCode++;
		} else {
			lastCode++;
		}

		if (thirdCode + 1 > 57 && thirdCode < 65) {
			thirdCode = 65;
		} else if (thirdCode + 1 > 90) {
			thirdCode = 48;
			secondCode++;
		}
		
		if (secondCode + 1 > 57 && secondCode < 65) {
			secondCode = 65;
		} else if (secondCode + 1 > 90) {
			secondCode = 48;
			firstCode++;
		}
		
		if (firstCode > 57 && firstCode < 65) {
			firstCode = 65;
		} else if (firstCode > 90) {
			firstCode = 48;// 到了Z了,回到原位，理论不会有这么多编码了
		}
		
		byte[] asicllBtye = {(byte)firstCode, (byte) secondCode, (byte) thirdCode, (byte) lastCode};
		
		return new String(asicllBtye);
	} 
	
	/**
	 * 将字符串中的特殊字符转义es指定特殊字符，防止查询时报错
	 * @param s
	 * @return
	 */
	public static String escapeEsSpecial(String s) 
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if ((c == '\\') || (c == '+') || (c == '-') || (c == '!')
					|| (c == '(') || (c == ')') || (c == '^')
					|| (c == '[') || (c == ']') || (c == '"') || (c == '{')
					|| (c == '}') || (c == '~') || (c == '|') || (c == '&') 
					|| (c == '/')) {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		/* String text = "123456";  
	     String replacement = "two$two";  
	     replacement = replacement.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;  
	     String resultString = text.replaceAll("2", replacement);  
	     resultString = resultString.replaceAll("RDS_CHAR_DOLLAR", "\\$");// decode replacement;  
	     System.out.println(resultString);  */
	}
}
