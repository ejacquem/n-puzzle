#include "npuzzle.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>
#include <limits.h>

int manhattan(int ax, int ay, int bx, int by) {
    return abs(ax - bx) + abs(ay - by);
}

int evaluate(int **grid, int size) {
    int score = 0;

    // printf("Evaluating position: \n");
    // printGrid(grid, size);

    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            int number = grid[i][j];
            int x = number % size;
            int y = number / size;
            score += manhattan(j, i, x, y);
        }
    }

    return score;
}

void solver(int **grid, int size) {
    // coordinate of the 0 case
    int x = 0;
    int y = 0;

    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (grid[i][j] == 0) {
                x = j;
                y = i;
            }
        }
    }

    printGrid(grid, size);

    int bestScore = evaluate(grid, size);

    // UP DOWN LEFT RIGHT
    int dx[4] = {0, 0, -1, 1}; 
    int dy[4] = {-1, 1, 0, 0}; 
    char dirName[4][10] = {"UP", "DOWN", "LEFT", "RIGHT"}; 

    while (bestScore != 0) {
        int score[4] = {-1, -1, -1, -1};

        for (int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if (nx < 0 || nx >= size || ny < 0 || ny >= size)
                continue;

            grid[y][x] = grid[ny][nx]; // move cell
            grid[ny][nx] = 0; // move zero
            
            score[dir] = evaluate(grid, size);
            
            grid[ny][nx] = grid[y][x]; // move cell
            grid[y][x] = 0; // move zero
        }

        printf("scores: %d, %d, %d, %d\n", score[0], score[1], score[2], score[3]);

        int bestDirIndex = -1;
        int min = INT_MAX;
        for (int i = 0; i < 4; i++) {
            if (score[i] != -1 && score[i] < min){
                bestDirIndex = i;
                min = score[i];
            }
        }
        bestScore = min;
        printf("bestScore = %d\n", bestScore);
        printf("%s\n", dirName[bestDirIndex]);

        int nx = x + dx[bestDirIndex];
        int ny = y + dy[bestDirIndex];

        // move in the best direction
        grid[y][x] = grid[ny][nx];
        grid[ny][nx] = 0;

        x = nx;
        y = ny;

        printGrid(grid, size);
        
        // getchar();// blocks here
    }
}

int main(int argc, char **argv)
{
    int **grid;
    int size;
 
    if (argc <= 1) {
        fprintf(stderr, "Usage: %s <file>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    if (argc == 2) {
        getGrid(argv[1], &grid, &size);
    } else {
        for (int i = 1; i < argc; i++)
        {
            if (strcmp(argv[i], "-g") == 0 && argv[i + 1] != NULL) {
                size = atoi(argv[i + 1]);
                grid = generateRandomNPuzzle(size);
                i++;
            } else {
                printf("invalid flag");
                return 1;
            }
        }
    }

    printGrid(grid, size);
    printf("solving...\n");

    solver(grid, size);

    printf("solved!\n");
    printGrid(grid, size);

    return 0;
}