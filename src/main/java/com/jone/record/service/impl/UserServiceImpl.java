package com.jone.record.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.jone.record.dao.system.DepartDao;
import com.jone.record.dao.system.RoleDao;
import com.jone.record.dao.system.UserDao;
import com.jone.record.entity.system.DepartEntity;
import com.jone.record.entity.system.RoleEntity;
import com.jone.record.entity.system.UserEntity;
import com.jone.record.entity.vo.PageParamVo;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.service.UserService;
import com.jone.record.util.Md5PasswordEncoder;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DepartDao departDao;
    @Autowired
    private RoleDao roleDao;


    @Override
    public UserEntity findByLoginNameOrMobile(String loginName) throws Exception {
        return userDao.findByLoginNameOrTel(loginName, loginName);
    }

    @Override
    public UserEntity addUser(UserEntity user) throws Exception {
        UserEntity userEntity = userDao.findByLoginNameOrTel(user.getLoginName(), user.getTel());
        if (userEntity != null)
            throw new Exception("用户已存在");
        user.setPassword(Md5PasswordEncoder.encrypt("123456", user.getLoginName()));
        user.setState(1);
        user.setRegDt(new Date());
        return userDao.save(user);
    }

    @Override
    public void updateUser(UserEntity user) throws Exception {
        UserEntity userEntity = userDao.findById(user.getId()).orElse(null);
        if (userEntity != null) {
            userEntity.setName(user.getName());
            userEntity.setDuty(user.getDuty());
            userEntity.setEmail(user.getEmail());
            userEntity.setTel(user.getTel());
            userEntity.setRemark(user.getRemark());
            userEntity.setRole(user.getRole());
            userEntity.setDepart(user.getDepart());
            userDao.save(userEntity);
        }

    }

    @Override
    public void delUser(Integer userId) throws Exception {
        userDao.deleteById(userId);
    }

    @Override
    public void resetPassword(String loginName) throws Exception {
        userDao.modifyPasswordByLoginName(Md5PasswordEncoder.encrypt("123456", loginName), loginName);
    }

    @Override
    public void updatePassword(String loginName, String password) throws Exception {

    }

    @Override
    public PageVo<UserEntity> listUser(PageParamVo pageParamVo) throws Exception {
        Specification specification = new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(pageParamVo.getKeyword()))
                    predicates.add(criteriaBuilder.like(root.get("name"), pageParamVo.getKeyword()));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pageRequest = PageRequest.of(pageParamVo.getPage() - 1, pageParamVo.getSize(), sort);
        Page<UserEntity> pageList = userDao.findAll(specification, pageRequest);
        PageVo<UserEntity> page = new PageVo<>();
        for (UserEntity u : pageList.getContent()
        ) {
            u.setPassword("");
        }
        page.setPage(pageParamVo.getPage());
        page.setSize(pageParamVo.getSize());
        page.setTotal(pageList.getTotalElements());
        page.setTotalPages(pageList.getTotalPages());
        page.setData(pageList.getContent());
        return page;
    }

    @Override
    public UserEntity findUserByLoginNameAndPwd(String loginName, String password) throws Exception {
        return userDao.findByLoginNameAndPassword(loginName, Md5PasswordEncoder.encrypt(password, loginName));
    }

    @Override
    public Map<String, Object> listDepartAndRole() throws Exception {
        Map<String, Object> data = new HashedMap();
        List<DepartEntity> departList =departDao.findAll();
        List<RoleEntity> roleList = roleDao.findAll();
        data.put("roleList", roleList);
        data.put("departList", departList);
        return data;
    }
}
