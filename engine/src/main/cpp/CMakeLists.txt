cmake_minimum_required(VERSION 3.22.1)

project("stockfish_bridge")

add_library(
    stockfish_bridge
    SHARED
    stockfish_bridge.cpp
)

find_library(
    log-lib
    log
)

target_link_libraries(
    stockfish_bridge
    ${log-lib}
)
