// Place your Spring DSL code here
beans = {
    multipartResolver(org.springframework.web.multipart.commons.CommonsMultipartResolver) {
    	maxUploadSize = 1000000
    }    
}