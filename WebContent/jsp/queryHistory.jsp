<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/base.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CICS Savvy</title>
</head>
<body>
	<%@ include file="../include/header.jsp"%>

	<div class="container">

		<font color="red" size="5"><s:actionmessage /> <s:if
				test="hasErrors()">empty transfer error. Reason:${actionErrors[0]}
			</s:if> </font> <br>

		<s:form id="form1" action="doQueryAccount" method="post" namespace="/">

			<table class="global_table" cellpadding="0" cellspacing="0">
				<tr>
					<th class="width135">Account Number:</th>
					<td><input class="txt" id="account.accountNumber" name="account.accountNumber"
						type="text" value="<s:property value="account.accountNumber" />" /></td>
				</tr>


			</table>
			<div class="g_tablebtn">
				<input id="btnSearch" type="submit" value="query" />
			</div>

		</s:form>
		<br />

		Account info:
		<table class="global_table" cellpadding="0" cellspacing="0">


			<tr>
				<th class="width100">CustomerID:</th>
				<td><s:property value="account.customerID" /></td>
				<th class="width100">Balance:</th>
				<td><s:property value="account.balance" /></td>
				<th class="width100">Last Change Time:</th>
				<td><s:property value="account.lastChangeTime" /></td>
			</tr>

		</table>
		<br />

		Transaction records:
		<table width="1200" border="3" cellpadding="0" cellspacing="1"
			class="search_results">
			<tr>
				<th>transName</th>
				<th>transAmount</th>
				<th>transTime</th>

			</tr>

			<s:iterator id="account.transHistories" value="account.transHistories" status="st">
				<tr>

					<td><s:property value="transName" /></td>
					<td><s:property value="transAmount" /></td>
					<td><s:property value="transTime" /></td>
					<!-- td><s:date name="transTime" format="yyyy-MM-dd hh:mm:ss"
							nice="false" /></td-->
				</tr>
			</s:iterator>
		</table>

	</div>
	<%@ include file="../include/footer.jsp"%>
</body>
</html>
