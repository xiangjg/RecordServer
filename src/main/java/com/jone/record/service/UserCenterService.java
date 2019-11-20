package com.jone.record.service;

import com.jone.record.entity.center.CenterMsg;
import com.jone.record.entity.vo.UserInfo;

import java.util.List;

public interface UserCenterService {

    List<CenterMsg> getMyCenterMsg(Integer userId, Integer state, Integer type) throws Exception;

    CenterMsg saveCenterMsg(CenterMsg centerMsg)throws Exception;

    void readMsg(Integer msgId, UserInfo userInfo)throws Exception;

    void deleteCenterMsg(Integer msgId, UserInfo userInfo)throws Exception;
}
