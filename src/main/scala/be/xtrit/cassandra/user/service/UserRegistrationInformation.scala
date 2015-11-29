package be.xtrit.cassandra.user.service

import scala.beans.BeanProperty

class UserRegistrationInformation {
    @BeanProperty
    var name: String = null
    @BeanProperty
    var email: String = null
    @BeanProperty
    var password: String = null
}