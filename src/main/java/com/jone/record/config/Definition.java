package com.jone.record.config;

public class Definition {
    /**
     * 专题-基础信息图片
     */
    public static final int TYPE_FILE_SPECIAL = 1;
    /**
     * 专题-栏目附件
     */
    public static final int TYPE_FILE_COLUMN = 2;
    /**
     * 专题-栏目图集(内容附件)
     */
    public static final int TYPE_FILE_IMAGE = 3;
    /**
     * 课堂-课程封面
     */
    public static final int TYPE_FILE_COURSE = 4;

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
