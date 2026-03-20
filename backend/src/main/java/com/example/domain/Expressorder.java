package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value ="ExpressOrder")
@Data
public class Expressorder implements Serializable{

    @TableId(value = "EOID", type = IdType.AUTO)
    private Integer eoid;

    @TableField(value = "OID")
    private Integer oid;

    @TableField(value = "EOState")
    private Integer eostate;

    @TableField(value = "EOTime")
    private Date eotime;

    @TableField(value = "EOAddress")
    private String eoaddress;

    public Integer getEoid() {
        return eoid;
    }

    public void setEoid(Integer eoid) {
        this.eoid = eoid;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getEostate() {
        return eostate;
    }

    public void setEostate(Integer eostate) {
        this.eostate = eostate;
    }

    public Date getEotime() {
        return eotime;
    }

    public void setEotime(Date eotinme) {
        this.eotime = eotinme;
    }

    public String getEoaddress() {
        return eoaddress;
    }

    public void setEoaddress(String eoaddress) {
        this.eoaddress = eoaddress;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
