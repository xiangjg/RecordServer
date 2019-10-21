package com.jone.record.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.entity.vo.ImageCode;
import com.jone.record.util.Md5PasswordEncoder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoginController extends BaseController{
    @RequestMapping(value = "/findByLoginName", method = RequestMethod.POST)
    @ApiOperation(value = "登录验证", notes = "传入登录用户名以及密码,返回{session:session标识,userName:真实姓名,role:角色ID,proId:所属工程Id}")
    @ApiImplicitParam(name = "object", value = "{loginName:loginName,psw:psw,thirdId:第三方平台用户ID}")
    public void findByLoginName(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject object) {
        BaseData bd = new BaseData();
        SUserEntity user = null;
        String imageCode = request.getSession().getAttribute("imageCode").toString();
        try {
            if (StringUtils.isEmpty(object.getString("loginName"))) {
                bd.setMessage("请填写登录名");
                bd.setCode(-1);
            }else if(StringUtils.isEmpty(object.getString("verify"))){
                bd.setMessage("请填写验证码");
                bd.setCode(-1);
            }else if(!imageCode.toUpperCase().equals(object.getString("verify").toUpperCase())){
                bd.setMessage("验证码错误");
                bd.setCode(-1);
            } else {
                user = userService.findByLoginNameOrMobile(object.getString("loginName"));
                if (null == user) {
                    bd.setMessage("用户不存在");
                    bd.setCode(-1);
                } else {
                    String pwd = Md5PasswordEncoder.encrypt(object.getString("psw"), user.getLoginName());
                    if (pwd.equals(user.getPassword())) {
                        pushToken(user,bd,response);
                        //判断是否是第三方平台登录
                        if(null!=object.get("thirdId")){
                            SThirdUserEntity thirdUserEntity = new SThirdUserEntity();
                            thirdUserEntity.setThirdId(object.getString("thirdId"));
                            thirdUserEntity.setUserId(user.getId());
                            thirdUserDao.save(thirdUserEntity);
                        }
                    } else {
                        bd.setMessage("密码错误");
                        bd.setCode(-1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bd.setMessage(e.getMessage());
            bd.setCode(-2);
        } finally {
            this.printJson(bd, response);
        }
    }

    @RequestMapping(value = "/codeImage", method = RequestMethod.GET)
    @ApiOperation(value = "登录验证码")
    public void codeImage(HttpServletRequest request, HttpServletResponse response)throws Exception {
        //生成imageCode对象
        ImageCode imageCode = createImageCode();
        //将图形验证码存入到session中
        request.getSession().setAttribute("imageCode", imageCode.getCode());
        // 将生成的图片写到接口的响应中
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }


    private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z',  '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private ImageCode createImageCode() {
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 80; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String code = "";
        for (int i = 0; i < 4; i++) {
            String rand =String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            code += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();

        return new ImageCode(image, code, 60);
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    //用户验证成功后颁发token
    private void pushToken(SUserEntity user,BaseData bd,HttpServletResponse response){
        //2.颁发token
        String session = StringUtil.GetGUID();
        UserInfo userInfo = new UserInfo();
        userInfo.setDt(new Date());
        userInfo.setSession(session);
        userInfo.setLoginName(user.getLoginName());
        userInfo.setRoleId(user.getRoleId());
        userInfo.setProId(user.getProId());
        userInfo.setUserId(user.getId() + "");
        userInfo.setUserName(user.getUserName());
        String sql = "SELECT job_group_id FROM  b_system.user_group  where user_id=" + user.getId() + "";
        java.util.List<Map<String, Object>> list = dao.findBySqlToMap(sql);
        java.util.List<Integer> listGroup = new ArrayList<>();
        for (Map map1 : list) {
            listGroup.add(Integer.valueOf(map1.get("job_group_id").toString()));
        }
        userInfo.setJobGroups(listGroup);
        //获取角色权限
        String sqlRole = "SELECT * FROM b_system.s_role where id=" + user.getRoleId() + "";
        List<Map<String, Object>> listRole = dao.findBySqlToMap(sqlRole);
        String roleName = "";
        if (listRole.size() == 1) {
            Map mapRole = listRole.get(0);
            userInfo.setRights(BigInteger.valueOf(Long.valueOf(mapRole.get("rights").toString())));
            roleName = mapRole.get("name").toString();
        }
        ////token 是 loginName
        redisDao.setKey(user.getLoginName(), JSON.toJSONString(userInfo));
        JSONObject object1 = new JSONObject();
        //object1.put("session", session);
        object1.put("userName", user.getUserName());
        object1.put("role", user.getRoleId());
        object1.put("proId", user.getProId());
        object1.put("token", user.getLoginName());
        object1.put("userId", user.getId());
        object1.put("jobGroup", listGroup);
        object1.put("roleName", roleName);
        response.setHeader("session", session);
        bd.setCode(1);
        bd.setData(object1);
    }
}
