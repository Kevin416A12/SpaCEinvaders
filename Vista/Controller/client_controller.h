#ifndef CLIENT_CONTROLLER_H
#define CLIENT_CONTROLLER_H

#include "../Model/game_state.h"

typedef struct {
    int socket_fd;
    int partida;
    char role[20];
    GameState *game_state;
} ClientController;

int controller_connect(ClientController *controller, const char *ip, int port);
void controller_register(ClientController *controller);
void controller_start_receiver(ClientController *controller);
void controller_send_message(ClientController *controller, const char *message);
void controller_close(ClientController *controller);

#endif