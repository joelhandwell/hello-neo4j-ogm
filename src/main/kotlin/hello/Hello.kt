package hello

import java.io.File
import org.neo4j.graphdb.Direction
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import hello.model.Organization
import hello.model.Person
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.Level
import org.slf4j.LoggerFactory

fun main(args : Array<String>){

    val logger: Logger = LoggerFactory.getLogger("org.neo4j.ogm") as Logger
    logger.level = Level.ERROR

    val greeter = Greeter()

    val greet = greeter.greet()
    println(greet)

    val morningGreet = greeter.morningGreet()
    println(morningGreet)
}

const val DB_PATH = "graph.db"

class Greeter {

    private val graphDb :GraphDatabaseService = GraphDatabaseFactory().newEmbeddedDatabase(File(DB_PATH))

    init {
        Runtime.getRuntime().addShutdownHook(Thread(graphDb::shutdown))
    }

    fun greet(): String?{

        var greet: String? = null

        graphDb.beginTx().use { tx ->

            val firstNode = graphDb.createNode()
            firstNode.setProperty("message", "Hello ")

            val secondNode = graphDb.createNode()
            secondNode.setProperty("message", "World!")

            val relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS)
            relationship.setProperty("message", "with Neo4j")

            greet = firstNode.getProperty("message") as String + secondNode.getProperty("message") as String

            firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete()

            firstNode.delete()
            secondNode.delete()

            tx.success()

        }

        return greet
    }

    fun morningGreet(): String?{

        val configuration = Configuration.Builder().uri("file:///${DB_PATH}").build()
        val sessionFactory = SessionFactory(configuration, "hello.model")
        val session = sessionFactory.openSession()

        val organization = Organization(name = "An Organization")

        val john = Person(name = "John Smith", greet = "Good Morning")
        john.belongsTo(organization)

        val peter = Person(name = "Peter White", greet = " World!")
        peter.belongsTo(organization)

        session.save(organization)

        val anOrganzation = session.load(Organization::class.java, organization.id)

        val people = mutableListOf(anOrganzation.members.filter { it.name == "John Smith" }.first())

        people.add(anOrganzation.members.filter { it.name == "Peter White" }.first())

        val greeting = StringBuilder()

        people.forEach {
            greeting.append(it.greet)
            session.delete(it)
        }

        session.delete(organization)

        return greeting.toString()
    }
}
