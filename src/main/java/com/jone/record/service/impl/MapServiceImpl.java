package com.jone.record.service.impl;


import com.jone.record.dao.center.CenterMsgDao;
import com.jone.record.dao.map.PoisEntityDao;
import com.jone.record.dao.map.ShareEntityDao;
import com.jone.record.entity.center.CenterMsg;
import com.jone.record.entity.map.PoisEntity;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.MapService;
import org.geotools.data.shapefile.files.ShpFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service(value = "mapService")
public class MapServiceImpl implements MapService {

    @Autowired
    private PoisEntityDao poisEntityDao;
    @Autowired
    private ShareEntityDao shareEntityDao;
    @Autowired
    private CenterMsgDao centerMsgDao;

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
    public ShareEntity auditShare(Integer shareId, Integer state, UserInfo user) throws Exception {
        ShareEntity shareEntity = shareEntityDao.findById(shareId).orElse(null);
        if(shareEntity == null)
            throw new Exception("不存在该记录ID");
        shareEntity.setAuditDt(new Date());
        shareEntity.setAuditId(user.getUserId().toString());
        shareEntity.setState(state);

        String msg = "";
        if(state == 1)
            msg = "通过";
        else if(state == 0)
            msg = "不通过";
        CenterMsg centerMsg = new CenterMsg();
        centerMsg.setFromId(user.getUserId().toString());
        centerMsg.setInsertDt(new Date());
        centerMsg.setBody("您上传的[志慧共享]资料【"+shareEntity.getTitle()+"】审核"+msg);
        centerMsg.setTitle("志慧共享上传资料审核"+msg);
        centerMsg.setToId(shareEntity.getCreator());
        centerMsg.setType(3);
        centerMsg.setState(0);
        centerMsgDao.save(centerMsg);
        shareEntity = shareEntityDao.save(shareEntity);
        return shareEntity;
    }

    @Override
    public void deleteShareEntity(Integer id) throws Exception {
        ShareEntity shareEntity = shareEntityDao.findById(id).orElse(null);
        if(shareEntity == null)
            throw new Exception("不存在该记录ID");
        shareEntity.setState(-1);
        shareEntityDao.save(shareEntity);
    }
}
