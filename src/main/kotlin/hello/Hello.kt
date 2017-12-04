package hello

fun main(args : Array<String>){

    val greeter = Greeter()
    val greet = greeter.greet()

    println(greet)
}

class Greeter {
    fun greet() = "Hello World!"
}
