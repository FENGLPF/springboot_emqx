package com.lpf.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CommonUtil {

    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijkmnpqrstuvwxyz0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(34);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static Date dateAddHours(Date date,int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
}
