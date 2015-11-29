package be.xtrit.cassandra.user

import be.xtrit.cassandra.user.model.Credentials
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CredentialsTest {
    private val PASSWORD = "Pass"
    private val SALT = "Salt"

    @Test
    def loggingIn(): Unit = {
        val cred = new Credentials(PASSWORD, SALT)
        assertThat(cred.isValid(PASSWORD)).isTrue()
        assertThat(cred.isValid("Bad")).isFalse()
    }

    @Test
    def theHashedPasswordIsNotTheSameAsThePassword(): Unit = {
        val cred = new Credentials(PASSWORD, SALT)
        assertThat(cred.hashedPassword).isNotEqualTo(PASSWORD);
    }

    @Test
    def saltEnsuresThatTheSamePasswordWillNotHaveTheSameHash(): Unit = {
        val cred1 = new Credentials(PASSWORD, SALT)
        val cred2 = new Credentials(PASSWORD, "another salt")
        assertThat(cred1.hashedPassword).isNotEqualTo(cred2.hashedPassword);
    }
}