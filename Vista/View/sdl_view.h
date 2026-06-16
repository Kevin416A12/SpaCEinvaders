#ifndef SDL_VIEW_H
#define SDL_VIEW_H

#include "../Model/game_state.h"
#include "../Controller/client_controller.h"

void view_run(GameState *state, ClientController *controller);

#endif