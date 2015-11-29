package be.xtrit.cassandra.user.model

import be.xtrit.cassandra.mapper.ResultSetMapper
import com.datastax.driver.core.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
private[user] class CredentialsDao {
    @Autowired
    var session: Session = null
    @Autowired
    var mapper: ResultSetMapper = null

    def saveCredentials(credentials: Credentials): Unit = {
        session.execute("insert into credentials (email, password, salt, userid) values (?, ?, ?, ?) if not exists",
            credentials.email, credentials.hashedPassword, credentials.salt, credentials.userId)
    }

    def getCredentials(email:String): Credentials = {
        val resultSet = session.execute("select password, salt, userid from credentials where email=?", email)
        mapper.mapSingleResult(resultSet, classOf[Credentials])
    }

    def isCredentialsExistsWithEmail(email: String): Boolean = {
        val resultSet = session.execute("select count(*) from credentials where email=?", email)
        resultSet.one().getLong(0) != 0
    }
}
