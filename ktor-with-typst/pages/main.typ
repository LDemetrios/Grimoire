#let params = if sys.inputs.at("ktor", default: false) {
    eval(read("/__query-parameters.typc"))
} else { (:) }

Hello, #params.at("name", default:("World",)).at(0)!
