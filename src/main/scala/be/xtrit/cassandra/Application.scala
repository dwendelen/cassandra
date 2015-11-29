package be.xtrit.cassandra

import be.xtrit.cassandra.mapper.{MapperConfig, ResultSetMapper}
import be.xtrit.cassandra.session.SessionConfig
import be.xtrit.cassandra.user.controller.Controller
import com.datastax.driver.core.{Session, Cluster, ResultSet}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{SpringBootApplication, EnableAutoConfiguration}
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{Bean, Import, Configuration, AnnotationConfigApplicationContext}
import org.springframework.web.servlet.config.annotation.EnableWebMvc

import scala.beans.BeanProperty



object Application {
    def main (args: Array[String]) {
        SpringApplication.run(classOf[Application])
    }
}

@SpringBootApplication
class Application {
    @Bean
    def controller = new Controller

    @Bean
    def servletContainer = {
        new JettyEmbeddedServletContainerFactory()
    }
}


class Table {
    @Column("string")
    @BeanProperty
    var string:String = null
    @Column("in")
    @BeanProperty
    var in : Int = 0
}