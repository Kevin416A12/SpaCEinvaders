#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <SDL2/SDL.h>

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 5000
#define SIZE 32
#define CELL_SIZE 20
#define BUFFER_SIZE 1024

int matriz[SIZE][SIZE];
pthread_mutex_t matriz_mutex = PTHREAD_MUTEX_INITIALIZER;

void *recibir_matriz(void *arg) {
    int sock = *(int *)arg;
    char buffer[BUFFER_SIZE];

    FILE *socket_file = fdopen(sock, "r");

    if (socket_file == NULL) {
        perror("Error con fdopen");
        return NULL;
    }

    while (fgets(buffer, BUFFER_SIZE, socket_file) != NULL) {
        if (strncmp(buffer, "MATRIX", 6) == 0) {

            pthread_mutex_lock(&matriz_mutex);

            for (int i = 0; i < SIZE; i++) {
                fgets(buffer, BUFFER_SIZE, socket_file);

                for (int j = 0; j < SIZE; j++) {
                    matriz[i][j] = buffer[j] - '0';
                }
            }

            pthread_mutex_unlock(&matriz_mutex);

            fgets(buffer, BUFFER_SIZE, socket_file);
        }
    }

    return NULL;
}

void enviar_mensaje(int sock, const char *mensaje) {
    char buffer[BUFFER_SIZE];
    snprintf(buffer, BUFFER_SIZE, "%s\n", mensaje);
    send(sock, buffer, strlen(buffer), 0);
}

int main(int argc, char *argv[]) {
    int sock;
    struct sockaddr_in server_addr;
    char registro[BUFFER_SIZE];

    if (argc != 3) {
        printf("Uso: %s <partida> <CONTROL|DISPLAY>\n", argv[0]);
        return 1;
    }

    int partida = atoi(argv[1]);
    char *rol = argv[2];

    sock = socket(AF_INET, SOCK_STREAM, 0);

    if (sock < 0) {
        perror("Error creando socket");
        return 1;
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);

    if (inet_pton(AF_INET, SERVER_IP, &server_addr.sin_addr) <= 0) {
        perror("Direccion IP invalida");
        close(sock);
        return 1;
    }

    if (connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Error conectando al servidor Java");
        close(sock);
        return 1;
    }

    snprintf(registro, BUFFER_SIZE, "REGISTER %d %s\n", partida, rol);
    send(sock, registro, strlen(registro), 0);

    printf("Conectado a partida %d como %s\n", partida, rol);

    pthread_t hilo_recepcion;
    pthread_create(&hilo_recepcion, NULL, recibir_matriz, &sock);

    SDL_Init(SDL_INIT_VIDEO);

    SDL_Window *window = SDL_CreateWindow(
        "Space Invaders - Cliente C",
        SDL_WINDOWPOS_CENTERED,
        SDL_WINDOWPOS_CENTERED,
        SIZE * CELL_SIZE,
        SIZE * CELL_SIZE,
        0
    );

    SDL_Renderer *renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);

    int running = 1;
    SDL_Event event;

    while (running) {
        while (SDL_PollEvent(&event)) {

            if (event.type == SDL_QUIT) {
                running = 0;
            }

            if (event.type == SDL_KEYDOWN && strcmp(rol, "CONTROL") == 0) {
                switch (event.key.keysym.sym) {
                    case SDLK_a:
                        enviar_mensaje(sock, "Derecha");
                        break;

                    case SDLK_d:
                        enviar_mensaje(sock, "Izquierda");
                        break;

                    case SDLK_p:
                        enviar_mensaje(sock, "Disparar");
                        break;

                    default:
                        break;
                }
            }
        }

        SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
        SDL_RenderClear(renderer);

        pthread_mutex_lock(&matriz_mutex);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matriz[i][j] == 1) {
                    SDL_Rect rect;
                    rect.x = j * CELL_SIZE;
                    rect.y = i * CELL_SIZE;
                    rect.w = CELL_SIZE;
                    rect.h = CELL_SIZE;

                    SDL_SetRenderDrawColor(renderer, 255, 255, 255, 255);
                    SDL_RenderFillRect(renderer, &rect);
                }
            }
        }

        pthread_mutex_unlock(&matriz_mutex);

        SDL_RenderPresent(renderer);
        SDL_Delay(16);
    }

    close(sock);

    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    SDL_Quit();

    return 0;
}