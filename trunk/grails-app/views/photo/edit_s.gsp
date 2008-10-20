

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Photo</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Photo List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Photo</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Photo</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${photo}">
            <div class="errors">
                <g:renderErrors bean="${photo}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <div class="dialog">
					<g:each in="${photoList}" var="photo" status="i">
                    <table>
                        <tbody>
							<tr class="prop">
								<td valign="top"><g:showPhoto photo="${photo}" width="200" height="200"/></td>
							</tr>
                            <tr class="prop">
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'description','errors')}">
                                    <textarea style="width: 200px; height: 200px;" id="description_$i" name="description_$i">${fieldValue(bean:photo,field:'description')}</textarea>
                                	<input type="hidden" name="id_$i" value="${photo?.id}" />
								</td>
                            </tr>
                        </tbody>
                    </table>
					</g:each>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" action="update_s"/></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
