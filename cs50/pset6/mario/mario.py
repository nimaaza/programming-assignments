from cs50 import get_int

# Prompt the user for the height of the pyramid.
while True:
    n = get_int("Enter height: ")
    # Make sure the input integer is within the allowed range.
    if n >= 0 and n <= 23:
        break

# Print the pyramid.
for i in range(1, n + 1):
    # print spaces
    print(" " * (n - i), end="")
    # print #'s
    print("#" * (i + 1), end="")
    # print new line
    print()