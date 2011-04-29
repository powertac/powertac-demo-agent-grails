grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// development plugin locations
grails.plugin.location.PowertacCommon = "../powertac-common"

grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // uncomment to disable ehcache
    // excludes 'ehcache'
  }
  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  repositories {
    grailsPlugins()
    grailsHome()
    grailsCentral()

    // uncomment the below to enable remote dependency resolution
    // from public Maven repositories
    // mavenLocal()
    mavenCentral()
    mavenRepo "http://snapshots.repository.codehaus.org"
    mavenRepo "http://repository.codehaus.org"
    mavenRepo "http://download.java.net/maven/2/"
    mavenRepo "http://repository.jboss.com/maven2/"
  }
  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

    // runtime 'mysql:mysql-connector-java:5.1.13'
    compile('org.apache.activemq:activemq-core:5.4.2') {
      excludes 'commons-collections', 'commons-logging', 'junit', 'log4j', 'spring-context', 'spring-parent', 'spring-aop', 'spring-asm', 'spring-beans', 'spring-expression', 'xalan', 'xml-apis'
    }

    compile('org.apache.activemq:activemq-pool:5.4.2') {
      excludes 'commons-collections', 'commons-pool', 'commons-logging', 'junit', 'log4j', 'spring-context', 'spring-parent', 'spring-aop', 'spring-asm', 'spring-beans', 'spring-expression', 'xalan', 'xml-apis'
    }

    runtime('org.codehaus.groovy.modules.http-builder:http-builder:0.5.1') {
      excludes 'groovy', 'xml-apis'
    }

    // this should have been inherited from powertac-common but somehow IntelliJ doesn't see this
    // compile( group: 'com.thoughtworks.xstream', name: 'xstream', version: '1.3.1', export: true )
  }
}
