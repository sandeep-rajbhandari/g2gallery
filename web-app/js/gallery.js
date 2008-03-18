function showPhotoInfos(id) {
	location.href = CONTEXT_PATH + "/photo/show/" + id;
}

function showPhotosOfAlbum(id) {
    location.href = CONTEXT_PATH + "/album/show/" + id;
}

function showPhotoInfos2(id) {
    var showPhotoUrl = CONTEXT_PATH + "/photo/show2/" + id;
	new Ajax.Request(showPhotoUrl, {
		onSuccess : function (transport) {
			var photoShowDiv = $('photoShowDiv');
			var photo = eval('(' + transport.responseText + ')');
			alert(photo);
			photoView(photo, photoShowDiv);
		}
	});
}

function photoView(photo, container) {
	alert(container.getWidth());
	alert(container.getHeight());
	container.update(photo);
}