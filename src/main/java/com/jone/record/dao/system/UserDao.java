package com.jone.record.dao.system;

import com.jone.record.entity.system.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserDao extends JpaSpecificationExecutor<UserEntity>,JpaRepository<UserEntity, Integer> {

    UserEntity findByLoginNameOrTel(String loginName, String tel);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.password = ?1 where u.loginName = ?2")
    int modifyPasswordByLoginName(String password, String loginName);
}
