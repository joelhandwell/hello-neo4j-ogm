package hello.model

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
class Person(
        @Id @GeneratedValue
        var id: Long? = null,

        var name: String,

        @Relationship(type = "BELONGS_TO", direction = "OUTGOING")
        var organizations: Set<Organization> = emptySet(),

        var greet: String? = null
){
    fun belongsTo(organization: Organization){
        organizations = organizations.plus(organization)
        organization.members = organization.members.plus(this)
    }
}

@NodeEntity
class Organization(
        @Id @GeneratedValue
        var id: Long? = null,

        var name: String,

        @Relationship(type = "BELONGS_TO", direction = "INCOMING")
        var members: Set<Person> = emptySet()
)
