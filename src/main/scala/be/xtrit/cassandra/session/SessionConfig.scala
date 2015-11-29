package be.xtrit.cassandra.session

import javax.annotation.PreDestroy

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import org.springframework.context.annotation.{Configuration, Bean}

@Configuration
class SessionConfig {
    var cluster: Cluster = null
    var session: Session = null

    @Bean
    def getSession: Session = {
        cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .build()

        session = cluster.connect("test")
        session
    }

    @PreDestroy
    def cleanup(): Unit = {
        if(session != null) {
            session.close()
        }
        if(cluster != null) {
            cluster.close()
        }
    }
}
