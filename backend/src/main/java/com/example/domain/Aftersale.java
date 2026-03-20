package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName Aftersale
 */
@TableName(value ="Aftersale")
@Data
public class Aftersale implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer afid;

    private Integer oid;

    private String afname;

    private String aftext;

    private Integer afstate;

    private Date aftime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getAfid() {
        return afid;
    }

    public void setAfid(Integer afid) {
        this.afid = afid;
    }

    public Date getAftime() {
        return aftime;
    }

    public void setAftime(Date aftime) {
        this.aftime = aftime;
    }

    public Integer getAfstate() {
        return afstate;
    }

    public void setAfstate(Integer afstate) {
        this.afstate = afstate;
    }

    public String getAftext() {
        return aftext;
    }

    public void setAftext(String aftext) {
        this.aftext = aftext;
    }

    public String getAfname() {
        return afname;
    }

    public void setAfname(String afname) {
        this.afname = afname;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }
}
