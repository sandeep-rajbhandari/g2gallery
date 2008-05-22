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
        <meta name="layout" content="main" />
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

			function initPaginateButtons() {
	        	new Effect.Appear('paginateButtons', {duration:.5});
			}

			function appear(elem) {
				new Effect.Appear(elem, {duration:.5});
			}

        	new Event.observe(window, 'load', init);
        	new Event.observe(window, 'load', initPaginateButtons);
        </g:javascript>
    </head>

    <body>
        <div class="nav" id="menuDiv">
        	<div style="float: left;">
	            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
	            <g:isLoggedIn>
	            	<span class="menuButton"><g:link class="create" action="create">New Photo</g:link></span>
	            </g:isLoggedIn>
	            <span class="menuButton"><a href="javascript:void(photoViewer.show(0))">Slide Show</a></span>
			</div>

			<g:render template="/shared/userLoggedOn"/>
        </div>

        <div class="" id="content">

            <div class="" id="photoListDiv">

            	<div class="">
		            <g:if test="${flash.message}">
		            <div class="message">${flash.message}</div>
		            </g:if>
				</div>

                <div class="list">
                    <g:each in="${photoList}" status="i" var="photo">
                        <div class="iconDiv" id="iconDiv${photo.id}" onclick="showPhotoInfos2(${photo.id})">
                            <div class="iconPhoto" id="iconPhoto${photo.id}">
                            	<g:showPhoto photo="${photo}" width="70" height="70" style="display: none;" id="photo${photo.id}" onload="appear(this)"/>
                            </div>
                        </div>
                        <div class="iconDivTooltip" id="iconDiv${photo.id}Tooltip">
                            <div>Description : ${photo.description}</div>
                            <div>Dimension : ${photo.width}x${photo.height}</div>
                               <div>Size : ${photo.size}</div>
                            <div>Album : ${photo.album?.name}</div>
                        </div>
                     </g:each>
                     <div style="clear: both;"></div>
                </div>

                <g:if test="${photoList && photoList.size() < Photo.count()}">
                    <div class="paginateButtons" id="paginateButtons" style="display: none;">
                        <g:paginate total="${Photo.count()}" />
                    </div>
                </g:if>

            </div>

            <div id="photoShowDiv">
                <div id="photoDiv">aaa</div>
            </div>
        </div>

    </body>
</html>