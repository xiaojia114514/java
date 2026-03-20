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
 * @TableName Alipayment_order
 */
@TableName(value ="Alipayment_order")
@Data
public class Alipay implements Serializable {
    @TableId(value = "alid", type = IdType.AUTO)
    private Integer alid;

    @TableField(value = "out_trade_no")
    private String out_trade_no;

    @TableField(value = "oid")
    private int oid;

    @TableField(value = "totalAmount")
    private String totalAmount;

    @TableField(value = "subject")
    private String subject;

    @TableField(value = "add_time")
    private Date add_time;

    @TableField(value = "update_time")
    private Date update_time;

    @TableField(value = "state")
    private int state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Integer getAlid() {
        return alid;
    }

    public void setAlid(Integer alid) {
        this.alid = alid;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Date add_time) {
        this.add_time = add_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
