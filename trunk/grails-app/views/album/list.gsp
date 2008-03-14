

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Album List</title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'gallery.css')}" />
        <link rel="stylesheet" href="${createLinkTo(dir:'css/prototip',file:'prototip.css')}" />
        <g:javascript library="scriptaculous"/>
        <g:javascript src="prototip.js"/>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Album</g:link></span>
        </div>
        <div class="body">
            <h1>Album List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <g:each in="${albumList}" status="i" var="album">
                    <g:render template="iconAlbum" model="[album : album]"/>
                </g:each>
                <div style="clear: both;"></div>
            </div>

			<g:if test="${albumList.size() < Album.count()}">
            <div class="paginateButtons">
                <g:paginate total="${Album.count()}" />
            </div>
            </g:if>
        </div>
    </body>
</html>
