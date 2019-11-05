package com.jone.record.controller.common;

import com.jone.record.controller.BaseController;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.file.FileEntity;
import com.jone.record.entity.vo.BaseData;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.FileService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
@Api(tags = "文件相关接口")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/saveFile",method = RequestMethod.POST)
    @ApiOperation(value="上传文件", notes="传入文件类型type和文件数据列表file")
    public void saveFile(@RequestParam Integer type,  HttpServletRequest request, HttpServletResponse response){
        BaseData baseData = new BaseData();
        try{
            UserInfo userInfo = getRedisUser(request,redisDao);
            if(null==userInfo){
                baseData.setCode(-2);
                baseData.setMessage("用户登录没有登录,或session过期");
            }else {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                List<MultipartFile> files = multiRequest.getFiles("file");
                baseData.setData(fileService.upload(files, type, userInfo));
                baseData.setCode(1);
            }
        }catch (Exception e){
            e.printStackTrace();
            baseData.setMessage(e.getMessage());
            baseData.setCode(-1);
        }finally {
            this.printJson(baseData,response);
        }
    }

    @RequestMapping(value = "/getFile",method = RequestMethod.GET)
    @ApiOperation(value="下载文件", notes="传入文件ID值fileId")
    public void getFile(@RequestParam Integer fileId,  HttpServletRequest request, HttpServletResponse response){
        ServletOutputStream out = null;
        try{
            out = response.getOutputStream();
            FileEntity fileEntity = fileService.getFile(fileId);
            response.setContentType(fileEntity.getContentType());
            response.setHeader("Content-disposition", "attachment;filename="
                    + getFileName(request, response, fileEntity.getName()));
            response.setContentLength(fileEntity.getData().length);
            out.write(fileEntity.getData());
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/deleteFile",method = RequestMethod.POST)
    @ApiOperation(value="删除文件", notes="传入文件ID值fileId")
    public void deleteFile(@RequestParam Integer fileId,  HttpServletResponse response){
        try{
            fileService.deleteFile(fileId);
            printJson(ResultUtil.success(), response);
        }catch (Exception e){
            e.printStackTrace();
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/findFile",method = RequestMethod.POST)
    @ApiOperation(value="查找附件", notes="传入关联编码refId及文件类型type")
    public void deleteFile(@RequestParam Integer refId,@RequestParam Integer type,  HttpServletResponse response){
        try{
            List<FileEntity> fileEntityList = fileService.listByRefIdAndType(refId, type);
            printJson(ResultUtil.success(), response);
        }catch (Exception e){
            e.printStackTrace();
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
