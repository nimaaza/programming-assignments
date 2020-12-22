// Declares a dictionary's functionality

#ifndef DICTIONARY_H
#define DICTIONARY_H

#include <stdbool.h>

// Maximum length for a word
// (e.g., pneumonoultramicroscopicsilicovolcanoconiosis)
#define LENGTH 45
// the number of letters in English alphabet + '\''
#define ALPHABET 27

typedef struct node
{
    bool is_word;
    struct node *alphabet[ALPHABET];
} node;

node *root;

// Prototypes
bool check(const char *word);
bool load(const char *dictionary);
unsigned int size(void);
//unsigned int r_size(node *t_root);
bool unload();
bool r_unload(node *root);
unsigned int index_of(char c);

#endif // DICTIONARY_H
