<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@ include file="/systeminfo/init.jsp" %>
<%@ page import="weaver.general.Util,weaver.dnwj.*" %>
<table border="0" width="100%" >
<%
 if(CheckUserInRoles.CheckRoleByUserId(1823, user.getUID())){
 %>
 	<tr>
	  <td><a href="http://192.168.0.64:8081/lczServer/hrServlet?fileName=hte_2020_06_19111904594&authId=anonymous_admin&targetVolume=app_20160726094256" target="_blank">1.ÃûÆ¬ÖÆ×÷ÉêÇë</a></td>
	</tr>
 <% } %> 
 
</table>
