Minimalistic example of a webserver that uses Typst for HTML generation. It uses Ktor for webservering, and TyKo for typsting. 

`RealWorld` provides a virtual FS for the Typst compiler... which is quite an ironic name... When the server receives GET request, 
it sets the respective file to be an entry point of compilation.
The query parameters of request are passed through a virtual file 
named `/__query-parameters.typc`. 

The example file is `main.typ`, which displays `Hello, name!` where name
is passed from query parameter (`/main.typ?name=Dmitry`), or `Hello, World!`
if none are passed.

For an example run, launch the `launch.sh` script.
