// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text-plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.yourexcitingapplication.com"
    }
}

// log4j configuration
log4j {
    appender.stdout = "org.apache.log4j.ConsoleAppender"
    appender.'stdout.layout'="org.apache.log4j.PatternLayout"
    appender.'stdout.layout.ConversionPattern'='[%r] %c{2} %m%n'
    appender.errors = "org.apache.log4j.FileAppender"
    appender.'errors.layout'="org.apache.log4j.PatternLayout"
    appender.'errors.layout.ConversionPattern'='[%r] %c{2} %m%n'
    appender.'errors.File'="stacktrace.log"

    appender.spring = "org.apache.log4j.FileAppender"
    appender.'spring.layout'="org.apache.log4j.PatternLayout"
    appender.'spring.layout.ConversionPattern'='[%r] %c{2} %m%n'
    appender.'spring.File'="springstacktrace.log"

    rootLogger="error,errors"
    logger {
        grails="debug"
        StackTrace="error,errors"
        org {
            codehaus.groovy.grails.web.servlet="error"  //  controllers
            codehaus.groovy.grails.web.pages="error" //  GSP
            codehaus.groovy.grails.web.sitemesh="error" //  layouts
            codehaus.groovy.grails."web.mapping.filter"="error" // URL mapping
            codehaus.groovy.grails."web.mapping"="error" // URL mapping
            codehaus.groovy.grails.commons="info" // core / classloading
            codehaus.groovy.grails.plugins="debug" // plugins
            codehaus.groovy.grails.orm.hibernate="debug" // hibernate integration
            springframework="debug"
            hibernate="error"
        }
    }
    additivity.StackTrace=false
}




//log4j.logger.org.springframework.security="error"