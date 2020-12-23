import crypt
import sys

if len(sys.argv) != 2:
    sys.exit(1)

hashed = sys.argv[1]

alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

for c1 in alphabet:
    if hashed == crypt.crypt(c1, c1):
        print(c1)
        sys.exit(0)
    for c2 in alphabet:
        pass2 = c1 + c2
        if hashed == crypt.crypt(pass2, pass2):
            print(pass2)
            sys.exit(0)
        for c3 in alphabet:
            pass3 = pass2 + c3
            if hashed == crypt.crypt(pass3, pass2):
                print(pass3)
                sys.exit(0)
            for c4 in alphabet:
                pass4 = pass3 + c4
                if hashed == crypt.crypt(pass4, pass2):
                    print(pass4)
                    sys.exit(0)
