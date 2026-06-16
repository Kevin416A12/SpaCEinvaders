#include "sdl_view.h"

#include <string.h>
#include <SDL2/SDL.h>

#define CELL_SIZE 3

void view_run(GameState *state, ClientController *controller) {
    SDL_Init(SDL_INIT_VIDEO);

    SDL_Window *window = SDL_CreateWindow(
        "Space Invaders - Cliente C",
        SDL_WINDOWPOS_CENTERED,
        SDL_WINDOWPOS_CENTERED,
        MATRIX_SIZE * CELL_SIZE,
        MATRIX_SIZE * CELL_SIZE,
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

            if (event.type == SDL_KEYDOWN && strcmp(controller->role, "CONTROL") == 0) {
                switch (event.key.keysym.sym) {
                    case SDLK_r:
                        controller_send_message(controller, "Izquierda");
                        break;

                    case SDLK_l:
                        controller_send_message(controller, "Derecha");
                        break;

                    case SDLK_p:
                        controller_send_message(controller, "Disparar");
                        break;

                    default:
                        break;
                }
            }
        }
        char title[100];

        pthread_mutex_lock(&state->mutex);
        int current_score = state->score;
        pthread_mutex_unlock(&state->mutex);

        snprintf(title, sizeof(title), "Space Invaders - Puntaje: %d", current_score);
        SDL_SetWindowTitle(window, title);
        
        SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
        SDL_RenderClear(renderer);

        pthread_mutex_lock(&state->mutex);

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                int v = state->matrix[i][j];

                if (v != 0) {

                    SDL_Rect rect;
                    rect.x = j * CELL_SIZE;
                    rect.y = i * CELL_SIZE;
                    rect.w = CELL_SIZE;
                    rect.h = CELL_SIZE;

                    if (v == 1)
                        SDL_SetRenderDrawColor(renderer, 255,255,255,255);
                    else if (v == 2)
                        SDL_SetRenderDrawColor(renderer, 0,255,255,255);
                    else if (v == 3)
                        SDL_SetRenderDrawColor(renderer, 255,0,255,255);
                    else if (v == 4)
                        SDL_SetRenderDrawColor(renderer, 0,255,0,255);
                    else if (v == 5)
                        SDL_SetRenderDrawColor(renderer, 255,0,0,255);
                    else if (v == 6)
                        SDL_SetRenderDrawColor(renderer, 0,200,0,255);
                    else if (v == 7)
                        SDL_SetRenderDrawColor(renderer, 255,255,0,255);

                    SDL_RenderFillRect(renderer, &rect);
                }
            }
        }

        pthread_mutex_unlock(&state->mutex);

        SDL_RenderPresent(renderer);
        SDL_Delay(16);
    }

    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    SDL_Quit();
}