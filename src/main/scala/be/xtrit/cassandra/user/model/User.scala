package be.xtrit.cassandra.user.model

import java.util.UUID

import be.xtrit.cassandra.Column

class User {
    @Column("userid")
    var userid: UUID = null
    @Column("name")
    var name: String = null
    @Column("email")
    var email: String = null
}
