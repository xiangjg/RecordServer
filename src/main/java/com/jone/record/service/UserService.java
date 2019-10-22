package com.jone.record.service;

import com.jone.record.entity.system.UserEntity;
import org.springframework.data.domain.Page;

public interface UserService {

    UserEntity findByLoginNameOrMobile(String loginName) throws Exception;
    UserEntity addUser(UserEntity user) throws Exception;
    void updateUser(UserEntity user) throws Exception;
    void delUser(Integer userId) throws Exception;
    void resetPassword(String loginName) throws Exception;
    void updatePassword(String loginName, String password) throws Exception;
    Page<UserEntity> listUser(int page, int size, String keyword) throws Exception;
}
