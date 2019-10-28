package com.jone.record.service.impl;

import com.jone.record.dao.common.TypeDao;
import com.jone.record.entity.common.TypeEntity;
import com.jone.record.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "typeService")
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;

    @Override
    public TypeEntity save(TypeEntity typeEntity) throws Exception {
        TypeEntity typeEntity1 = findByType(typeEntity.getType(), typeEntity.getRef());
        if(typeEntity1!=null)
            throw new Exception("该类型已经定义");
        typeEntity = typeDao.save(typeEntity);
        return typeEntity;
    }

    @Override
    public TypeEntity findByType(Integer type, String ref) throws Exception {
        return typeDao.findByTypeAndRef(type, ref);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        TypeEntity typeEntity = typeDao.findById(id).orElse(null);
        if (typeEntity != null)
            typeDao.deleteById(id);
    }

    @Override
    public List<TypeEntity> listAll() throws Exception {
        return typeDao.findAll();
    }
}
