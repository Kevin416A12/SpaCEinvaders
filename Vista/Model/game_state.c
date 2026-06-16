#include "game_state.h"

void game_state_init(GameState *state) {
    pthread_mutex_init(&state->mutex, NULL);
    state->score = 0;

    for (int i = 0; i < MATRIX_SIZE; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            state->matrix[i][j] = 0;
        }
    }
}