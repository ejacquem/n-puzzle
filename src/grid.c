#include "npuzzle.h"

#include <string.h>
#include <ctype.h>
#include <fcntl.h>
#include <unistd.h>
#include <time.h>

void printGrid(int **grid, int size){
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            printf("%d ", grid[i][j]);
        }
        printf("\n");
    }
}

int **initGrid(int size) {
    int **grid = malloc(size);
    memset(grid, 0, size);
    for (int i = 0; i < size; i++) {
        grid[i] = malloc(size);
        memset(grid[i], 0, size);
    }
    return grid;
}

int getGrid(char *filename, int ***grid, int *gridSize) 
{
    FILE *stream;
    char *line = NULL;
    size_t len = 0;
    ssize_t nread;
    *gridSize = -1;

    stream = fopen(filename, "r");
    if (stream == NULL) {
        perror("fopen");
        return 1;
    }

    int row = 0;
    while ((nread = getline(&line, &len, stream)) != -1) {
        // printf("Retrieved line: %s", line);
        int index = 0;
        if (line[index] == '\n') continue;
        while (isspace(line[index])) index++;
        if (*gridSize == -1) {
            if (line[0] == '#') continue;
            *gridSize = atoi(line);
            // printf("gridSize = %d\n", *gridSize);
            *grid = initGrid(*gridSize);
        } else {
            for (int col = 0; col < *gridSize; col++) {
                if (!isdigit(line[index])) return (perror("invalid number"), 1);
                (*grid)[row][col] = atoi(line + index);
                while (isdigit(line[index])) index++;
                while (isspace(line[index])) index++;
                if (line[0] == '#') continue;
            }
            row++;
        }
    }
    if (row != *gridSize) {
        perror("invalid number of row");
    }
    return EXIT_SUCCESS;
}

int **generateRandomNPuzzle(const int size) {
    int **grid = initGrid(size);
    srand(time(NULL));

    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            grid[i][j] = i * size + j;
        }
    }

    int x = 0, y = 0;
    int dx[4] = {0, 0, -1, 1};
    int dy[4] = {-1, 1, 0, 0};

    for (int i = 0; i < size * size * size; i++) {
        int randomDir = rand() % 4;
        int nx = x + dx[randomDir];
        int ny = y + dy[randomDir];

        if (nx < 0 || nx >= size || ny < 0 || ny >= size)
            continue;

        grid[y][x] = grid[ny][nx]; // move cell
        grid[ny][nx] = 0; // move zero

        x = nx;
        y = ny;
    }

    return grid;
}

int create_and_write_file(const char *filename, char *content)
{
    int fd = open(filename, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (fd < 0)
        return 1;
    write(fd, content, strlen(content));
    close(fd);
    return 0;
}