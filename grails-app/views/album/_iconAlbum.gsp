<div class="iconDiv" id="iconDiv${album.id}">
	<div class="iconDivInner" onclick="showPhotosOfAlbum(${album.id})">
		<div class="iconPhoto"><img src="${createLinkTo(dir : 'images', file : 'folder.png')}" width="50" height="50" title="${album.description}"/></div>
		<div class="iconText">${album.name}</div>
	</div>
</div>
<div class="iconDivTooltip" id="iconDiv${album.id}Tooltip">
	<div style="font-size: 110%;font-weight: bold">${album.name}</div>
	<div>${album.description}</div>
	<div>contains ${album.photos.size()} photo(s)</div>
</div>
<g:javascript>
new Tip("iconDiv${album.id}", $("iconDiv${album.id}Tooltip").innerHTML, {className : 'darkTip', effect : 'appear'});
</g:javascript>