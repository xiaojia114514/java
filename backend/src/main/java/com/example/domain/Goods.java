package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@TableName(value ="Goods")
@Data
public class Goods implements Serializable {

    @TableId(value = "GID", type = IdType.AUTO)
    private Integer gid;


    @TableField(value = "GName")
    private String gname;

    @TableField(value = "GDesc")
    private String gdesc;

    @TableField(value = "GStock")
    private Integer gstock;

    @TableField(value = "GValue")
    private Integer gvalue;

    @TableField(value = "CID")
    private Integer cid;

    @TableField(value = "GSeller")
    private String gseller;

    @TableField(value = "GShelf")
    private Integer gshelf;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGdesc() {
        return gdesc;
    }

    public void setGdesc(String gdesc) {
        this.gdesc = gdesc;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public Integer getGstock() {
        return gstock;
    }

    public void setGstock(Integer gstock) {
        this.gstock = gstock;
    }

    public Integer getGvalue() {
        return gvalue;
    }

    public void setGvalue(Integer gvalue) {
        this.gvalue = gvalue;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setGseller(String gseller) {
        this.gseller = gseller;
    }

    public String getGseller() {
        return gseller;
    }

    public Integer getGshelf() {
        return gshelf;
    }

    public void setGshelf(Integer gshelf) {
        this.gshelf = gshelf;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
