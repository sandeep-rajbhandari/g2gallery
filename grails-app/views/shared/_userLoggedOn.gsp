<div style="float: right;">
	
	<g:loggedInUserInfo field="userRealName">
		<form name="loginForm" action="/gallery/j_spring_security_check" method="post">
			<input type="hidden" name="spring-security-redirect" value=""/>
			<label class="loginLabel">Username</label>
			<input type="text" class="loginInput" name="j_username" />

			<label class="loginLabel">Password</label>
			<input type="password" class="loginInput" name="j_password"/>

			<input type="button" value="Login" onclick="doLogin();return false;"/>
		</form>
	</g:loggedInUserInfo>
	
	<g:isLoggedIn>
		<g:link controller="logout">( Logout )</g:link>
	</g:isLoggedIn>

</div>
<div style="clear: both;"></div>