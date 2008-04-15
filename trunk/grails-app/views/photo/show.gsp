

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Photo</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Photo List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Photo</g:link></span>
        </div>
        <div class="body">
            <h1>Show Photo</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">Description:</td>

                            <td valign="top" class="value">${photo.description}</td>

                        </tr>

						 <tr class="prop">
                            <td valign="top" class="name">Album:</td>

                            <td valign="top" class="value">${photo.album?.name}</td>

                        </tr>

                    	<tr class="prop">
                    		<td valign="top" class="name">
                    			<a href="${createLink(action : 'showPhoto', id : photo.id)}">
                    				<g:showPhoto photo="${photo}" width="100" height="100"/>
                    			</a>
                    		</td>
                    	</tr>
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${photo?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
