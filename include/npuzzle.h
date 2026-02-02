#ifndef NPUZZLE_H
#define NPUZZLE_H

#include <stdio.h>
#include <stdlib.h>

/* Grid / parsing */
int     **initGrid(int size);
int     getGrid(char *filename, int ***grid, int *gridSize);
int     **generateRandomNPuzzle(const int size);
void    printGrid(int **grid, int size);

/* Solver / heuristics */
int     manhattan(int ax, int ay, int bx, int by);
int     evaluate(int **grid, int size);
void    solver(int **grid, int size);

#endif
