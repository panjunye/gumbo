cmake_minimum_required(VERSION 3.4.1)

find_library( log-lib log )

include_directories(src/main/cpp/include/)

file(GLOB ZLIB_SOURCES "src/main/cpp/bzlib/*.c")

add_library(bspatch SHARED src/main/cpp/bspatch.c ${ZLIB_SOURCES})

target_link_libraries(bspatch ${log-lib} )
