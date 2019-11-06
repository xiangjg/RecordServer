package com.jone.record.service.impl;


import com.jone.record.dao.map.PoisEntityDao;
import com.jone.record.dao.map.ShareEntityDao;
import com.jone.record.entity.map.PoisEntity;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.service.MapService;
import org.geotools.data.shapefile.files.ShpFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service(value = "mapService")
public class MapServiceImpl implements MapService {

    @Autowired
    private PoisEntityDao poisEntityDao;
    @Autowired
    private ShareEntityDao shareEntityDao;

    @Override
    public List<PoisEntity> listPoisByState(Integer state) throws Exception {
        return poisEntityDao.findByState(state);
    }

    @Override
    public PoisEntity save(PoisEntity poisEntity, List<MultipartFile> files) throws Exception {
        if(files!=null&&files.size()>0){
            MultipartFile file = files.get(0);
            //TODO 保存shp文件
        }
        poisEntity = poisEntityDao.save(poisEntity);
        return poisEntity;
    }

    @Override
    public void deletePoisEntity(Integer id) throws Exception {
        poisEntityDao.deleteById(id);
    }

    @Override
    public List<ShareEntity> listShareByState(Integer state) throws Exception {
        return shareEntityDao.findByState(state);
    }

    @Override
    public ShareEntity save(ShareEntity shareEntity) throws Exception {
        return shareEntityDao.save(shareEntity);
    }

    @Override
    public void deleteShareEntity(Integer id) throws Exception {
        shareEntityDao.deleteById(id);
    }
}
