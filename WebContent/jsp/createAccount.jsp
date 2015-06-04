<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../include/base.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Create Account</title>




</head>



<body>
	<%@ include file="../include/header.jsp"%>
	<div class="container">
		<br />
		<h2>
			<span>Create Account</span>
		</h2>

		<font color="red" size="5"><s:actionmessage /> <s:if
				test="hasErrors()">Create failed.reason:${actionErrors[0]}
		</s:if></font>


		<s:form id="form1" action="doCreateAccount" method="post"
			namespace="/">

			<table class="global_table" cellpadding="0" cellspacing="0">
				<tr>
					<th class="width135">Account Number:</th>
					<td><input class="txt" id="account.accountNumber"
						name="account.accountNumber" type="text" /></td>

					<th class="width135">balance:</th>
					<td><input class="txt" id="account.balance"
						name="account.balance" type="text" /></td>

				</tr>
				<tr>
					<th class="width135">Customer ID:</th>
					<td><input class="txt" id="account.customerID"
						name="account.customerID" type="text" /></td>

				</tr>

			</table>
			<div class="g_tablebtn">
				<input id="btnSearch" type="submit" value="create" />
			</div>

		</s:form>
		<br />

	</div>

	<%@ include file="../include/footer.jsp"%>
</body>
</html>