#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>

const int BLOCKSIZE = 512;
typedef uint8_t boolean;
const uint8_t FALSE = 0;
const uint8_t TRUE = 1;

boolean valid_jpeg(int8_t *block);

int main(int argc, char *argv[])
{
    // check for correct number of arguments (2)
    if (argc != 2)
    {
        fprintf(stderr, "Incorrect usage: ./recover path/to/image\n");
        return 1;
    }

    // try opening the image file
    FILE *image = fopen(argv[1], "r");
    if (image == NULL)
    {
        fprintf(stderr, "Unable to open the image file.\n");
        return 2;
    }

    int file_name = 0;
    FILE *recovered = NULL;
    int8_t block[BLOCKSIZE];

    while (fread(block, BLOCKSIZE, 1, image) == 1)
    {
        // check if we are looking at a valid JPEG first block and
        // if so, open a new file for writing its content until we
        // find another valid block...
        if (valid_jpeg(block))
        {
            if (recovered != NULL)
            {
                fclose(recovered);
            }

            char s[3 + 4];
            sprintf(s, "%03d.jpg", file_name);

            recovered = fopen(s, "w");
            if (recovered == NULL)
            {
                // failing to open a file for writing recovered data should
                // terminate the program...
                fprintf(stderr, "Unable to open image file for writing.\n");
                return 3;
            }

            file_name++;
        }

        // when we have a file open for writing it means we
        // are in the middle of a JPEG file reading...
        if (recovered != NULL)
        {
            fwrite(block, BLOCKSIZE, 1, recovered);
        }
    }

    // be a good citizen and close open files
    if (image != NULL)
    {
        fclose(image);
    }

    if (recovered != NULL)
    {
        fclose(recovered);
    }

    return 0;
}

// check for possible JPEG file beginning
boolean valid_jpeg(int8_t *block)
{
    if ((uint8_t) block[0] == 0xff
        && (uint8_t) block[1] == 0xd8
        && (uint8_t) block[2] == 0xff)
    {
        uint8_t i = ((uint8_t) block[3]) >> 4;
        if (i == 0x0e)
        {
            return TRUE;
        }
    }

    return FALSE;
}