#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <stdio.h>
#include "menu_sdl.h"

void draw_text(SDL_Renderer *ren, TTF_Font *font,
               const char *text, int x, int y) {

    SDL_Color color = {255, 255, 255};

    SDL_Surface *surface = TTF_RenderText_Solid(font, text, color);
    SDL_Texture *texture = SDL_CreateTextureFromSurface(ren, surface);

    SDL_Rect dst;
    dst.x = x;
    dst.y = y;
    dst.w = surface->w;
    dst.h = surface->h;

    SDL_RenderCopy(ren, texture, NULL, &dst);

    SDL_FreeSurface(surface);
    SDL_DestroyTexture(texture);
}

MenuResult menu_select() {

    SDL_Init(SDL_INIT_VIDEO);
    TTF_Init();

    SDL_Window *win = SDL_CreateWindow(
        "Space Invaders Menu",
        SDL_WINDOWPOS_CENTERED,
        SDL_WINDOWPOS_CENTERED,
        700, 500,
        0
    );

    SDL_Renderer *ren = SDL_CreateRenderer(win, -1, 0);

    // Fuente del sistema Linux
    TTF_Font *font = TTF_OpenFont(
        "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
        24
    );

    if (!font) {
        printf("Error cargando fuente TTF\n");
    }

    SDL_Event e;

    int partida = 1;
    int isControl = 1;
    int running = 1;

    while (running) {

        while (SDL_PollEvent(&e)) {

            if (e.type == SDL_QUIT) {
                partida = -1;
                running = 0;
            }

            if (e.type == SDL_KEYDOWN) {

                switch (e.key.keysym.sym) {

                    case SDLK_ESCAPE:
                        partida = -1;
                        running = 0;
                        break;

                    case SDLK_1:
                        partida = 1;
                        break;

                    case SDLK_2:
                        partida = 2;
                        break;

                    case SDLK_c:
                        isControl = 1;
                        break;

                    case SDLK_d:
                        isControl = 0;
                        break;

                    case SDLK_RETURN:
                        running = 0;
                        break;
                }
            }
        }

        // =========================
        // BACKGROUND
        // =========================
        SDL_SetRenderDrawColor(ren, 10, 10, 10, 255);
        SDL_RenderClear(ren);

        // =========================
        // TITULO
        // =========================
        draw_text(ren, font, "SPACE INVADERS", 220, 40);

        // =========================
        // PARTIDAS
        // =========================
        draw_text(ren, font, "1 - PARTIDA 1", 100, 140);
        draw_text(ren, font, "2 - PARTIDA 2", 100, 180);

        if (partida == 1)
            draw_text(ren, font, "<--", 420, 140);

        if (partida == 2)
            draw_text(ren, font, "<--", 420, 180);

        // =========================
        // MODOS
        // =========================
        draw_text(ren, font, "C - CONTROL (JUGAR)", 100, 260);
        draw_text(ren, font, "D - DISPLAY (ESPECTADOR)", 100, 300);

        if (isControl)
            draw_text(ren, font, "SELECTED", 420, 260);
        else
            draw_text(ren, font, "SELECTED", 420, 300);

        // =========================
        // INSTRUCCIONES
        // =========================
        draw_text(ren, font, "ENTER - INICIAR JUEGO", 100, 380);
        draw_text(ren, font, "ESC - CERRAR", 100, 420);

        SDL_RenderPresent(ren);
        SDL_Delay(16);
    }

    // =========================
    // CLEANUP
    // =========================
    TTF_CloseFont(font);
    SDL_DestroyRenderer(ren);
    SDL_DestroyWindow(win);

    TTF_Quit();
    SDL_Quit();

    MenuResult result;
    result.partida = partida;
    result.isControl = isControl;

    return result;
}