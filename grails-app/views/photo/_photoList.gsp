<div class="list">
	<g:each in="${photoList}" status="i" var="photo">
         <g:render template="/photo/iconPhoto" model="[photo: photo]"/>
     </g:each>
     <div style="clear: both;"></div>

</div>

<g:if test="${photoList && photoList.size() < count}">
	<div class="paginateButtons">
	    <g:paginate total="${count}" />
	</div>
</g:if>