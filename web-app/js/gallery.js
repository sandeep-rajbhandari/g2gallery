function showPhotoInfos(id) {
	location.href = CONTEXT_PATH + "/photo/show/" + id;
}

function showPhotosOfAlbum(id) {
    location.href = CONTEXT_PATH + "/album/show/" + id;
}

var showedPhotoId;

function showPhotoInfos2(photoId) {
	if (showedPhotoId)
		$('iconPhoto' + showedPhotoId).setStyle({borderColor : 'white'});
	showedPhotoId = photoId;
    $('iconPhoto' + showedPhotoId).setStyle({borderColor : 'red'});
    var showPhotoUrl = CONTEXT_PATH + "/photo/show2/" + photoId;
	new Ajax.Request(showPhotoUrl, {
		onSuccess : function (transport) {
			var photoShowDiv = $('photoDiv');
			var photo = eval('(' + transport.responseText + ')').photo;
			photoView(photo, photoShowDiv);
		}
	});
}

function photoView(photo, container) {
	var height = photo.height > container.getHeight() - 20 ?
					photo.height > container.getHeight() - 20 : photo.height;
	var width = photo.width > container.getWidth()  - 40 ?
					photo.width > container.getWidth()  - 40 : photo.width;

	var img = "<img src='" + CONTEXT_PATH + "/photo/showPhoto/" + photo.id + "' ";
	img += " height='" + height + "'";
	img += " width='" + width + "'";

	img += "/>";
	img = "<div style='border:4px solid white;width:" + width + ";height:" + height + ";margin : 0 auto;'>" + img + "</div>";
	img += "<div>" + photo.description + "</div>";

	new Effect.Fade(container, {duration : .5, afterFinish : function() {
		container.update(img);
		new Effect.Appear(container, {duraciton : .5});
	}});

}