#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Model/game_state.h"
#include "Controller/client_controller.h"
#include "View/sdl_view.h"
#include "View/menu_sdl.h"

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 5000

int main(int argc, char *argv[]) {

    // =========================
    // 1. LANZAR SERVIDOR JAVA
    // =========================
    printf("Iniciando servidor Java...\n");

    int ret = system("java -jar server.jar > server.log 2>&1 &");

    if (ret == -1) {
        printf("ERROR: no se pudo iniciar el servidor Java\n");
        return 1;
    }

    // Pequeña pausa para que levante el server
    system("sleep 1");

    // =========================
    // 2. INICIALIZAR ESTADO
    // =========================
    GameState state;
    game_state_init(&state);

    ClientController controller;
    controller.game_state = &state;

    // =========================
    // 3. MENU SDL
    // =========================
    MenuResult menu = menu_select();

    if (menu.partida == -1) {
        printf("Salida del juego\n");
        return 0;
    }

    controller.partida = menu.partida;

    if (menu.isControl)
        strcpy(controller.role, "CONTROL");
    else
        strcpy(controller.role, "DISPLAY");

    // =========================
    // 4. CONEXIÓN AL SERVIDOR
    // =========================
    printf("Conectando al servidor...\n");

    if (!controller_connect(&controller, SERVER_IP, SERVER_PORT)) {
        printf("ERROR: no se pudo conectar al servidor\n");
        return 1;
    }

    controller_register(&controller);
    controller_start_receiver(&controller);

    printf("Conectado a partida %d como %s\n",
           controller.partida,
           controller.role);

    // =========================
    // 5. INICIAR JUEGO
    // =========================
    view_run(&state, &controller);

    controller_close(&controller);

    return 0;
}