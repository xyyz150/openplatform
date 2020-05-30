package com.qimenapi.base;

import com.qimenapi.util.StringHelp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyyz150 on 2015/6/25.
 */
public class PageQuerySqlMapParameters {
    private int _PageRowCount = 10;
    private int _PageIndex = 1;
    private int BeginRowIndex=0;
    private int EndRowCount=10;
    private String OrderByColumn;
    private Map<String,Object> QueryParams=new HashMap<String, Object>();

    public void AddQueryParam(String key,Object value)
    {
        if(StringHelp.IsNullOrEmpty(key))return;
        if(value==null||value.toString()==""||value.toString().equalsIgnoreCase("null"))return;
        QueryParams.put(key,value);
    }

    public void AddCollectionQueryParam(Map<String,Object> param){
        if(param==null||param.isEmpty()||param.size()==0)return;
        QueryParams.putAll(param);
    }

    public String getOrderByColumn() {
        if(OrderByColumn!=null&&OrderByColumn.isEmpty())
            return OrderByColumn;
        else
            return "RowId Desc";
    }

    public void setOrderByColumn(String orderByColumn) {
        OrderByColumn = orderByColumn;
    }

    public int get_PageRowCount() {
        return _PageRowCount;
    }

    public void set_PageRowCount(int _PageRowCount) {
        this._PageRowCount = _PageRowCount>0?_PageRowCount:this._PageRowCount;
    }

    public int get_PageIndex() {
        return _PageIndex;
    }

    public void set_PageIndex(int _PageIndex) {
        this._PageIndex = _PageIndex;
    }


    /// <summary>
/// 开始记录号
/// </summary>
    public int getBeginRowIndex() {
        BeginRowIndex= _PageRowCount * (getPageIndex() - 1);
        return BeginRowIndex;
    }

    /// <summary>
/// 行数
/// </summary>
    public int getEndRowCount() {
//        EndRowCount= _PageRowCount * (getPageIndex());
//        return EndRowCount;
        return _PageRowCount;
    }

    /// <summary>
/// 当前页
/// </summary>
    public int getPageIndex() {

        if (_PageIndex == 0) return 1;
        return _PageIndex;
    }

    public Map<String,Object> GetSqlMapParameter()
    {
        QueryParams.put("OrderByColumn",getOrderByColumn());
        QueryParams.put("BeginRowIndex",getBeginRowIndex());
        QueryParams.put("EndRowCount",getEndRowCount());
        return QueryParams;
    }
}
