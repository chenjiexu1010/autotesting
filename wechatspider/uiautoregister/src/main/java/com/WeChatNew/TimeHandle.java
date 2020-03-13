package com.WeChatNew;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeHandle {
    public static Date ParseXYD0(String s) {
        //String s = "11月20日 11:23";
        String text = "(\\d{2}):(\\d{2})";
        boolean yestday = false;
        if (s.contains("昨天")) {
            text = "昨天\\s+(\\d{2}):(\\d{2})";
            yestday = true;
        }
        Pattern pattern = Pattern.compile(text);

        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            String hh = matcher.group(1).toString();
            String ss = matcher.group(2).toString();

            Date now = new Date();

            String.valueOf(now.getYear());

            String astr = new SimpleDateFormat("yyyy-MM-dd").format(now);
            // 11/26/2015 12:08:23
            String str = String.format(" %s:%s:00",
                    hh,
                    ss
            );
            str = astr + str;
            Date d = ParseTime1(str);
            if (yestday) {
                d = addMinutes(d, -60 * 24);
            }
            return d;
        } else {
            return null;
        }
    }

    public static Date addMinutes(Date d, int minutes) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.MINUTE, minutes);
        d = calendar.getTime();   //这个时间就是日期往后推一天的结果
        return d;
    }

    public static Date ParseTime1(String dateString) {
        if (dateString.equals("")) return null;
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(dateString);
            //format = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
            //dateString = format.format(date);
        } catch (Exception ex) {
        }
        return date;
    }

    public static Date ParseXYD1(String s) {
        //String s = "11月20日 11:23";
        //Pattern pattern = Pattern.compile("(\\d{2})月(\\d{2})日\\s+(\\d{2}):(\\d{2})");

        //Matcher matcher = pattern.matcher(s);

        try {
            String yy = s.substring(0, s.indexOf("年"));
            String mm = s.substring(s.indexOf("年") + 1, s.indexOf("月"));
            String dd = s.substring(s.indexOf("月") + 1, s.indexOf("日"));
            String hh = s.substring(s.indexOf(" ") + 1, s.indexOf(":"));
            String ss = s.substring(s.indexOf(":") + 1);

            // 11/26/2015 12:08:23
            String str = String.format("%s/%s/%s %s:%s:00",
                    mm,
                    dd,
                    yy,// String.valueOf(new Date().getYear()),
                    hh,
                    ss
            );
            Date d = ParseTime(str);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date ParseTime(String dateString) {
        if (dateString.equals("")) return null;
        Date date = null;
        try {
            dateString = dateString.replaceAll("/", "-");
            // 11/26/2015 12:08:23
            // yyyy-MM-dd'T'HH:mm+s:SSSS
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            date = format.parse(dateString);
            //format = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
            //dateString = format.format(date);
        } catch (Exception ex) {
        }
        return date;
    }

}

