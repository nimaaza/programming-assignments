#include <cs50.h>
#include <stdio.h>

int main(void)
{
    int height = 0;

    do
    {
        height = get_int("Enter positive height:");
    }
    while (height < 0 || height > 23);

    for (int i = 0; i < height; i++)
    {
        for (int j = 0, first_hash_index = height - i - 1; j <= height; j++)
        {
            if (j < first_hash_index)
            {
                printf(" ");
            }
            else
            {
                printf("#");
            }
        }
        printf("\n");
    }
}