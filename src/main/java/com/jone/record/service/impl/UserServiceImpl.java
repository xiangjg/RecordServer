package com.jone.record.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.jone.record.dao.system.UserDao;
import com.jone.record.entity.system.UserEntity;
import com.jone.record.entity.vo.PageParamVo;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.service.UserService;
import com.jone.record.util.Md5PasswordEncoder;
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
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserEntity findByLoginNameOrMobile(String loginName) throws Exception {
        return userDao.findByLoginNameOrTel(loginName, loginName);
    }

    @Override
    public UserEntity addUser(UserEntity user) throws Exception {
        UserEntity userEntity = userDao.findByLoginNameOrTel(user.getLoginName(), user.getTel());
        if (userEntity != null)
            throw new Exception("用户以存在");
        user.setPassword(Md5PasswordEncoder.encrypt("123456", user.getLoginName()));
        return userDao.save(user);
    }

    @Override
    public void updateUser(UserEntity user) throws Exception {

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
        for (UserEntity u:pageList.getContent()
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
}
