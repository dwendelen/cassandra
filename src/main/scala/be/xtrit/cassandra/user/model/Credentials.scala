package be.xtrit.cassandra.user.model

import java.security.MessageDigest
import java.util.UUID

import be.xtrit.cassandra.Column
import org.springframework.util.Base64Utils

import scala.annotation.meta.field

class Credentials(@(Column @field)("email")
                  var email:String,
                  password: String,
                  @(Column @field)("salt")
                  var salt: String,
                  @(Column @field)("userid")
                  var userId: UUID) {


    @Column("password")
    var hashedPassword = createHash(password)

    def this() = this(null, null, null, null)

    def isValid(password: String): Boolean = {
        hashedPassword == createHash(password)
    }

    private def createHash(password: String): String = {
        val saltedPassword = salt + password
        var bytes = MessageDigest.getInstance("SHA-256").digest(saltedPassword.getBytes)
        new String(Base64Utils.encode(bytes))
    }
}
