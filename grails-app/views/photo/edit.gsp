

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
                <input type="hidden" name="id" value="${photo?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>

                        	<tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'name','errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:photo,field:'name')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description">Description:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'description','errors')}">
                                    <input type="text" id="description" name="description" value="${fieldValue(bean:photo,field:'description')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="url">Url:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'url','errors')}">
                                    <label id="url">${photo?.url}</label>
                                </td>
                            </tr>

							<tr class="prop">
                                <td valign="top" class="name">
                                    <label for="album">Album:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'album','errors')}">
                                    <g:select optionKey="id" from="${Album.list()}" name="album.id" value="${photo?.album?.id}" noSelection="['-1' : 'Not in album']"></g:select>
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
