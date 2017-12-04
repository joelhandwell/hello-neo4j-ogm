package hello

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals


object HelloTest: Spek({
    describe("Greeter") {
        it("should greet with standard greeting"){
            val greeter = Greeter()

            assertEquals("Hello World!", greeter.greet())
        }
    }
})
