package org.ldemetrios

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.errors.*
import org.ldemetrios.tyko.compiler.*
import org.ldemetrios.tyko.ffi.TypstSharedLibrary
import org.ldemetrios.tyko.model.*
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.system.exitProcess

val PARAMS_PATH = "${File.separator}__query-parameters.typc"

class RealWorld() : World {
    lateinit var main: Path
    lateinit var params: Map<String, List<String>>

    override fun file(file: FileDescriptor): RResult<ByteArray, FileError> = when (file.pack?.namespace) {
        null -> {
            val f = RealWorld::class.java.classLoader.getResource(file.path.drop(1).replace(File.separator, "/"))

            when {
                file.path == PARAMS_PATH -> RResult.Ok(
                    TDictionary(params.mapValues { TArray(it.value.map(String::t)) }).repr().toByteArray()
                )

                f == null -> RResult.Err(FileError.NotFound(file.path))

                else -> try {
                    RResult.Ok(f.readBytes())
                } catch (e: IOException) {
                    RResult.Err(FileError.Other(e.message))
                }
            }
        }

        else -> RResult.Err(FileError.Package(PackageError.Other("Custom namespace package are not allowed here")))
    }

    override fun library(): StdlibProvider = object : StdlibProvider.Standard {
        override val inputs: TDictionary<TValue> = TDictionary("ktor" to true.t)

        override val features: List<Feature> = listOf(Feature.Html)
    }

    override fun mainFile(): FileDescriptor = FileDescriptor(null, main.toString())

    override fun now(): WorldTime? = WorldTime.System

    override val autoManageCentral: Boolean = true
}

fun main(args: Array<String>) {
    val path = args.getOrNull(1)?.let { Path(it) } ?: Path(".", System.mapLibraryName("typst_shared"))
    val sharedLib = TypstSharedLibrary.instance(path)
    val world = RealWorld()
    val compiler = WorldBasedTypstCompiler(sharedLib, world)

    embeddedServer(Netty, port = args.getOrNull(0)?.toInt() ?: 8080, host = "0.0.0.0") {
        routing {
            get("/shutdown") {
                exitProcess(0)
            }
            get("/{path...}") {
                try {
                    val path = call.parameters.getAll("path")?.joinToString(File.separator) ?: ""

                    compiler.reset()
                    sharedLib.evict_cache(1)
                    world.main = Path(path)
                    world.params = call.request.queryParameters.toMap()
                    val result = compiler.compileHtml()
                    call.respond(
                        TextContent(
                            result,
                            ContentType.Text.Html.withCharset(Charsets.UTF_8),
                            HttpStatusCode.OK
                        )
                    )
                } catch (e: Throwable) {
                    // Proper handling would have too much space
                    call.respondText(e.stackTraceToString())
                }
            }
        }
    }.start(wait = true)
}

