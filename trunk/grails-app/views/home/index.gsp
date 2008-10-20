
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
	<title>Gallery</title>
	<style type="text/css">
	.album-div {
		float: left;
		margin : 5px;
		width : 150px;
		height : 150px;
		text-align: center;
	}
	.cover {
		margin: auto;
	}
	.name {
		margin: auto;
		width: 100%;
		text-align: center;
		font-weight: bold;
	}
	</style>
</head>
<body>
	<div class="nav" id="menuDiv">
       	<div style="float: left;">
            <g:isLoggedIn>
            	<span class="menuButton"><g:link class="create" action="create" controller="album">New Album</g:link></span>
            </g:isLoggedIn>
		</div>

		<g:render template="/shared/userLoggedOn"/>
	</div>

	<div class="content" style="margin : 30px 200px;">
		<g:form url="[controller : 'photo', action : 'list']" name="showPhotosForm">
			<input type="hidden" name="album.id"/>
		</g:form>
		<g:each in="${albumList}" var="album">
			<g:each in="${(0..<album.photos.size())}" var="i">
				<div class="album-div" title="Description : ${album.description}" onclick="showAlbum(${album.id})">
					<div class="cover"><g:showPhoto photo="${album.photos.iterator().next()}" width="120" height="120"/></div>
					<div class="name">${album.name}</div>
				</div>
			</g:each>
		</g:each>
	</div>
</body>
</html>