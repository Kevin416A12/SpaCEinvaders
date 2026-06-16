#include "client_controller.h"

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 2048

static void *receiver_thread(void *arg) {
    ClientController *controller = (ClientController *)arg;
    char buffer[BUFFER_SIZE];

    FILE *socket_file = fdopen(controller->socket_fd, "r");

    if (socket_file == NULL) {
        perror("Error con fdopen");
        return NULL;
    }

    while (fgets(buffer, BUFFER_SIZE, socket_file) != NULL) {
        if (strncmp(buffer, "SCORE", 5) == 0) {
            int nuevo_score = 0;
            sscanf(buffer, "SCORE %d", &nuevo_score);

            pthread_mutex_lock(&controller->game_state->mutex);
            controller->game_state->score = nuevo_score;
            pthread_mutex_unlock(&controller->game_state->mutex);
        }

        if (strncmp(buffer, "MATRIX", 6) == 0) {

            pthread_mutex_lock(&controller->game_state->mutex);

            for (int i = 0; i < MATRIX_SIZE; i++) {
                if (fgets(buffer, BUFFER_SIZE, socket_file) == NULL) {
                    break;
                }

                for (int j = 0; j < MATRIX_SIZE; j++) {
                    controller->game_state->matrix[i][j] = buffer[j] - '0';
                }
            }

            pthread_mutex_unlock(&controller->game_state->mutex);

            fgets(buffer, BUFFER_SIZE, socket_file);
        }
    }

    return NULL;
}

int controller_connect(ClientController *controller, const char *ip, int port) {
    struct sockaddr_in server_addr;

    controller->socket_fd = socket(AF_INET, SOCK_STREAM, 0);

    if (controller->socket_fd < 0) {
        perror("Error creando socket");
        return 0;
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);

    if (inet_pton(AF_INET, ip, &server_addr.sin_addr) <= 0) {
        perror("Direccion IP invalida");
        return 0;
    }

    if (connect(controller->socket_fd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Error conectando al servidor Java");
        return 0;
    }

    return 1;
}

void controller_register(ClientController *controller) {
    char registro[BUFFER_SIZE];

    snprintf(
        registro,
        BUFFER_SIZE,
        "REGISTER %d %s\n",
        controller->partida,
        controller->role
    );

    send(controller->socket_fd, registro, strlen(registro), 0);
}

void controller_start_receiver(ClientController *controller) {
    pthread_t thread;
    pthread_create(&thread, NULL, receiver_thread, controller);
    pthread_detach(thread);
}

void controller_send_message(ClientController *controller, const char *message) {
    char buffer[BUFFER_SIZE];

    snprintf(buffer, BUFFER_SIZE, "%s\n", message);
    send(controller->socket_fd, buffer, strlen(buffer), 0);
}

void controller_close(ClientController *controller) {
    close(controller->socket_fd);
}