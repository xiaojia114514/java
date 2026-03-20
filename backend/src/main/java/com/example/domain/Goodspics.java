package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Arrays;
import lombok.Data;

/**
 * 
 * @TableName GoodsPics
 */
@TableName(value ="GoodsPics")
@Data
public class Goodspics implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer picid;

    /**
     * 
     */
    private Integer gid;

    /**
     * 
     */
    private byte[] picdata;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getPicid() {
        return picid;
    }

    public void setPicid(Integer picid) {
        this.picid = picid;
    }

    public byte[] getPicdata() {
        return picdata;
    }

    public void setPicdata(byte[] picdata) {
        this.picdata = picdata;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Goodspics other = (Goodspics) that;
        return (this.getPicid() == null ? other.getPicid() == null : this.getPicid().equals(other.getPicid()))
            && (this.getGid() == null ? other.getGid() == null : this.getGid().equals(other.getGid()))
            && (Arrays.equals(this.getPicdata(), other.getPicdata()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPicid() == null) ? 0 : getPicid().hashCode());
        result = prime * result + ((getGid() == null) ? 0 : getGid().hashCode());
        result = prime * result + (Arrays.hashCode(getPicdata()));
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", picid=").append(picid);
        sb.append(", gid=").append(gid);
        sb.append(", picdata=").append(picdata);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}