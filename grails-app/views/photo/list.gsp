

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Photo List</title>
        <script type="text/javascript" src="http://slideshow.triptracker.net/slide.js"></script>
        <g:javascript>
        	var photoViewer = new PhotoViewer();
        	<g:each in="${photoList}" var="photo">
        		photoViewer.add("${createLink(action : 'showPhoto', params : [url : photo.url])}", "${photo.description}");
        	</g:each>
        </g:javascript>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Photo</g:link></span>
            <span class="menuButton"><a href="javascript:void(photoViewer.show(0))">Slide Show</a></span>
        </div>
        <div class="body">
            <h1>Photo List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>

			<g:render template="photoList" model="[photoList : photoList, count : Photo.count()]"/>

        </div>
    </body>
</html>
