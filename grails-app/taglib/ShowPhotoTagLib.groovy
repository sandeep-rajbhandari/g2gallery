class ShowPhotoTagLib {
    def showPhoto = {attrs ->
		def link = g.createLink(controller : 'photo', action : 'showPhoto', params : [url : attrs.photo.url])

		def img = "<img src='${link}'"
		attrs.each {name, value ->
			if (name != 'photo') {
				img += addAttr(name, value)
			}
		}

		img += " />"

		out << img
	}

	def addAttr = {name, value ->
		" $name='$value'"
	}
}
