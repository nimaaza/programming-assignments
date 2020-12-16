// Helper functions for music

#include <cs50.h>
#include <string.h>
#include <math.h>

#include "helpers.h"

// Converts a fraction formatted as X/Y to eighths
int duration(string fraction)
{
    /*
    Figure out the denominator and return
    the correct duration based on that.
    */
    switch (fraction[2])
    {
        case '8':
            return fraction[0] - '0';
            break;
        case '4':
            return (fraction[0] - '0') * 2;
            break;
        case '2':
            return (fraction[0] - '0') * 4;
            break;
        case '1':
            return (fraction[0] - '0') * 8;
            break;
        default:
            return 0;
    }
}

// Calculates frequency (in Hz) of a note
int frequency(string note)
{
    int octave = 4;
    char notel = '\0';
    char acc = '\0';

    // take apart the parts of the note
    if (strlen(note) == 2)
    {
        // length of two has only a note and octave
        notel = note[0];
        octave = note[1] - '0';
    }
    else if (strlen(note) == 3)
    {
        // length of three has a note, an accidental,
        // and an octave
        notel = note[0];
        acc = note[1];
        octave = note[2] - '0';
    }

    float f = 440.0;
    float step = pow(2.0, 1.0 / 12.0);

    // the case for 'A' is not needed in the
    // switch statement...
    switch (notel)
    {
        case 'C':
            f /= pow(step, 9);
            break;
        case 'D':
            f /= pow(step, 7);
            break;
        case 'E':
            f /= pow(step, 5);
            break;
        case 'F':
            f /= pow(step, 4);
            break;
        case 'G':
            f /= pow(step, 2);
            break;
        // case 'A': f *=1; // clearly redundant!
        case 'B':
            f *= pow(step, 2);
            break;
    }

    // correct for the appropriate accidental...
    if (acc == '#')
    {
        f *= step;
    }
    else if (acc == 'b')
    {
        f /= step;
    }

    // correct the frequency for different octaves
    f *= pow(2, octave - 4);

    return round(f);
}

// Determines whether a string represents a rest
bool is_rest(string s)
{
    // couldn't be simpler...!
    return s[0] == '\0';
}
