from nltk.tokenize import sent_tokenize


def lines(a, b):
    """Return lines in both a and b"""

    # decompose strings to line lists
    a_lines = decompose(a)
    b_lines = decompose(b)

    # compare lists and make a list of similar lines
    return findsims(a_lines, b_lines)


def sentences(a, b):
    """Return sentences in both a and b"""

    # decompose strings to sentences
    a_sents = sent_tokenize(a)
    b_sents = sent_tokenize(b)

    # compare lists and make a list of similar lines
    return findsims(a_sents, b_sents)


def substrings(a, b, n):
    """Return substrings of length n in both a and b"""

    # get list of substrings
    a_subs = decompose_substrings(a, n)
    b_subs = decompose_substrings(b, n)

    return findsims(a_subs, b_subs)


# Helper Functions ##############################

def decompose(text):
    lines = []
    line = ""

    for c in text:
        if c == "\n":
            lines.append(line)
            line = ""
            continue
        line += c

    if c != "\n":
        lines.append(line)

    return lines


def decompose_substrings(a, n):
    subs = []

    for i in range(len(a) - n + 1):
        subs.append(a[i:i + n])

    return subs


def findsims(a, b):
    similars = []

    for s in a:
        if s in b and s not in similars:
            similars.append(s)

    return similars
