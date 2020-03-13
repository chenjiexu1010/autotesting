package com.redbookedit;

public class RedBookTaskEdit {
    public int Id;

    public int TaskId;

    public String Title;

    public String Target;

    public String Account;

    public String Password;

    public String NickName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getNickName() {
        return NickName;
    }

    public int getTaskId() {
        return TaskId;
    }

    public String getAccount() {
        return Account;
    }

    public String getPassword() {
        return Password;
    }

    public String getTarget() {
        return Target;
    }

    public String getTitle() {
        return Title;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public void setTaskId(int taskId) {
        TaskId = taskId;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
