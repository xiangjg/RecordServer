package com.jone.record.entity.vo;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xiangjg
 * @date 2019/7/24 9:36
 */
public class ImageCode implements Serializable {
    private BufferedImage image;
    private  String code;
    private LocalDateTime expireTime;//过期时间
    /**
     *
     * @param image
     * @param code
     * @param expireInt :该参数是过期时间秒数,如60
     */
    public ImageCode(BufferedImage image, String code, int expireInt) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireInt);//当前时间加上60秒
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
