// This should be later moved to commons

package org.ldemetrios.intrinsics.runtime.prolog

import alice.tuprolog.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.RecordComponent
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.Iterable
import kotlin.collections.Iterator
import kotlin.collections.MutableList
import kotlin.collections.asList
import kotlin.collections.asReversed
import kotlin.collections.asSequence
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.plus
import kotlin.collections.reduce
import kotlin.collections.reduceRight
import kotlin.collections.toMutableList
import kotlin.collections.toTypedArray

typealias PInt = alice.tuprolog.Int
typealias PLong = alice.tuprolog.Long
typealias PFloat = alice.tuprolog.Float
typealias PDouble = alice.tuprolog.Double


class PrologScope {
    internal val variables = ConcurrentHashMap<String, Var>()
}

context(scope: PrologScope)
fun varOf(str: String) =
    scope.variables.computeIfAbsent(str, Var::of) // It should be the same variable through one scope

// Ya tellin' me, we ain't need no macros?!
context(scope: PrologScope) val A get() = varOf("A")
context(scope: PrologScope) val B get() = varOf("B")
context(scope: PrologScope) val C get() = varOf("C")
context(scope: PrologScope) val D get() = varOf("D")
context(scope: PrologScope) val E get() = varOf("E")
context(scope: PrologScope) val F get() = varOf("F")
context(scope: PrologScope) val G get() = varOf("G")
context(scope: PrologScope) val H get() = varOf("H")
context(scope: PrologScope) val I get() = varOf("I")
context(scope: PrologScope) val J get() = varOf("J")
context(scope: PrologScope) val K get() = varOf("K")
context(scope: PrologScope) val L get() = varOf("L")
context(scope: PrologScope) val M get() = varOf("M")
context(scope: PrologScope) val N get() = varOf("N")
context(scope: PrologScope) val O get() = varOf("O")
context(scope: PrologScope) val P get() = varOf("P")
context(scope: PrologScope) val Q get() = varOf("Q")
context(scope: PrologScope) val R get() = varOf("R")
context(scope: PrologScope) val S get() = varOf("S")
context(scope: PrologScope) val T get() = varOf("T")
context(scope: PrologScope) val U get() = varOf("U")
context(scope: PrologScope) val V get() = varOf("V")
context(scope: PrologScope) val W get() = varOf("W")
context(scope: PrologScope) val X get() = varOf("X")
context(scope: PrologScope) val Y get() = varOf("Y")
context(scope: PrologScope) val Z get() = varOf("Z")

/**
 * Object solely for resolution bounding purposes
 */
open class PrologDsl internal constructor() {
    companion object {
        @JvmStatic
        val INSTANCE = PrologDsl()
    }
}

class PrologConv : PrologDsl() {
    companion object {
        @JvmStatic
        val INSTANCE = PrologConv()
    }
}

context(prolog: PrologDsl) operator fun String.invoke(vararg args: Term) = Struct.of(this, args)

val emptyList = Struct.emptyList()
fun cons(a: Term, b: Term) = Struct.of(".", a, b)

/**
 * The _last_ one is the tail (`consOf(A, B, T)` ~ `[A, B | T]`)
 */
context(prolog: PrologDsl) fun consOf(term: Term, vararg terms: Term) = listOf(term, *terms).reduceRight(::cons)

context(prolog: PrologDsl) fun list(vararg terms: Term) = (terms.asList() + emptyList).reduceRight(::cons)

context(prolog: PrologDsl) val `_` get() = Var.of("_") // Important to create new each time!

context(prolog: PrologDsl) infix fun Term.`if`(another: Term): Struct = ":-"(this, another)
context(prolog: PrologDsl) infix fun Term.`,`(another: Term): Struct = ","(this, another)
context(prolog: PrologDsl) infix fun Term.`=`(another: Term): Struct = "="(this, another)

//context(prolog: Prolog) infix fun Term.`=..`(another: Term): Struct = "=.."(this, another)
context(prolog: PrologDsl) infix fun Term.`=@=`(another: Term): Struct = "=@="(this, another)

//context(prolog: Prolog) infix fun Term.`=:=`(another: Term): Struct = "=:="(this, another)
context(prolog: PrologDsl) infix fun Term.`==`(another: Term): Struct = "=="(this, another)
context(prolog: PrologDsl) infix fun Term.`=!=`(another: Term): Struct = "=\\="(this, another)
context(prolog: PrologDsl) infix fun Term.`!=`(another: Term): Struct = "\\="(this, another)
context(prolog: PrologDsl) infix fun Term.`!==`(another: Term): Struct = "\\=="(this, another)
context(prolog: PrologDsl) infix fun Term.`as`(another: Term): Struct = "as"(this, another)
context(prolog: PrologDsl) infix fun Term.`is`(another: Term): Struct = "is"(this, another)
context(prolog: PrologDsl) infix fun Term.xor(another: Term): Struct = "xor"(this, another)
context(prolog: PrologDsl) infix fun Term.rdiv(another: Term): Struct = "rdiv"(this, another)
context(prolog: PrologDsl) infix fun Term.shl(another: Term): Struct = "<<"(this, another)
context(prolog: PrologDsl) infix fun Term.shr(another: Term): Struct = ">>"(this, another)
context(prolog: PrologDsl) infix fun Term.mod(another: Term): Struct = "mod"(this, another)
context(prolog: PrologDsl) infix fun Term.`**`(another: Term): Struct = "**"(this, another)
context(prolog: PrologDsl) infix fun Term.`^`(another: Term): Struct = "^"(this, another)

context(prolog: PrologDsl) operator fun Term.plus(another: Term) = "+"(this, another)
context(prolog: PrologDsl) operator fun Term.minus(another: Term) = "-"(this, another)
context(prolog: PrologDsl) operator fun Term.times(another: Term) = "*"(this, another)
context(prolog: PrologDsl) operator fun Term.div(another: Term) = "/"(this, another)
context(prolog: PrologDsl) operator fun Term.rem(another: Term) = "rem"(this, another)
context(prolog: PrologDsl) operator fun Term.unaryMinus() = "-"(this)

open class RuleContext {
    internal val clauses: MutableList<Term> = mutableListOf()
}

context(ctx: RuleContext) fun cut() = ctx.apply { clauses.add(Struct.cut()) }
context(ctx: RuleContext) val `!` get() = cut()
context(ctx: RuleContext) fun add(term: Term) = ctx.apply { clauses.add(term) }
context(ctx: RuleContext) operator fun Term.unaryPlus() = add(this)
operator fun RuleContext.plus(term: Term) = add(term)
infix fun RuleContext.`,`(term: Term) = add(term)

context(prolog: PrologDsl) operator fun String.invoke(vararg args: Term, body: context(RuleContext) () -> Unit): Term {
    val head = Struct.of(this, args)
    val ctx = RuleContext()
    with(ctx) { body() }
    return Struct.of(":-", head, ctx.clauses.reduce { a, b -> ","(a, b) })
}

context(prolog: PrologDsl) fun <T> scoped(body: context(PrologScope) () -> T): T = with(PrologScope()) { body() }

context(prolog: PrologDsl) fun theory(vararg clauses: Term): Theory = Theory.of(*clauses)

fun <T> prolog(body: context(PrologDsl) () -> T): T = with(PrologDsl.INSTANCE) { body() }

private class MappingIterator<T, R>(val source: Iterator<T>, val mapper: (T) -> R) : Iterator<R> {
    override fun hasNext(): Boolean = source.hasNext()
    override fun next(): R = mapper(source.next())
}

context(prolog: PrologConv) fun Any?.toTerm(): Term = when (this) {
    is Term -> this
    null -> `_`
    is Int, is Long, is Byte, is Short -> PLong.of(toLong())
//    is BigInteger -> PInt.of(this)
    is Float, is Double -> PDouble.of(this.toDouble())
//    is BigDecimal -> Real.of(this)
    is Char -> Struct.atom(toString())
    is String -> Struct.atom(this)
    is Boolean -> Struct.atom(if (this) "true" else "false")
    is Array<*> -> list(*this.map { it.toTerm() }.toTypedArray())
    is Iterable<*> -> asSequence().toTerm()
    is Sequence<*> -> list(*this.map { it.toTerm() }.toList().toTypedArray())
    is Map.Entry<*, *> -> entry(this)
    is Map<*, *> -> entries.map { entry(it) }.toTerm()
    is Record -> struct(
        ",",
        javaClass.getRecordComponents()
            .map { component: RecordComponent? ->
                try {
                    return@map (component!!.accessor.invoke(this)).toTerm()
                } catch (e: IllegalAccessException) {
                    throw AssertionError("Cannot convert record", e)
                } catch (e: InvocationTargetException) {
                    throw AssertionError("Cannot convert record", e)
                }
            }
            .toTypedArray()
    )

    else -> throw IllegalArgumentException("${this.javaClass} can't be converted to Term")
}

context(prolog: PrologConv) operator fun String.invoke(arg: Any?, vararg args: Any?) =
    Struct.of(this, (listOf(arg) + args.asList()).map { it.toTerm() }.toTypedArray())

/**
 * The _last_ one is the tail (`consOf(A, B, T)` ~ `[A, B | T]`)
 */
context(prolog: PrologConv) fun consOf(term: Any?, vararg terms: Any?) =
    listOf(term, *terms).map { it.toTerm() }.reduceRight(::cons)

context(prolog: PrologConv) fun list(vararg terms: Any?) =
    (terms.asList() + listOf(emptyList)).map { it.toTerm() }.reduceRight(::cons)

context(prolog: PrologConv) infix fun Any?.`if`(another: Term): Struct = ":-"(this, another)
context(prolog: PrologConv) infix fun Any?.`=`(another: Term): Struct = "="(this, another)
context(prolog: PrologConv) infix fun Any?.`=@=`(another: Term): Struct = "=@="(this, another)
context(prolog: PrologConv) infix fun Any?.`==`(another: Term): Struct = "=="(this, another)
context(prolog: PrologConv) infix fun Any?.`=!=`(another: Term): Struct = "=\\="(this, another)
context(prolog: PrologConv) infix fun Any?.`!=`(another: Term): Struct = "\\="(this, another)
context(prolog: PrologConv) infix fun Any?.`!==`(another: Term): Struct = "\\=="(this, another)
context(prolog: PrologConv) infix fun Any?.`as`(another: Term): Struct = "as"(this, another)
context(prolog: PrologConv) infix fun Any?.`is`(another: Term): Struct = "is"(this, another)
context(prolog: PrologConv) infix fun Any?.xor(another: Term): Struct = "xor"(this, another)
context(prolog: PrologConv) infix fun Any?.rdiv(another: Term): Struct = "rdiv"(this, another)
context(prolog: PrologConv) infix fun Any?.shl(another: Term): Struct = "<<"(this, another)
context(prolog: PrologConv) infix fun Any?.shr(another: Term): Struct = ">>"(this, another)
context(prolog: PrologConv) infix fun Any?.mod(another: Term): Struct = "mod"(this, another)
context(prolog: PrologConv) infix fun Any?.`**`(another: Term): Struct = "**"(this, another)
context(prolog: PrologConv) infix fun Any?.`^`(another: Term): Struct = "^"(this, another)

context(prolog: PrologConv) infix fun Term.`if`(another: Any?): Struct = ":-"(this, another)
context(prolog: PrologConv) infix fun Term.`=`(another: Any?): Struct = "="(this, another)
context(prolog: PrologConv) infix fun Term.`=@=`(another: Any?): Struct = "=@="(this, another)
context(prolog: PrologConv) infix fun Term.`==`(another: Any?): Struct = "=="(this, another)
context(prolog: PrologConv) infix fun Term.`=!=`(another: Any?): Struct = "=\\="(this, another)
context(prolog: PrologConv) infix fun Term.`!=`(another: Any?): Struct = "\\="(this, another)
context(prolog: PrologConv) infix fun Term.`!==`(another: Any?): Struct = "\\=="(this, another)
context(prolog: PrologConv) infix fun Term.`as`(another: Any?): Struct = "as"(this, another)
context(prolog: PrologConv) infix fun Term.`is`(another: Any?): Struct = "is"(this, another)
context(prolog: PrologConv) infix fun Term.xor(another: Any?): Struct = "xor"(this, another)
context(prolog: PrologConv) infix fun Term.rdiv(another: Any?): Struct = "rdiv"(this, another)
context(prolog: PrologConv) infix fun Term.shl(another: Any?): Struct = "<<"(this, another)
context(prolog: PrologConv) infix fun Term.shr(another: Any?): Struct = ">>"(this, another)
context(prolog: PrologConv) infix fun Term.mod(another: Any?): Struct = "mod"(this, another)
context(prolog: PrologConv) infix fun Term.`**`(another: Any?): Struct = "**"(this, another)
context(prolog: PrologConv) infix fun Term.`^`(another: Any?): Struct = "^"(this, another)

context(prolog: PrologConv) operator fun Any?.plus(another: Term) = "+"(this, another)
context(prolog: PrologConv) operator fun Any?.minus(another: Term) = "-"(this, another)
context(prolog: PrologConv) operator fun Any?.times(another: Term) = "*"(this, another)
context(prolog: PrologConv) operator fun Any?.div(another: Term) = "/"(this, another)
context(prolog: PrologConv) operator fun Any?.rem(another: Term) = "rem"(this, another)
context(prolog: PrologConv) operator fun Term.plus(another: Any?) = "+"(this, another)
context(prolog: PrologConv) operator fun Term.minus(another: Any?) = "-"(this, another)
context(prolog: PrologConv) operator fun Term.times(another: Any?) = "*"(this, another)
context(prolog: PrologConv) operator fun Term.div(another: Any?) = "/"(this, another)
context(prolog: PrologConv) operator fun Term.rem(another: Any?) = "rem"(this, another)

context(prolog: PrologConv) operator fun String.invoke(vararg args: Any?, body: context(RuleContext) () -> Unit) =
    this.invoke(*args.map { it.toTerm() }.toTypedArray(), body = body)

fun <T> prologWithConversions(body: context(PrologConv) () -> T): T = with(PrologConv.INSTANCE) { body() }

private inline fun <reified T, R> cast(context: String?, convertion: (T) -> R): R {
    return convertion(context as T)
}

context(prolog: PrologConv) private fun entry(entry: Map.Entry<*, *>): Term {
    return struct(",", (entry.key).toTerm(), (entry.value).toTerm())
}

context(prolog: PrologConv) fun struct(rule: String, vararg args: Any): Term = Struct.of(
    rule, args.map { it.toTerm() }.toTypedArray()
)


fun Prolog.solve(query: context(PrologScope) () -> Term): Sequence<Map<String, Term>> {
    return prolog {
        val query = scoped<Term>(query)
        var info = solve(query)
        sequence {
            while (info.isSuccess) {
                yield(info.bindingVars.associate { it.name to it.link })
                if (!info.hasOpenAlternatives()) break // ?
                info = solveNext()
            }
            if (info.isHalted) throw AssertionError("Halt ($info)")
        }
    }
}