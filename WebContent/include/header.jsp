<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script language="text/javascript">
function menuFix() {
var sfEls = document.getElementById("menu").getElementsByTagName("li");
for (var i=0; i<sfEls.length; i++) {
sfEls[i].onmouseover=function() {
this.className+=(this.className.length>0? " ": "") + "sfhover";
}
sfEls[i].onMouseDown=function() {
this.className+=(this.className.length>0? " ": "") + "sfhover";
}
sfEls[i].onMouseUp=function() {
this.className+=(this.className.length>0? " ": "") + "sfhover";
}
sfEls[i].onmouseout=function() {
this.className=this.className.replace(new RegExp("( ?|^)sfhover\\b"),"");
}
}
}
window.onload=menuFix;


</script>

<div id="wrap">
	<div id="header">
		<div>
			<div class="logo">
				<img src="images/logo.jpg" alt="IBM" />
			</div>
			<div class="topc">
				<s:if test="#session.user!=null">
					<p>
						<span style="color: 000000; font-weight: bold; font-size: 13px">Welcome:[<s:property
								value="#session.user" />]
						</span>
					</p>
				</s:if>
			</div>
		</div>
	</div>

	<div id="menu" class="container">
		<li><a href="toMenuPage.action">Home Page</a></li>



		<li><a href="#">User </a>
			<ul>
				<li><a href="toCreateUser.action">Create User</a></li>
				<li><a href="toQueryUser.action">Query User</a></li>

			</ul></li>

		<li><a href="#">Account</a>
			<ul>
				<li><a href="toCreateAccount.action">Create Account</a></li>
				<li><a href="toQueryAccount.action">Query Account</a></li>
			</ul></li>

		<li><a href="#">Transaction</a>
			<ul>

				<li><a href="toDeposit.action">Deposit</a></li>
				<li><a href="toWithDraw.action">Withdraw</a></li>
				<li><a href="toTransfer.action">Transfer</a></li>
			</ul></li>



	</div>