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
			
		<s:form id="form1" action="doQueryUser" method="post" namespace="/">

			<table class="global_table" cellpadding="0" cellspacing="0">
				<tr>
					<th class="width135">User ID:</th>
					<td><input class="txt" id="user.customerID" name="user.customerID"
						type="text" value="<s:property value="user.customerID" />" /></td>
				</tr>

			</table>
			<div class="g_tablebtn">
				<input id="btnSearch" type="submit" value="query" />
			</div>

		</s:form>			
		<br />

        Customer Info:
		<table class="global_table" cellpadding="0" cellspacing="0">
			<tr>
				<th class="width100">CustomerID:</th>
				<td><s:property value="user.customerID" /></td>
				<th class="width100">Name:</th>
				<td><s:property value="user.userName" /></td>
				<th class="width100">Gender:</th>
				<td><s:property value="tempUserGender" /></td>
			</tr>
			<tr>
				<th class="width100">Age:</th>
				<td><s:property value="user.age" /></td>
				<th class="width100">Address:</th>
				<td><s:property value="user.address" /></td>
			</tr>

		</table>
		<br />
		
		Accounts of the customer:	
		<table width="1200" border="3" cellpadding="0" cellspacing="1"
			class="search_results">
			<tr>
				<th>AccountNumber</th>
				<th>Balance</th>
				<th>lastChangeTime</th>

			</tr>

			<s:iterator id="userIterator" value="user.accountSet" status="st">
				<tr>

					<td><s:property value="accountNumber" /></td>
					<td><s:property value="balance" /></td>
					<td><s:property value="lastChangeTime" /></td>
				</tr>
			</s:iterator>
		</table>

	</div>
	<%@ include file="../include/footer.jsp"%>
</body>
</html>
