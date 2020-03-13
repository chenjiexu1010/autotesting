package com.qq;

/**
 * Created by Jeqee on 2015/12/28.
 */
public class QQDate {
    public QQDate(String nick, String password) {
        this.nick = nick;
        this.password = password;
    }
    private String uin;
    private String nick;
    private String phoneNum;
    private String password;

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
