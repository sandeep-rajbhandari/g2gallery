<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />

        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'gallery.css')}" />

        <link rel="stylesheet" href="${createLinkTo(dir:'css/prototip',file:'prototip.css')}" />
        <g:javascript library="scriptaculous"/>
        <g:javascript library="prototip"/>

        <g:layoutHead />

        <g:javascript>
        	var CONTEXT_PATH = "${request.contextPath}"
        </g:javascript>
        <g:javascript library="application" />
        <g:javascript library="gallery" />
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>

        <g:isNotLoggedIn>
	        <div style="position: absolute;right: 0;">
	        	<form action="../j_acegi_security_check" method="POST" id="loginForm" class="cssform">
					<p>
						<label for="j_username" style="width: 100px;float: left;">Login</label>
						<input type='text' class="text_" name='j_username' value='' />
					</p>
					<p>
						<label for="j_password" style="width: 100px;float: left;">Password</label>
						<input type='password' class="text_" name='j_password' value='' />
					</p>
					<p>
						<label for="j_password" style="width: 100px;float: left;">Remember me</label>
						<input type="checkbox" class="chk" name="_acegi_security_remember_me">
						<input type="submit" value="Login" />
					</p>
				</form>
	        </div>
        </g:isNotLoggedIn>

		<div><img src="${createLinkTo(dir:'images',file:'grails_logo.jpg')}" alt="Grails" /></div>

        <g:layoutBody />

        <div id="footer">
        	<div id="footerText" >
            	&copy; Gallery <g:meta name="app.version"/> on Grails <g:meta name="app.grails.version"/> by <a href="http://tranductrung.s42.eatj.com/blog/">trungsi</a>.
            </div>
        </div>
    </body>
</html>