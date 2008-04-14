class ShowPhotoTagLib {
    def showPhoto = {attrs ->
		def link = g.createLink(controller : 'photo', action : 'showPhoto', params : [id : attrs.photo.id])

		def img = "<img src='${link}' class='photo' "
		attrs.each {name, value ->
			if (name != 'photo') {
				img += addAttr(name, value)
			}
		}

		img += " />"

		out << img
	}

	def addAttr = {name, value ->
		" $name='${escape(value)}'"
	}

    def escape = {value ->
    	value.replaceAll("'", "\"")
    }
}
