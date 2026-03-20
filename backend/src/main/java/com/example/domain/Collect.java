package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName Collect
 */
@TableName(value ="Collect")
@Data
public class Collect implements Serializable {

    @TableId(value = "CoID", type = IdType.AUTO)
    private Integer coID;

    @TableField(value = "UAccount")
    private String UAccount;

    @TableField(value = "GID")
    private Integer GID;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getCoID() {
        return coID;
    }

    public void setCoID(Integer coID) {
        this.coID = coID;
    }

    public String getUAccount() {
        return UAccount;
    }

    public void setUAccount(String UAccount) {
        this.UAccount = UAccount;
    }

    public Integer getGID() {
        return GID;
    }

    public void setGID(Integer GID) {
        this.GID = GID;
    }
}
