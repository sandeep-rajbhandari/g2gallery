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
        <g:javascript library="layout_manager" />
    </head>
    <body>
    	<div class="lm_container">
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>
        <div class="logo lm_top" id="logoDiv"><img src="${createLinkTo(dir:'images',file:'grails_logo.jpg')}" alt="Grails" /></div>
        <g:layoutBody />
        </div>
    </body>
</html>