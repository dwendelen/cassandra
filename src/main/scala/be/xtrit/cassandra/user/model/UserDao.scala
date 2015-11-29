package be.xtrit.cassandra.user.model

import be.xtrit.cassandra.mapper.ResultSetMapper
import be.xtrit.cassandra.user.EmailAddressIsAlreadyInUseException
import com.datastax.driver.core.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
private[user] class UserDao {
    @Autowired
    var session: Session = null
    @Autowired
    var mapper: ResultSetMapper = null

    def saveNewUser(user: User): Unit = {
        session.execute("insert into user (userid, name, email) values (?,?,?)",
            user.userid, user.name, user.email)
    }
}
