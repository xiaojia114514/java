package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.Setter;

/**
 *
 * @TableName StockHistory
 */
@TableName(value ="StockHistory")
@Data
public class Stockhistory implements Serializable {
    @TableId(value = "SHID", type = IdType.AUTO)
    private Integer SHID;

    @TableField(value = "SHTime")
    private Date SHTime;

    @TableField(value = "SHReason")
    private Integer SHReason;

    @TableField(value = "GID")
    private Integer GID;

    @TableField(value = "SHStock_O")
    private Integer SHStock_O;

    @TableField(value = "SHStock_N")
    private Integer SHStock_N;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getSHID() {
        return SHID;
    }

    public void setSHID(Integer SHID) {
        this.SHID = SHID;
    }

    public Integer getSHReason() {
        return SHReason;
    }

    public void setSHReason(Integer SHReason) {
        this.SHReason = SHReason;
    }

    public Date getSHTime() {
        return SHTime;
    }

    public void setSHTime(Date SHTime) {
        this.SHTime = SHTime;
    }

    public Integer getGID() {
        return GID;
    }

    public void setGID(Integer GID) {
        this.GID = GID;
    }

    public Integer getSHStock_O() {
        return SHStock_O;
    }

    public void setSHStock_O(Integer SHStock_O) {
        this.SHStock_O = SHStock_O;
    }

    public Integer getSHStock_N() {
        return SHStock_N;
    }

    public void setSHStock_N(Integer SHStock_N) {
        this.SHStock_N = SHStock_N;
    }
}
