package com.fclub.utils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KuangUtils<T> {

    static boolean printFlag = true;

    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static Timestamp getTime(){
        return new Timestamp(new Date().getTime());
    }

    public static void print(String msg){
        if (printFlag){
            System.out.println("kuangshen:=>"+msg);
        }
    }

    public static String getIP(HttpServletRequest request){
        String ip=request.getHeader("x-forwarded-for");
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("X-Real-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
        }
        return ip;
    }

    public static String toString(String obj){
        return obj == null ? "" : trim(obj.toString());
    }

    public static String trim(String str, String forNull) {
        return str == null ? forNull : str.trim();
    }

    public static String trim(String str) {
        return trim(str, "");
    }




    public static String getSubTitle(String title){
        String pattern = "!\\[\\]\\(.*\\)";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(title);
        return null;
    }


}
