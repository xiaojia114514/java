package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName User
 */
@TableName(value ="User")
@Data
public class User implements Serializable {
    @TableId
    private String uaccount;

    private String upassword;

    private String uphone;

    private Integer ucategory;

    public String getUaccount() {
        return uaccount;
    }

    public void setUaccount(String uaccount) {
        this.uaccount = uaccount;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public Integer getUcategory() {
        return ucategory;
    }

    public void setUcategory(Integer ucategory) {
        this.ucategory = ucategory;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
