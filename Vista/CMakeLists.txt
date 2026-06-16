cmake_minimum_required(VERSION 3.10)
project(untitled1 C)

set(CMAKE_C_STANDARD 11)

find_package(SDL2 REQUIRED)
find_package(SDL2_ttf REQUIRED)
add_executable(untitled1
        main.c
        Model/game_state.c
        Controller/client_controller.c
        View/sdl_view.c
        View/menu_sdl.c
        View/menu_sdl.h
)

target_include_directories(untitled1 PRIVATE
        ${SDL2_INCLUDE_DIRS}
        .
)
target_link_libraries(untitled1
        ${SDL2_LIBRARIES}
        SDL2_ttf
        pthread
)
