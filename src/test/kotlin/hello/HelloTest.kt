package hello

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals


object HelloTest: Spek({

    val greeter = Greeter()

    describe("Greeter") {
        it("should greet with standard greeting"){

            assertEquals("Hello World!", greeter.greet())
        }
    }

    describe("Greeter in the morning") {
        it("should greet with morning greeting"){

            assertEquals("Good Morning World!", greeter.morningGreet())
        }
    }
})
