#include <stdio.h>
#include <cs50.h>

int main(int argc, string argv[])
{
    if (argc != 2)
    {
        return 1;
    }

    int key = atoi(argv[1]) % 26;
    string text = get_string("Enter text: ");

    for (int i = 0; text[i] != '\0'; i++)
    {
        char target = text[i];

        if (target >= 'a' && target <= 'z')
        {
            target = (target + key <= 'z') ? target + key : 'a' + (target + key - 'z' - 1);
        }
        else if (target >= 'A' && target <= 'Z')
        {
            target = (target + key <= 'Z') ? target + key : 'A' + (target + key - 'Z' - 1);
        }
        text[i] = target;
    }

    printf("ciphertext: %s\n", text);
}