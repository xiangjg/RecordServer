package com.jone.record.service;

import com.jone.record.entity.center.CenterBookmarks;
import com.jone.record.entity.center.CenterFavorite;
import com.jone.record.entity.center.CenterMsg;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.entity.vo.UserInfo;

import java.util.List;

public interface UserCenterService {
    /**
     * 获取个人中心消息
     * @param userId 用户ID
     * @param state 消息状态
     * @param type 消息类型
     * @return
     * @throws Exception
     */
    PageVo<CenterMsg> getMyCenterMsg(Integer userId, Integer state, Integer type, int page, int size) throws Exception;

    /**
     * 保存用户消息
     * @param centerMsg
     * @return
     * @throws Exception
     */
    CenterMsg saveCenterMsg(CenterMsg centerMsg)throws Exception;

    /**
     * 阅读消息
     * @param msgId 消息ID
     * @param userInfo 登录用户信息
     * @throws Exception
     */
    void readMsg(Integer msgId, UserInfo userInfo)throws Exception;

    /**
     * 删除消息
     * @param msgId 消息ID
     * @param userInfo 登录用户信息
     * @throws Exception
     */
    void deleteCenterMsg(Integer msgId, UserInfo userInfo)throws Exception;

    List<CenterMsg> listNotDel(String userId) throws Exception;

    PageVo<CenterFavorite> listByState(Integer state, int page, int size, UserInfo userInfo) throws Exception;

    CenterFavorite saveCenterFavorite(CenterFavorite centerFavorite, UserInfo userInfo) throws Exception;

    void deleteCenterFavorite(Integer id, UserInfo userInfo) throws Exception;

    PageVo<CenterBookmarks> listCenterBookmarks(Integer state, Integer type, int page, int size, UserInfo userInfo) throws Exception;

    CenterBookmarks saveCenterBookmarks(CenterBookmarks centerBookmarks, UserInfo userInfo) throws Exception;

    void deleteCenterBookmarks(Integer id, UserInfo userInfo) throws Exception;
}
