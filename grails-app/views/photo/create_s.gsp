

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Photo</title>
		<style type="text/css">
			url {
				width: 30px;
			}
		</style>
		<script type="text/javascript">
		function addFile(elem) {
			var i = $('form').elements.length - 1;
			$('url_' + i).insert({after : '<div id="url_' + (i+1) + '"><input type="file" name="url_' + (i+1) + '" class="url"/></div>'});
		}
		</script>
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

			<g:form action="save_s" method="post" enctype="multipart/form-data" name="form">
				<div id="url_1"><input type="file" name="url_1" class="url"/></div>
				<div id="url_2"><input type="file" name="url_2" class="url"/></div>
				<div id="url_3"><input type="file" name="url_3" class="url"/></div>
				<div id="url_4"><input type="file" name="url_4" class="url"/></div>
				<div id="url_5"><input type="file" name="url_5" class="url"/></div>
				<div id="url_6"><input type="file" name="url_6" class="url"/></div>
				<div><a href="javascript:addFile();">Add more</a></div>
				<input type="submit" value="Upload"/>
			</g:form>
        </div>
    </body>
</html>
