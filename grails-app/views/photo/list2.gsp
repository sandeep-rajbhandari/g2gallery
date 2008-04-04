<%--
  Created by IntelliJ IDEA.
  User: trungsi
  Date: 17 mars 2008
  Time: 23:57:43
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main2" />
        <title>Photo List</title>
        <script type="text/javascript" src="http://slideshow.triptracker.net/slide.js"></script>

        <g:javascript>
        	var photoViewer;

        	function init() {
	        	photoViewer = new PhotoViewer();
	        	<g:each in="${photoList}" var="photo">
	        		photoViewer.add("${createLink(action : 'showPhoto', params : [id : photo.id])}", "${photo.description}");
	        		new Tip("iconDiv${photo.id}", $("iconDiv${photo.id}Tooltip").innerHTML, {className : 'darkTip', effect : 'appear'});
	        	</g:each>
        	}

        	new Event.observe(window, 'load', init);
        </g:javascript>
    </head>

    <body onload="init();">
        <div class="nav lm_top" id="menuDiv">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Photo</g:link></span>
            <span class="menuButton"><a href="javascript:void(photoViewer.show(0))">Slide Show</a></span>
        </div>

        <div class="lm_center">
	        <div class="lm_container" id="content">
				<div class="lm_top">
		            <h1>Photo List</h1>
		            <g:if test="${flash.message}">
		            <div class="message">${flash.message}</div>
		            </g:if>
				</div>

	            <div class="lm_left" style="width: 300px;">
	                <div class="list">
	                    <g:each in="${photoList}" status="i" var="photo">
	                         <div class="iconDiv" id="iconDiv${photo.id}">
	                            <div class="iconDivInner" onclick="showPhotoInfos2(${photo.id})">
	                                <div class="iconPhoto"><g:showPhoto photo="${photo}" width="70" height="70"/></div>
	                            </div>
	                        </div>
	                        <div class="iconDivTooltip" id="iconDiv${photo.id}Tooltip">
	                            <div>Name : ${photo.name}</div>
	                            <div>Description : ${photo.description}</div>
	                            <div>Dimension : ${photo.width}x${photo.height}</div>
	                            <div>Url : ${photo.url}</div>
	                            <div>Album : ${photo.album?.name}</div>
	                        </div>
	                     </g:each>
	                     <div style="clear: both;"></div>

	                </div>

	                <g:if test="${photoList && photoList.size() < Photo.count()}">
	                    <div class="paginateButtons">
	                        <g:paginate total="${Photo.count()}" />
	                    </div>
	                </g:if>
	            </div>

	            <div class="lm_center" style="text-align: center;vertical-align: middle;">
	                <div id="photoShowDiv">aaaa</div>
	            </div>
	        </div>
        </div>
    </body>
</html>