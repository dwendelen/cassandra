package be.xtrit.cassandra.user.service

import java.util.UUID

import be.xtrit.cassandra.mapper.ResultSetMapper
import be.xtrit.cassandra.user.model.{CredentialsDao, User, UserDao, Credentials}
import be.xtrit.cassandra.user.EmailAddressIsAlreadyInUseException
import com.datastax.driver.core.Session
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserService {
    @Autowired
    var session: Session = null
    @Autowired
    var mapper: ResultSetMapper = null
    @Autowired
    var userDao: UserDao = null
    @Autowired
    var credentialsDao: CredentialsDao = null

    def validateCredentials(loginRequest: LoginRequest): Boolean = {
        val credentials = credentialsDao.getCredentials(loginRequest.emailAddress)
        credentials.isValid(loginRequest.password)
    }

    def registerUser(userRegistrationInformation: UserRegistrationInformation): Unit = {
        if(credentialsDao.isCredentialsExistsWithEmail(userRegistrationInformation.email)) {
            throw new EmailAddressIsAlreadyInUseException
        }

        val userId = UUID.randomUUID()

        val user = new User()
        user.userid = userId
        user.email = userRegistrationInformation.email
        user.name = userRegistrationInformation.name

        userDao.saveNewUser(user)

        val salt = RandomStringUtils.randomAlphanumeric(10)
        val credentials = new Credentials(
            userRegistrationInformation.email,
            userRegistrationInformation.password,
            salt,
            userId)

        credentialsDao.saveCredentials(credentials)
    }
}
