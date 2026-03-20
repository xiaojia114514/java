package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName Address
 */
@TableName(value ="Address")
@Data
public class Address implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer aid;

    private String uaccount;

    private String atext;

    private Float adefault;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getUaccount() {
        return uaccount;
    }

    public void setUaccount(String uaccount) {
        this.uaccount = uaccount;
    }

    public String getAtext() {
        return atext;
    }

    public void setAtext(String atext) {
        this.atext = atext;
    }

    public Float getAdefault() {
        return adefault;
    }

    public void setAdefault(Float adefault) {
        this.adefault = adefault;
    }
}
