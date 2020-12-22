// Implements a dictionary's functionality

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

#include "dictionary.h"

unsigned int dsize = 0;

// Returns true if word is in dictionary else false
bool check(const char *word)
{
    int count = 0;
    char c = '\0';
    node *t_pointer = root;

    while (word[count] != '\0')
    {
        c = tolower(word[count]);

        if (t_pointer->alphabet[index_of(c)] == NULL)
        {
            return false;
        }
        else
        {
            t_pointer = t_pointer->alphabet[index_of(c)];
        }

        count++;
    }

    return t_pointer->is_word;
}

// Loads dictionary into memory, returning true if successful else false
bool load(const char *dictionary)
{
    // open dictionary file
    FILE *d_file = fopen(dictionary, "r");
    if (d_file == NULL)
    {
        return false;
    }

    // be sure not to touch the root variable
    root = (node *) calloc(1, sizeof(node));
    node *t_pointer = root;

    // read letters one by one and build the trie
    char c;
    while ((c = fgetc(d_file)) != EOF)
    {
         int index = index_of(c);

        // if c does not designate end of a word...
        if (c != '\n')
        {
            // check if path to next node already exists
            if (t_pointer->alphabet[index] == NULL)
            {
                // it does not exist, so make a new node
                // and link it to the previous one...
                t_pointer->alphabet[index] = (node *) calloc(1, sizeof(node));

                // make sure memory has been allocated, otherwise
                // close the file and unload all that has been
                // loaded so far...
                if (t_pointer->alphabet[index] == NULL)
                {
                    fclose(d_file);
                    r_unload(root);
                    return false;
                }

                // t_pointer must now point to the next node...
                t_pointer = t_pointer->alphabet[index];
            }
            else
            {
                // there is already a path, so just change the value
                // of t_pointer to point to the next node in the path
                t_pointer = t_pointer->alphabet[index];
            }
        }
        else
        {
            // whatever t_pointer is pointing to now must
            // indicate a valid end of word
            // dsize is incremented each time a word has been
            // fully loaded, hence indicates dictionary size
            // also, we must start from the root again
            t_pointer->is_word = true;
            dsize++;
            t_pointer = root;
        }
    }

    // close dictionary file
    fclose(d_file);

    return true;
}

// Returns number of words in dictionary if loaded else 0 if not yet loaded
unsigned int size(void)
{
    // dsize simply indicates dictionary size which was
    // calculated already in load()
    return dsize;
}

// Unloads dictionary from memory, returning true if successful else false
bool unload()
{
    return r_unload(root);
}

// the actual recursive unload function...
bool r_unload(node *t_root)
{
    for (int i = 0; i < ALPHABET; i++)
    {
        if (t_root->alphabet[i] != NULL)
        {
            return r_unload(t_root->alphabet[i]);
        }
    }

    free(t_root);

    return true;
}

// Retrun corresponding index of character c in the node array
unsigned int index_of(char c)
{
    // '\'' is assigned the last entry in the array
    if (c == '\'')
    {
        return ALPHABET - 1;
    }
    else
    {
        return c - 'a';
    }
}