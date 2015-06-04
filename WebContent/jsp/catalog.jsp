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


		<br />
		<h2>

			<span>CICS Savvy</span>
		</h2>


		welcome to CICS Savvy<br /> <br /> <br /> <font color="red"
			size="5"><s:actionmessage /> <s:if test="hasErrors()">empty transfer error. Reason:${actionErrors[0]}
			</s:if> </font>


	</div>
	<%@ include file="../include/footer.jsp"%>
</body>
</html>
