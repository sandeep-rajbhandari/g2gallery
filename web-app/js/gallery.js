function doLogin() {
	var form = document.loginForm;
	var params = form.serialize();
	alert(params);
	new Ajax.Request(form.action, {
		method : 'post',
		postBody : params,
		onSuccess : function(response) {
			var json = response.responseText.evalJSON();
			if(json.error) {
				alert(json.error);
			} else {
				alert("login successful");
			}
		}
	});
}

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
    
    var showPhotoUrl = CONTEXT_PATH + "/photo/showJSON/" + photoId;
	new Ajax.Request(showPhotoUrl, {
		onSuccess : function (transport) {
			var photoShowDiv = $('photoDiv');
			var photo = eval('(' + transport.responseText + ')').photo;
			photoView(photo, photoShowDiv);
		}
	});
}

function photoView(photo, container) {
	/*var height = photo.height > container.getHeight() - 20 ?
					container.getHeight() - 20 : photo.height;
	var width = photo.width > container.getWidth()  - 40 ?
					container.getWidth()  - 40 : photo.width;
	*/
	
	
	var img = "<img src='" + CONTEXT_PATH + "/photo/showPhoto/" + photo.id + "' ";
	img += " ";
	img += "/>";
	img = "<div style='border:4px solid white;margin : 0 auto;'>" + img + "</div>";
	img += "<div>" + photo.description + "</div>";

	new Effect.Fade(container, {duration : .5, afterFinish : function() {
		container.update(img);
		new Effect.Appear(container, {duraciton : .5});
	}});

}

function showAlbum(albumId) {
	$('showPhotosForm').elements['album.id'].value = albumId;
	$('showPhotosForm').submit();
}