from cs50 import get_float

# get a positive decimal number as user input
while True:
    cents = round(100 * get_float("Enter owed amount: "))
    if cents > 0:
        break

# this will hold the number of coins
count = 0

# check if we can change using 25 cents coins
if cents >= 25:
    count += cents // 25
    cents %= 25

# check if we can change using 10 cents coins
if cents >= 10:
    count += cents // 10
    cents %= 10

# check if we can change using 5 cents coins
if cents >= 5:
    count += cents // 5
    cents %= 5

# whatever has remained will only be changed using
# 1 cent coins, so we just add that number
count += cents

print(count)