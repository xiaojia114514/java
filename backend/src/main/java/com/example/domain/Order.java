package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName Order
 */
@TableName(value ="SystemOrder")
@Data
public class Order implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer oid;

    private Integer gid;

    private Integer eid;

    private String uaccount;

    private String ophone;

    private String oaddress;

    private Integer onumber;

    private String oremark;

    private Integer ostate;

    private Integer opaid;

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getOphone() {
        return ophone;
    }

    public void setOphone(String ophone) {
        this.ophone = ophone;
    }

    public String getUaccount() {
        return uaccount;
    }

    public void setUaccount(String uaccount) {
        this.uaccount = uaccount;
    }

    public String getOremark() {
        return oremark;
    }

    public void setOremark(String oremark) {
        this.oremark = oremark;
    }

    public String getOaddress() {
        return oaddress;
    }

    public void setOaddress(String oaddress) {
        this.oaddress = oaddress;
    }

    public Integer getOstate() {
        return ostate;
    }

    public void setOstate(Integer ostate) {
        this.ostate = ostate;
    }

    public Integer getOnumber() {
        return onumber;
    }

    public Integer getOpaid() {
        return opaid;
    }

    public void setOpaid(Integer opaid) {
        this.opaid = opaid;
    }

    public void setOnumber(Integer onumber) {
        this.onumber = onumber;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
