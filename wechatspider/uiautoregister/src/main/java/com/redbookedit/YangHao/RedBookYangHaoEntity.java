package com.redbookedit.YangHao;

import java.util.ArrayList;
import java.util.Date;

public class RedBookYangHaoEntity {
    private int Id;
    private String RedBookAccount;
    private String NickName;
    private String Keys;
    private String LoginAccount;
    private String LoginPassword;
    private ArrayList<String> KeysList;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRedBookAccount() {
        return RedBookAccount;
    }

    public void setRedBookAccount(String redBookAccount) {
        RedBookAccount = redBookAccount;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getKeys() {
        return Keys;
    }

    public void setKeys(String keys) {
        Keys = keys;
    }

    public String getLoginAccount() {
        return LoginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        LoginAccount = loginAccount;
    }

    public String getLoginPassword() {
        return LoginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        LoginPassword = loginPassword;
    }

    public ArrayList<String> getKeysList() {
        return KeysList;
    }

    public void setKeysList(ArrayList<String> keysList) {
        KeysList = keysList;
    }

    @Override
    public String toString() {
        return "RedBookYangHaoEntity{" +
                "Id=" + Id +
                ", RedBookAccount='" + RedBookAccount + '\'' +
                ", NickName='" + NickName + '\'' +
                ", Keys='" + Keys + '\'' +
                ", LoginAccount='" + LoginAccount + '\'' +
                ", LoginPassword='" + LoginPassword + '\'' +
                ", KeysList=" + KeysList +
                '}';
    }
}
