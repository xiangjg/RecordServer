package com.jone.record.config;

public class Definition {
    /**
     * 专题基础信息图片
     */
    public static final int TYPE_FILE_SPECIAL = 1;
    /**
     * 个人中心消息类型定义：未读
     */
    public static final int TYPE_MSG_NO_READ = 0;
    /**
     * 个人中心消息类型定义：已读
     */
    public static final int TYPE_MSG_READ = 1;
    /**
     * 个人中心消息类型定义：删除
     */
    public static final int TYPE_MSG_DELETE = -1;

    public static final int TYPE_STATE_INVALID = 0;//无效
    public static final int TYPE_STATE_VALID = 1;//有效
    public static final int TYPE_STATE_DELETE = 2;//删除
}
