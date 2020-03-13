package com.inSocial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeqee on 2015/9/24.
 * 请求的Model
 */
public class RequestModel {
    /// <summary>
    /// InSocial主键
    /// </summary>
    public int ID;
    /// <summary>
    /// InID
    /// </summary>
    public String InID;
    /// <summary>
    /// In昵称
    /// </summary>
    public String Name = "";
    /// <summary>
    /// 密码
    /// </summary>
    public String Password = "";
    /// <summary>
    /// 关注的数据集
    /// </summary>
    public List<String> FollowList = new ArrayList();
    /// <summary>
    /// 好友数据集
    /// </summary>
    public List<String> FriendList = new ArrayList();
    /// <summary>
    /// 好友验证信息集合
    /// </summary>
    public List<String> ReplyList = new ArrayList();

    /// <summary>
    /// 剩余任务
    /// </summary>
    public int FollowRemained = 0;

    /// <summary>
    /// 剩余任务
    /// </summary>
    public int FriendRemained = 0;


    /// <summary>
    /// 自动回复发送的图片
    /// </summary>
    public String ReplyPicture = "";


    /// <summary>
    /// 发布的图片
    /// </summary>
    public String PublishPicture = "";
    /// <summary>
    /// 发布的简介
    /// </summary>
    public String PublishDesc = "";


    /// <summary>
    /// in昵称
    /// </summary>
    public String InName = "";
    /// <summary>
    /// in简介
    /// </summary>
    public String InDesc = "";
    /// <summary>
    /// in地址
    /// </summary>
    public String InAddress = "";
    /// <summary>
    /// in头像
    /// </summary>
    public String InImage = "";
    /// <summary>
    /// in性别
    /// </summary>
    public String InGender = "";

    public List<InSocialCommentModel> CommentList = new ArrayList<InSocialCommentModel>();
    /// <summary>
    /// 聊天内容，Name,Text
    /// </summary>

    public List<InSocialChatModel> MessageList = new ArrayList<InSocialChatModel>();
    /// <summary>
    public List<String> ChatContent = new ArrayList<String>();
}
