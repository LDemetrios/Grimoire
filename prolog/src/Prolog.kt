package org.ldemetrios.intrinsics.runtime.prolog

import alice.tuprolog.Prolog
import alice.tuprolog.event.ExceptionEvent
import alice.tuprolog.event.OutputEvent
import alice.tuprolog.event.SpyEvent
import alice.tuprolog.interfaces.event.SpyListener

fun main() {
    prologWithConversions {
        val solver = Prolog()
        solver.addTheory(
            theory(
                "parent"("john", "mary"),
                "parent"("mary", "ann"),
                scoped { "ancestor"(X, Y) { +"parent"(X, Y) } },
                scoped { "ancestor"(X, Y) { +"parent"(X, Z) `,` "ancestor"(Z, Y) } }
            )
        )

        solver.addExceptionListener { exceptionEvent: ExceptionEvent? ->
            throw AssertionError("Prolog error: " + exceptionEvent!!.getMsg())
        }
        solver.addOutputListener { event: OutputEvent? ->
            // Strip text_term/2 output
            if (!event!!.getMsg().endsWith("'\nV_e0")) {
                print(event.getMsg())
            }
        }

//            Solver.problog.solverWithDefaultBuiltins(
//            staticKb = theory(
//                "parent"("john", "mary"),
//                "parent"("mary", "ann"),
//                scoped { "ancestor"(X, Y) { +"parent"(X, Y) } },
//                scoped { "ancestor"(X, Y) { +"parent"(X, Z) `,` "ancestor"(Z, Y) } }
//            )
//        )

//        val solutions = scoped {
//            val query = "ancestor"("john", X)
//            solver.solve(query)
//                .normalize()
//                .map { it[X] }
//        }

        val query = solver.solve {  "ancestor"("john", X) }
        for (result in query) Unit // println(result)
    }
}
