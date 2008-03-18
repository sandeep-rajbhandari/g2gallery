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
	        		photoViewer.add("${createLink(action : 'showPhoto', params : [url : photo.url])}", "${photo.description}");
	        		new Tip("iconDiv${photo.id}", $("iconDiv${photo.id}Tooltip").innerHTML, {className : 'darkTip', effect : 'appear'});
	        	</g:each>


	        	//$('content').setStyle({height: document.getSize().height - $('logoDiv').getHeight() - $('menuDiv').getHeight() -7 /* ???*/});
        	}

			/*document.getSize = function() {
			    var xScroll = 0;
			    var yScroll = 0;

			    if (window.innerHeight && window.scrollMaxY) {
			        xScroll = window.innerWidth + window.scrollMaxX;
			        yScroll = window.innerHeight + window.scrollMaxY;

			    } else if (document.body.scrollHeight > document.body.offsetHeight) { // all but Explorer Mac
			        xScroll = document.body.scrollWidth;
			        yScroll = document.body.scrollHeight;

			    } else { // Explorer Mac...would also work in Explorer 6 Strict,Mozilla and Safari
			        xScroll = document.body.offsetWidth;
			        yScroll = document.body.offsetHeight;
			    }

			    var windowWidth, windowHeight;

			    if (self.innerHeight) { // all except Explorer
			        windowWidth = (document.documentElement.clientWidth) ?
					document.documentElement.clientWidth : self.innerWidth;
					        windowHeight = self.innerHeight;

			    } else if (document.documentElement &&	document.documentElement.clientHeight) { // Explorer 6 Strict Mode
			        windowWidth = document.documentElement.clientWidth;
			        windowHeight = document.documentElement.clientHeight;

			    } else if (document.body) { // other Explorers
			        windowWidth = document.body.clientWidth;
			        windowHeight = document.body.clientHeight;
			    }

			    // for small pages with total height less then height of the viewport
			    var docHeight = (yScroll < windowHeight) ? windowHeight : yScroll;

			    // for small pages with total width less then width of the viewport
			    var docWidth = (xScroll < windowWidth) ? xScroll : windowWidth;

			    // return arrayPageSize;
			    return {"width": docWidth, "height": docHeight};

			}*/

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