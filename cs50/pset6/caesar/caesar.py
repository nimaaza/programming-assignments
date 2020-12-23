from cs50 import get_string
import sys

# make sure the cipher has been given on command line
if len(sys.argv) != 2:
    sys.exit(1)

# covert the passed argument to integer
key = int(sys.argv[1]) % 26
# prompt user for text
text = get_string("Enter text: ")

# some useful constants
a = ord('a')
z = ord('z')
A = ord('A')
Z = ord('Z')

ciphertext = ""

# parse through text characters and encode them if alphabetic
for c in text:
    # convert c to ASCII value for
    # easy comparison and calculation
    target = ord(c)

    if (target >= a and target <= z):
        target -= a
        target += key
        target %= 26
        target += a
    elif (target >= A and target <= Z):
        target -= A
        target += key
        target %= 26
        target += A

    # convert the ASCII code to char
    ciphertext += chr(target)

print(f"ciphertext: {ciphertext}")
