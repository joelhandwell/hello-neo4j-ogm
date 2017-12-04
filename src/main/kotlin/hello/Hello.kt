package hello

fun main(args : Array<String>){

    val greeter = Hello()
    val greet = greeter.greet()

    println(greet)
}

class Hello{
    fun greet() = "Hello World!"
}
