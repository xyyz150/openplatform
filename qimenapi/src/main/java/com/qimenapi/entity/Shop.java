package com.qimenapi.entity;

import com.qimenapi.util.DateHelp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xyyz150 on 2015/6/25.
 */
public class Shop implements Serializable {
    public Shop()
    {
        setCode("");
        setName("");
        setLeader("");
        setMobile("");
        setTelephone("");
        setZipCode("");
        setFax("");
        setAddress("");
        setAddress1("");
        setBegDate("");
        setEndDate("");
        setCreateDate(DateHelp.getDefaultDate());
        setCreater("");
        setRemark("");
        setPinyin("");
        setShopIdentifier("");
        setPlatParams("");

    }


    /**
     主键
     */
    private long RowId;


    /**
     店铺代码||不能为空
     */
    private String Code;


    /**
     店铺名称||不能为空
     */
    private String Name;


    /**
     启用||启用=1 0=禁用  不能为空
     */
    private boolean IsEnabled;


    /**
     店铺类型||不能为空
     */
    private long PlatTypeRowId;


    /**
     店铺性质||0=直营店 1=分销店 不能为空

     */
    private int ShopNatureEnu;


    /**
     负责人
     */
    private String Leader;


    /**
     手机
     */
    private String Mobile;


    /**
     电话
     */
    private String Telephone;


    /**
     邮编
     */
    private String ZipCode;


    /**
     传真
     */
    private String Fax;


    /**
     详细区域||不能为空
     */
    private long RegionRowId;


    /**
     地址
     */
    private String Address;


    /**
     地址1
     */
    private String Address1;


    /**
     开始时间||为空就是系统默认值
     */
    private String BegDate;


    /**
     结束时间||为空就是系统默认值
     */
    private String EndDate;


    /**
     显示顺序
     */
    private int DisplayNum;


    /**
     创建时间
     */
    private Date CreateDate;


    /**
     创建人
     */
    private String Creater;


    /**
     备注
     */
    private String Remark;


    /**
     拼音
     */
    private String Pinyin;


    /**
     店铺标识
     */
    private String ShopIdentifier;


    /**
     平台参数
     */
    private String PlatParams;


    /**
     主键
     */
    public long getRowId()
    {

        return this.RowId;
    }
    public void setRowId(long RowId)
    {
        this.RowId=RowId;;
    }


    /**
     店铺代码||不能为空
     */
    public String getCode()
    {

        return this.Code;
    }
    public void setCode(String Code)
    {
        this.Code=Code;;
    }


    /**
     店铺名称||不能为空
     */
    public String getName()
    {

        return this.Name;
    }
    public void setName(String Name)
    {
        this.Name=Name;;
    }


    /**
     启用||启用=1 0=禁用  不能为空
     */
    public boolean getIsEnabled()
    {

        return this.IsEnabled;
    }
    public void setIsEnabled(boolean IsEnabled)
    {
        this.IsEnabled=IsEnabled;;
    }


    /**
     店铺类型||不能为空
     */
    public long getPlatTypeRowId()
    {

        return this.PlatTypeRowId;
    }
    public void setPlatTypeRowId(long PlatTypeRowId)
    {
        this.PlatTypeRowId=PlatTypeRowId;;
    }


    /**
     店铺性质||0=直营店 1=分销店 不能为空

     */
    public int getShopNatureEnu()
    {

        return this.ShopNatureEnu;
    }
    public void setShopNatureEnu(int ShopNatureEnu)
    {
        this.ShopNatureEnu=ShopNatureEnu;;
    }


    /**
     负责人
     */
    public String getLeader()
    {

        return this.Leader;
    }
    public void setLeader(String Leader)
    {
        this.Leader=Leader;;
    }


    /**
     手机
     */
    public String getMobile()
    {

        return this.Mobile;
    }
    public void setMobile(String Mobile)
    {
        this.Mobile=Mobile;;
    }


    /**
     电话
     */
    public String getTelephone()
    {

        return this.Telephone;
    }
    public void setTelephone(String Telephone)
    {
        this.Telephone=Telephone;;
    }


    /**
     邮编
     */
    public String getZipCode()
    {

        return this.ZipCode;
    }
    public void setZipCode(String ZipCode)
    {
        this.ZipCode=ZipCode;;
    }


    /**
     传真
     */
    public String getFax()
    {

        return this.Fax;
    }
    public void setFax(String Fax)
    {
        this.Fax=Fax;;
    }


    /**
     详细区域||不能为空
     */
    public long getRegionRowId()
    {

        return this.RegionRowId;
    }
    public void setRegionRowId(long RegionRowId)
    {
        this.RegionRowId=RegionRowId;;
    }


    /**
     地址
     */
    public String getAddress()
    {

        return this.Address;
    }
    public void setAddress(String Address)
    {
        this.Address=Address;;
    }


    /**
     地址1
     */
    public String getAddress1()
    {

        return this.Address1;
    }
    public void setAddress1(String Address1)
    {
        this.Address1=Address1;;
    }


    /**
     开始时间||为空就是系统默认值
     */
    public String getBegDate()
    {

        return this.BegDate;
    }
    public void setBegDate(String BegDate)
    {
        this.BegDate=BegDate;;
    }


    /**
     结束时间||为空就是系统默认值
     */
    public String getEndDate()
    {

        return this.EndDate;
    }
    public void setEndDate(String EndDate)
    {
        this.EndDate=EndDate;;
    }


    /**
     显示顺序
     */
    public int getDisplayNum()
    {

        return this.DisplayNum;
    }
    public void setDisplayNum(int DisplayNum)
    {
        this.DisplayNum=DisplayNum;;
    }


    /**
     创建时间
     */
    public Date getCreateDate()
    {

        return this.CreateDate;
    }
    public void setCreateDate(Date CreateDate)
    {
        this.CreateDate=CreateDate;;
    }


    /**
     创建人
     */
    public String getCreater()
    {

        return this.Creater;
    }
    public void setCreater(String Creater)
    {
        this.Creater=Creater;;
    }


    /**
     备注
     */
    public String getRemark()
    {

        return this.Remark;
    }
    public void setRemark(String Remark)
    {
        this.Remark=Remark;;
    }


    /**
     拼音
     */
    public String getPinyin()
    {

        return this.Pinyin;
    }
    public void setPinyin(String Pinyin)
    {
        this.Pinyin=Pinyin;;
    }


    /**
     店铺标识
     */
    public String getShopIdentifier()
    {

        return this.ShopIdentifier;
    }
    public void setShopIdentifier(String ShopIdentifier)
    {
        this.ShopIdentifier=ShopIdentifier;;
    }


    /**
     平台参数
     */
    public String getPlatParams()
    {

        return this.PlatParams;
    }
    public void setPlatParams(String PlatParams)
    {
        this.PlatParams=PlatParams;;
    }
}
