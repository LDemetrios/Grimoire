#!/bin/bash

git clone https://github.com/LDemetrios/typst-shared-library
cd typst-shared-library
cargo build --release
cp target/release/libtypst_shared.so ../libtypst_shared.so || continue
cp target/release/libtypst_shared.dylib ../libtypst_shared.dylib || continue
cp target/release/typst_shared.dll ../typst_shared.dll || continue
cd ..

git clone https://github.com/LDemetrios/LDemetriosCommons
cd LDemetriosCommons
./gradlew publish # Puts to local maven repo
cd ..

git clone https://github.com/LDemetrios/TyKo
cd TyKo
./gradlew publish
cd ..

rm -rf LDemetriosCommons
rm -rf TyKo
rm -rf typst-shared-library
