package hello

import org.neo4j.graphdb.Direction
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import java.io.File

fun main(args : Array<String>){

    val greeter = Greeter()
    val greet = greeter.greet()

    println(greet)
}

class Greeter {

    private val graphDb :GraphDatabaseService = GraphDatabaseFactory().newEmbeddedDatabase(File("graph.db"))

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
}
