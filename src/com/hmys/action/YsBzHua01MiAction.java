package com.hmys.action;

import com.hmys.common.CommonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @className: YsBzHua01MiAction
 * @author: jun
 * @date: 2021-05-15 14:40
 * @Depiction: 预算编制审批流程
 * @Depiction: 预算编制 更新明细表一 明细表二  运算流水编号
 **/
public class YsBzHua01MiAction  implements Action {
    private Log log = LogFactory.getLog(YsBzHua01MiAction.class.getName());

    public String execute(RequestInfo requestInfo) {
        /**流程workflowid*/
        String workFlowId = requestInfo.getWorkflowid();
        /**流程requestid*/
        String requestId = requestInfo.getRequestid();
        /**流程名称*/
        String workFlowName = CommonUtil.getWorkFlowName(workFlowId);
        /**主表名称*/
        String tableName = CommonUtil.getWorkFlowTableName(workFlowId);
        log.info("触发流程:" + workFlowName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";发起流程ID:" + workFlowId + ";开始!");
        try {
            RecordSet recordSet = new RecordSet();
            RecordSet recordSet1 = new RecordSet();
            String sql1="select * from "+tableName+" where requestid = "+requestId;
            recordSet.execute(sql1);
            recordSet.next();
            String  mainId =Util.null2String(recordSet.getString("mainid"));
            /**流程编号*/
            String  lcbh =Util.null2String(recordSet.getString("lcbh"));
            /**获取明细表一数据信息*/
            String sql2 = "select * from " + tableName + "_dt1  where  mainid='"+mainId+"'";
            recordSet.execute(sql2);
            while(recordSet.next()){
                String id = Util.null2String(recordSet.getString("id"));
                String sql3="update "+tableName+"_dt1 set yslsbh='"+lcbh+"-"+id+"' where id = '"+id+"'";
                recordSet1.execute(sql3);
            }
            String sql4 = "select * from " + tableName + "_dt2  where  mainid='"+mainId+"'";
            recordSet.execute(sql4);
            while(recordSet.next()){
                String id = Util.null2String(recordSet.getString("id"));
                String sql3="update "+tableName+"_dt2 set yslsbh='"+lcbh+"-"+id+"' where id = '"+id+"'";
                recordSet1.execute(sql3);
            }
        } catch (Exception e) {
            requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
            log.info("触发流程:" + workFlowName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";发起流程ID:" + workFlowId + ";程序异常!");
            return Action.FAILURE_AND_CONTINUE;
        }
        return Action.SUCCESS;
    }
}
