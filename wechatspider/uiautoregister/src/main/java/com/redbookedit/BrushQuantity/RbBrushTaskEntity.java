package com.redbookedit.BrushQuantity;

import java.util.Date;

public class RbBrushTaskEntity {
    private int Id;
    private String AuthName;
    private String NoteTitle;
    private String CreateTime;
    private int ExcutedTimes;
    private int Status;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAuthName() {
        return AuthName;
    }

    public void setAuthName(String AuthName) {
        AuthName = AuthName;
    }

    public String getNoteTitle() {
        return NoteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        NoteTitle = noteTitle;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getExcutedTimes() {
        return ExcutedTimes;
    }

    public void setExcutedTimes(int excutedTimes) {
        ExcutedTimes = excutedTimes;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "RbBrushTaskEntity{" +
                "Id=" + Id +
                ", AuthName='" + AuthName + '\'' +
                ", NoteTitle='" + NoteTitle + '\'' +
                ", CreateTime=" + CreateTime +
                ", ExcutedTimes=" + ExcutedTimes +
                ", Status=" + Status +
                '}';
    }
}
