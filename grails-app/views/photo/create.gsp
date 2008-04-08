

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Photo</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Photo List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Photo</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${photo}">
            <div class="errors">
                <g:renderErrors bean="${photo}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" enctype="multipart/form-data">
                <div class="dialog">
                    <table>
                        <tbody>
                        <%--
							<tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'name','errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:photo,field:'name')}"/>
                                </td>
                            </tr>
						--%>
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
                                    <label for="album">Album:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'album','errors')}">
                                    <g:if test="${photo.album}">
                                    	<label>${photo.album}</label>
                                    	<input type="hidden" id="album" name="album.id" value="${fieldValue(bean:photo.album,field:'id')}"/>
                                    </g:if>
                                    <g:else>
                                    	<g:select optionKey="id" from="${Album.list()}" name="album.id" value="${album?.id}" noSelection="['-1' : 'Not in album']"></g:select>
                                    </g:else>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="url">Url:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:photo,field:'url','errors')}">
                                    <input type="file" id="url" name="url" value=""/>
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
