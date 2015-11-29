package be.xtrit.cassandra.user.service

import scala.beans.BeanProperty

class LoginRequest {
    @BeanProperty
    var emailAddress: String = null

    @BeanProperty
    var password: String = null
}
