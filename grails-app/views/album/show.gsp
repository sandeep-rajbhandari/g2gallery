

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Album</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Album List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Album</g:link></span>
        </div>
        <div class="body">
            <h1>Show Album</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <div>Name : ${album.name}</div>

                <div>Description : ${album.description}</div>

                <div>Photos:
                	<g:if test="${album.photos}">
                	<g:render template="/photo/photoList" model="[photoList : album.photos, count : album.photos.size()]"/>
                	</g:if>
                	<g:else>
                		No photo
                	</g:else>
                </div>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${album?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Add New Photo" /></span>
                    <span class="button"><g:actionSubmit class="edit" value="Add Existant Photo" /></span>

                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
