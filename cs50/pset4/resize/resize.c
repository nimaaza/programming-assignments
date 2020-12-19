#include <stdio.h>
#include <stdlib.h>
#include "bmp.h"

const uint16_t BM = 0x4D42;

int main(int argc, char *argv[])
{
	// check for the correct number of arguments
	if (argc != 4)
	{
		fprintf(stderr, "Usage: copy infile outfile\n");
		return 1;
	}

	int n = atoi(argv[1]);

	// open input and output files, check if input is a valid BMP,
	// and check if opening the files suceeded
	FILE *in = fopen(argv[2], "r");
	if (in == NULL)
	{
		fprintf(stderr, "Failed to open: %s.\n", argv[2]);
		return 2;
	}

	// read in the input's headers and check
	// whether the input file is a valid BMP
	BITMAPFILEHEADER in_header;
	BITMAPINFOHEADER in_info_header;

	fread(&in_header, sizeof(BITMAPFILEHEADER), 1, in);
	fread(&in_info_header, sizeof(BITMAPINFOHEADER), 1, in);

	if(in_header.bfType != BM)
	{
		fprintf(stderr, "Input file is not a valid BMP.\n");
		fclose(in);
		return 4;
	}

	FILE *out = fopen(argv[3], "w");
	if (out == NULL)
	{
		fprintf(stderr, "Failed to open: %s.\n", argv[3]);
		fclose(in);
		return 3;
	}

	// create the output file headers and fill them with appropriate data
	BITMAPFILEHEADER out_header;
	BITMAPINFOHEADER out_info_header;

    // calculate the padding for the input and output files
    int in_padding = (4 - (in_info_header.biWidth * sizeof(RGBTRIPLE)) % 4) % 4;
	int out_padding = (4 - (in_info_header.biWidth * n * sizeof(RGBTRIPLE)) % 4) % 4;

	out_info_header.biSize = in_info_header.biSize;
	out_info_header.biWidth = in_info_header.biWidth * n;
	out_info_header.biHeight = in_info_header.biHeight * n;
	out_info_header.biPlanes = in_info_header.biPlanes;
	out_info_header.biBitCount = in_info_header.biBitCount;
	out_info_header.biCompression = in_info_header.biCompression;
	out_info_header.biSizeImage = out_info_header.biWidth
	                               * sizeof(RGBTRIPLE)
	                               * abs(out_info_header.biHeight)
	                               + abs(out_info_header.biHeight)
	                               * out_padding;
	out_info_header.biXPelsPerMeter = in_info_header.biXPelsPerMeter;
	out_info_header.biYPelsPerMeter = in_info_header.biYPelsPerMeter;
	out_info_header.biClrUsed = in_info_header.biClrUsed;
	out_info_header.biClrImportant = in_info_header.biClrImportant;

    out_header.bfType = BM;
	out_header.bfSize = sizeof(BITMAPFILEHEADER)
						+ sizeof(BITMAPINFOHEADER)
						+ out_info_header.biSizeImage;
	out_header.bfReserved1 = out_header.bfReserved2 = 0x0000;
	out_header.bfOffBits = in_header.bfOffBits;

	fwrite(&out_header, sizeof(BITMAPFILEHEADER), 1, out);
	fwrite(&out_info_header, sizeof(BITMAPINFOHEADER), 1, out);

	// create an array to hold the resized scanlines
	RGBTRIPLE *scanline = malloc(sizeof(RGBTRIPLE) * out_info_header.biWidth);

	// start the actuall resizing
	for (int i = 0; i < abs(in_info_header.biHeight); i++)
	{
		// read one scanline from input file and resize it at the same time
		for (int j = 0; j < in_info_header.biWidth * n; j += n)
		{
			// read the RGB triple
			fread(&scanline[j], sizeof(RGBTRIPLE), 1, in);

			// copy it n - 1 times more and move j forward accordingly
			int c = 1;
			while (c < n)
			{
				// scanline[j + c].rgbtBlue = scanline[j].rgbtBlue;
				// scanline[j + c].rgbtGreen = scanline[j].rgbtGreen;
				// scanline[j + c].rgbtRed = scanline[j].rgbtRed;
				scanline[j + c] = scanline[j];
				c++;
			}
		}

		// now write the extended scanline n times with paddings if needed
		int c = 1;
		while (c <= n)
		{
			fwrite(scanline, sizeof(RGBTRIPLE) * out_info_header.biWidth, 1, out);
			for (int k = 0; k < out_padding; k++)
			{
				fputc(0x00, out);
			}
			c++;
		}

		// skip over the input file's padding
		fseek(in, in_padding, SEEK_CUR);
	}

	// let go of the allocated memory
	free(scanline);

	// close the files
	fclose(in);
	fclose(out);

    // we were successful!
    return 0;
}