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
			var photoShowDiv = $('photoDiv');
			var photo = eval('(' + transport.responseText + ')').photo;
			photoView(photo, photoShowDiv);
		}
	});
}

function photoView(photo, container) {
	var img = "<img src='" + CONTEXT_PATH + "/photo/showPhoto/" + photo.id + "' style='border : 4px solid white;' ";
	if (photo.height > container.getHeight() - 20) {
		img += " height='" + (container.getHeight() - 20) + "'";
	}
	if (photo.width > container.getWidth()  - 40) {
		img += " width='" + (container.getWidth() - 40) + "'";
	}
	img += "/>";
	img += "<div>" + photo.description + "</div>";

	container.update(img);
}