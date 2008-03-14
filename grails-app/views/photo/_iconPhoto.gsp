<div class="iconDiv" id="iconDiv${photo.id}">
	<div class="iconDivInner" onclick="showPhotoInfos(${photo.id})">
		<div class="iconPhoto"><g:showPhoto photo="${photo}" width="70" height="70"/></div>
		<div class="iconText">${photo.name}</div>
	</div>
</div>
<div class="iconDivTooltip" id="iconDiv${photo.id}Tooltip">
	<div>Name : ${photo.name}</div>
	<div>Description : ${photo.description}</div>
	<div>Url : ${photo.url}</div>
	<div>Album : ${photo.album?.name}</div>
</div>
<g:javascript>
new Tip("iconDiv${photo.id}", $("iconDiv${photo.id}Tooltip").innerHTML, {className : 'darkTip', effect : 'appear'});
</g:javascript>

