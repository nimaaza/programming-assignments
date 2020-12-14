#include <cs50.h>
#include <math.h>
#include <stdio.h>

int main(void)
{
    int cents = 0, count = 0;

    do
    {
        cents = round(100 * get_float("Enter owed amount in $: "));
    }
    while (cents < 0);

    (cents / 25 != 0) ? count += cents / 25 : 1 * 1;
    cents %= 25;

    (cents / 10 != 0) ? count += cents / 10 : 1 * 1;
    cents %= 10;

    (cents / 5 != 0) ? count += cents / 5 : 1 * 1;
    cents %= 5;

    count += cents;

    printf("%i\n", count);
}