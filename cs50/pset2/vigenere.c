#include <stdio.h>
#include <string.h>
#include <cs50.h>

char encipher(char target, char next_key);
int shift(char shift_key);
bool alphabetic_key(string key);
bool alphabetic_char(char target);

int main(int argc, string argv[])
{
    if (argc != 2 || !alphabetic_key(argv[1]))
    {
        printf("Usage: ./vigenere k\n");
        return 1;
    }

    string key = argv[1];

    string text = get_string("Enter text: ");

    for (int i = 0, j = 0; text[i] != '\0'; i++)
    {
        text[i] = encipher(text[i], key[j]);

        if (alphabetic_char(text[i + 1]))
        {
            j++;
        }

        if (key[j] == '\0')
        {
            j = 0;
        }
    }

    printf("ciphertext: %s\n", text);
}

char encipher(char target, char next_key)
{
    if (!alphabetic_char(target))
    {
        return target;
    }

    int key = shift(next_key);

    if (target >= 'a' && target <= 'z')
    {
        target = (target + key <= 'z') ? target + key : 'a' + (target + key - 'z' - 1);
    }
    else if (target >= 'A' && target <= 'Z')
    {
        target = (target + key <= 'Z') ? target + key : 'A' + (target + key - 'Z' - 1);
    }

    return target;
}

int shift(char shift_key)
{
    if (shift_key >= 'a' && shift_key <= 'z')
    {
        shift_key += 'A' - 'a';
    }

    return (int) shift_key - 'A';
}

bool alphabetic_key(string key)
{
    int l = strlen(key);

    for (int i = 0; i < l; i++)
    {
        if (!alphabetic_char(key[i]))
        {
            return false;
        }
    }

    return true;
}

bool alphabetic_char(char target)
{
    if (target >= 'a' && target <= 'z')
    {
        return true;
    }
    else if (target >= 'A' && target <= 'Z')
    {
        return true;
    }

    return false;
}