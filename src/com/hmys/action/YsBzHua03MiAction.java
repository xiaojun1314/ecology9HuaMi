package com.hmys.action;

import com.hmys.common.CommonUtil;
import com.weaver.file.Prop;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.ln.HttpClientUtil;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className: YsBzHua02MiAction
 * @author: jun
 * @date: 2021-05-15 15:31
 * @Depiction: 预算编制审批流程
 * @Depiction: 运算编制转建模， 批复运算金额编制转建模
 **/
public class YsBzHua03MiAction implements Action {
    private Log log = LogFactory.getLog(YsBzHua03MiAction.class.getName());
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
            String sql1 = "select * from " + tableName + " where requestid = " + requestId;
            recordSet.execute(sql1);
            recordSet.next();
            String mainId = Util.null2String(recordSet.getString("mainid"));
            /**申请人*/
            String sqr = Util.null2String(recordSet.getString("sqr"));

            Date nowDate= new Date();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
            String time = new SimpleDateFormat("HH:mm:ss").format(nowDate);
            JSONObject dataDetailObject = new JSONObject();
            dataDetailObject.put("operationDate",date);
            dataDetailObject.put("operator",sqr);
            dataDetailObject.put("operationTime",time);
            String systemid= Prop.getPropValue("ysbz", "interface.ysbz.systemid");
            String password=Prop.getPropValue("ysbz", "interface.ysbz.password");
            String url=Prop.getPropValue("ysbz", "interface.ysbz.url");
            String dataTimeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(nowDate);
            password=systemid+password+dataTimeStr;
            String md5Password=  com.alibaba.alimei.sso.api.utils.MD5Util.getMD5Str(password);

            String sql2 = "select * from " + tableName + "_dt1  where  mainid='"+mainId+"'";
            recordSet.execute(sql2);
            JSONArray detailArray = new JSONArray();
            while(recordSet.next()){
                JSONObject dataObject = new JSONObject();
                dataObject.put("operationinfo",dataDetailObject);
                JSONObject dataDetailObjectTemp = new JSONObject();
                /**预算流水编号*/
                dataDetailObjectTemp.put("yslsbh",Util.null2String(recordSet.getString("yslsbh")));
                /**预算类型*/
                dataDetailObjectTemp.put("yslx",Util.null2String(recordSet.getString("yslx")));
                /**年度*/
                dataDetailObjectTemp.put("nd",Util.null2String(recordSet.getString("nd")));
                /**期间*/
                dataDetailObjectTemp.put("qj",Util.null2String(recordSet.getString("ysqj")));
                /**期间开始日期*/
                dataDetailObjectTemp.put("qjksrq",Util.null2String(recordSet.getString("qjksrq")));
                /**期间结束日期*/
                dataDetailObjectTemp.put("qjjsrq",Util.null2String(recordSet.getString("qjjsrq")));
                /**预算费用用途说明*/
                // dataDetailObjectTemp.put("ysfyytsm",Util.null2String(recordSet.getString("id")));
                /**预算金额*/
                dataDetailObjectTemp.put("ysje",Util.null2String(recordSet.getString("ysje")));
                /**	预算币种（业务币种）*/
                dataDetailObjectTemp.put("ysbzywbz",Util.null2String(recordSet.getString("ysbz")));
                /**预算定稿可用金额（业务币种） 待定*/
                //dataDetailObjectTemp.put("ysdgkyjeywbz",Util.null2String(recordSet.getString("ysdgkyjeywbz")));
                /**预算调整金额（业务币种）*/
                dataDetailObjectTemp.put("ysdzjeywbz","0");
                /**预算调整次数*/
                dataDetailObjectTemp.put("ysdzcs","0");
                /**预算在途金额（业务币种）*/
                dataDetailObjectTemp.put("ysztjeywbz","0");
                /**预算冻结金额（业务币种）*/
                dataDetailObjectTemp.put("ysdjjeywbz","0");
                /**预算发生金额（业务币种）*/
                dataDetailObjectTemp.put("ysfsjeywbz","0");
                /**预算到期失效金额（业务币种）*/
                dataDetailObjectTemp.put("ysdqsxjeywbz","0");
                /**费用对应一级部门*/
                dataDetailObjectTemp.put("fydyyjbm",Util.null2String(recordSet.getString("yjfyby").split("-")[0]));
                /**费用对应二级部门*/
                dataDetailObjectTemp.put("fydyejbm",Util.null2String(recordSet.getString("ejfybm").split("-")[0]));
                /**费用对应三级部门*/
                dataDetailObjectTemp.put("fydysjbm",Util.null2String(recordSet.getString("yjyslx").split("-")[0]));
                /**预算状态*/
                dataDetailObjectTemp.put("yszt",Util.null2String(recordSet.getString("id")));
                /**收入支出类型*/
                dataDetailObjectTemp.put("srzclx",Util.null2String(recordSet.getString("id")));
                /**一级费用类型*/
                dataDetailObjectTemp.put("yjfylx",Util.null2String(recordSet.getString("yjfylx")));
                /**二级费用类型*/
                dataDetailObjectTemp.put("ejfylx",Util.null2String(recordSet.getString("ejfylx")));
                /**三级费用类型*/
                dataDetailObjectTemp.put("sjfylx",Util.null2String(recordSet.getString("sjfylx")));
                dataObject.put("mainTable",dataDetailObjectTemp);
                detailArray.add(dataObject);
            }
            JSONObject detailObject = new JSONObject();
            detailObject.put("data",detailArray);
            JSONObject headObject = new JSONObject();
            headObject.put("systemid",systemid);
            headObject.put("currentDateTime",dataTimeStr);
            headObject.put("Md5",md5Password);
            detailObject.put("header",headObject);
            JSONObject mainObject = new JSONObject();
            mainObject.put("datajson",detailObject.toString());
            String resultStr= HttpClientUtil.PostMethodTest(url,mainObject);
            JSONObject result = JSONObject.fromObject(resultStr);
            String resultFlag=result.get("status").toString();

            if(resultFlag.equals("1")){
                log.info("触发流程:" + workFlowName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";发起流程ID:" + workFlowId + ";执行成功!"+"返回结果"+resultStr);
                return Action.SUCCESS;
            }else{
                log.info("触发流程:" + workFlowName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";发起流程ID:" + workFlowId + ";执行失败!"+"返回结果"+resultStr);
                requestInfo.getRequestManager().setMessagecontent("运算编制建模失败，请联系系统管理员查看原因！");
                return Action.FAILURE_AND_CONTINUE;
            }

        } catch (Exception e) {
            requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
            log.info("触发流程:" + workFlowName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";发起流程ID:" + workFlowId + ";程序异常!");
        }
        return Action.SUCCESS;
    }

}
