package com.hmys.common;

import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * @className: CommonUtil
 * @author: jun
 * @date: 2021-05-15 14:44
 * @Depiction:
 **/
public class CommonUtil {

    /**获取发起流程的名称*/
    public static String getWorkFlowName(String workflowid) {
        RecordSet rs = new RecordSet();
        String workflowName = "";
        if (null == workflowid || "".equals(workflowid)) {
            return workflowName;
        }
        rs.execute("select workflowname from workflow_base where id = " + workflowid);
        if (rs.next()) {
            workflowName = Util.null2String(rs.getString("workflowname"));
        }
        return workflowName;
    }

    /**获取发起流程的 table名称*/
    public static String getWorkFlowTableName(String workflowid) {
        RecordSet rs = new RecordSet();
        String tableName = "";
        if (null == workflowid || "".equals(workflowid)) {
            return tableName;
        }
        rs.execute("select tablename  from workflow_bill where id = (select formid from workflow_base where id= '" + workflowid + "' and isbill=1)");
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        return tableName;
    }
}
