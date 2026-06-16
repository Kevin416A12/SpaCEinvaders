#ifndef GAME_STATE_H
#define GAME_STATE_H

#include <pthread.h>

#define MATRIX_SIZE 256

typedef struct {
    int matrix[MATRIX_SIZE][MATRIX_SIZE];
    int score;
    pthread_mutex_t mutex;
} GameState;

void game_state_init(GameState *state);

#endif