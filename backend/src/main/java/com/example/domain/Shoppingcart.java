package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName ShoppingCart
 */
@TableName(value ="ShoppingCart")
@Data
public class Shoppingcart implements Serializable {
    @TableId(value = "SCID", type = IdType.AUTO)
    private Integer SCID;

    @TableField(value = "GID")
    private Integer GID;

    @TableField(value = "UAccount")
    private String UAccount;

    @TableField(value = "SCNumber")
    private Integer SCNumber;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getSCID() {
        return SCID;
    }

    public void setSCID(Integer SCID) {
        this.SCID = SCID;
    }

    public Integer getGID() {
        return GID;
    }

    public void setGID(Integer GID) {
        this.GID = GID;
    }

    public String getUAccount() {
        return UAccount;
    }

    public void setUAccount(String UAccount) {
        this.UAccount = UAccount;
    }

    public Integer getSCNumber() {
        return SCNumber;
    }

    public void setSCNumber(Integer SCNumber) {
        this.SCNumber = SCNumber;
    }
}
