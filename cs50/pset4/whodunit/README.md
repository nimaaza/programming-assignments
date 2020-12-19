# Questions

## What's `stdint.h`?

## `stdint.h` is a header file in which new data types are defined based on other primitve data types in such a way that
## each of the newly defined types have a fixed known size independent of specific implimentation.

## What's the point of using `uint8_t`, `uint32_t`, `int32_t`, and `uint16_t` in a program?

## These types are defined in such a way that the number of bits for each of the types is fixed. `uint8_t` always has 8 bits
## independent of specific system implimentation. There are useful whenever we need to be certain that our program is going
## to deal with 8, 16, 32 bits. The original primitive data types cannot be guaranteed to always have a fixed length. `int`
## might, for example, consist of two or four bytes.

## How many bytes is a `BYTE`, a `DWORD`, a `LONG`, and a `WORD`, respectively?

## `BYTE` is a single byte (8 bits), `WORD` is 2 bytes, and `DWORD` and `LONG` are 4 bytes.

## What (in ASCII, decimal, or hexadecimal) must the first two bytes of any BMP file be? Leading bytes used to identify file formats (with high probability) are generally called "magic numbers."

## BM

## What's the difference between `bfSize` and `biSize`?

## `bfSize` is the total size of the file whereas `biSize` designates the size of file header structures.

## What does it mean if `biHeight` is negative?

## It indicates that the image pixels are stored in a top-down direction with the top of the image starting
## at the upper left corner.

## What field in `BITMAPINFOHEADER` specifies the BMP's color depth (i.e., bits per pixel)?

## `biBitCount` in `BITMAPINFOHEADER` is the number of bits per pixel, hence designated the number
## of colors that can be stored.

## Why might `fopen` return `NULL` in lines 24 and 32 of `copy.c`?

## Because it may fail to properly open a file or create one.

## Why is the third argument to `fread` always `1` in our code?

## Because there is either only one header structure to read (not multiple headers) or we want to read only
## one pixel in each read operation.

## What value does line 63 of `copy.c` assign to `padding` if `bi.biWidth` is `3`?

## Substituting the respective values we get (4 -(3x3 % 4)) % 4 = (4 - 1) % 4 = 3 % 4 = 3.

## What does `fseek` do?

## In general it allows us to read/write from/to a specific location in a file. In `copy.c` fseek allows us to ignore
## padding by jumping over the file as far as the size of padding.

## What is `SEEK_CUR`?

## `SEEK_CUR` means to change the location in the file relative to the current position.
