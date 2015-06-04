<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../include/base.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Transfer</title>




</head>



<body>
	<%@ include file="../include/header.jsp"%>
	<div class="container">
		<br />
		<h2>
			<span>Transfer</span>
		</h2>

		<font color="red" size="5"><s:actionmessage /> <s:if
				test="hasErrors()">Create failed.reason:${actionErrors[0]}
		</s:if></font>


		<s:form id="form1" action="doTransfer" method="post" namespace="/">

			<table class="global_table" cellpadding="0" cellspacing="0">
				<tr>
					<th class="width135">Source account:</th>
					<td><input class="txt" id="sourceAcct" name="sourceAcct"
						type="text" /></td>

					<th class="width135">Target account:</th>
					<td><input class="txt" id="targetAcct" name="targetAcct"
						type="text" /></td>

					<th class="width135">Transfer amount:</th>
					<td><input class="txt" id="amount" name="amount" type="text" /></td>

				</tr>


			</table>
			<div class="g_tablebtn">
				<input id="btnSearch" type="submit" value="Transfer" />
			</div>

		</s:form>
		<br />

	</div>

	<%@ include file="../include/footer.jsp"%>
</body>
</html>