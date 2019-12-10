package com.jone.record.dao.system;

import com.jone.record.entity.system.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update RoleEntity r set r.rights = :rights where r.id = :id ")
    Integer updateRights(@Param("id") Integer id, @Param("rights") String rights);
    @Transactional
    @Modifying @Query("update RoleEntity r set r.add = :add where r.id = :id ")
    Integer updateAdd(@Param("id") Integer id, @Param("add") String add);
    @Transactional
    @Modifying @Query("update RoleEntity r set r.del = :del where r.id = :id ")
    Integer updateDel(@Param("id") Integer id, @Param("del") String del);
    @Transactional
    @Modifying @Query("update RoleEntity r set r.change = :change where r.id = :id ")
    Integer updateChange(@Param("id") Integer id, @Param("change") String change);
    @Transactional
    @Modifying @Query("update RoleEntity r set r.query = :query where r.id = :id ")
    Integer updateQuery(@Param("id") Integer id, @Param("query") String query);
}
