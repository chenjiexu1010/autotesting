package com.inSocial;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Jeqee on 2015/9/24.
 * 提交的model
 */
public class SubmitModel {
    public SubmitModel(int id) {
        ID = id;
        GetTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Status = 1;
    }

    public int ID = 0;


    public String Logs = "";

    /// <summary>
    /// 评论内容
    /// </summary>
    public List<InSocialCommentModel> CommentInfo = new ArrayList<InSocialCommentModel>();
    /// <summary>
    /// 聊天内容
    /// </summary>
    public List<InSocialChatModel> MessageInfo = new ArrayList<InSocialChatModel>();

    public String FriendCurrentID = "";
    public String FollowCurrentID = "";
    public int FriendSuccessNum = 0;
    public int FollowSuccessNum = 0;
    public int IsOverFriend = 0;

    /// <summary>
    /// 图片任务完成情况，0，1
    /// </summary>
    public int PublishPictureState = 0;
    /// <summary>
    /// 发布图片时间
    /// </summary>
    public String PublishPictureTime = "";

    /// <summary>
    /// 0为有失败，1为都成功,2为被封号
    /// </summary>
    public int Status = 1;

    public String GetTime = "";
}
