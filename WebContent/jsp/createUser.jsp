<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../include/base.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Create User</title>




</head>



<body>
	<%@ include file="../include/header.jsp"%>
	<div class="container">
		<br />
		<h2>
			<span>Create User</span>
		</h2>

		<font color="red" size="5"><s:actionmessage /> <s:if
				test="hasErrors()">Create failed.reason:${actionErrors[0]}
		</s:if></font>


		<s:form id="form1" action="doCreateUser" method="post" namespace="/">

			<table class="global_table" cellpadding="0" cellspacing="0">
				<tr>
					<th class="width135">customerID:</th>
					<td><input class="txt" id="user.customerID"
						name="user.customerID" type="text" /></td>

					<th class="width135">userName:</th>
					<td><input class="txt" id="user.userName" name="user.userName"
						type="text" /></td>

				</tr>

				<tr>


					<th class="width135">Age:</th>
					<td><input class="txt" id="user.age" name="user.age"
						type="text" /></td>


					<th class="width135">Gender:</th>

					<td>
					<input type="radio" name="tempUserGender"
						id="tempUserGender1" value="male" checked="checked" /> 
						<label for="tempUserGender1">male</label> 
					<input type="radio"
						name="tempUserGender" id="tempUserGender0" value="female" /> 
						<label for="tempUserGender0">female</label>
					</td>
				</tr>



				<tr>


					<th class="width135">Address:</th>
					<td><textarea id="user.address" rows="2" cols="60"
							name="user.address"></textarea></td>

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