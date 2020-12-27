import os

from cs50 import SQL
from flask import Flask, flash, redirect, render_template, request, session
from flask_session import Session
from tempfile import mkdtemp
from werkzeug.exceptions import default_exceptions
from werkzeug.security import check_password_hash, generate_password_hash

from helpers import apology, login_required, lookup, usd

# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True

# Ensure responses aren't cached
@app.after_request
def after_request(response):
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response

# Custom filter
app.jinja_env.filters["usd"] = usd

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_FILE_DIR"] = mkdtemp()
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///finance.db")


@app.route("/")
@login_required
def index():
    """Show portfolio of stocks"""
    portofolio = db.execute("SELECT symbol, SUM(number) total_number, SUM(number * price) total_price FROM stocks WHERE user_id=:user_id GROUP BY symbol", user_id=session["user_id"])

    # remove symbols with zero total shares and add current value per share to the list
    stock_list=[]
    for item in portofolio:
        if item["total_number"] != 0:
            stock_list.append({"symbol": item["symbol"],
            "total_number": item["total_number"],
            "price_per_share": usd((lookup(item["symbol"]))["price"]),
            "total_price": usd(item["total_price"])
        })

    # get user balance and compute the total value of their shares and balance
    cash = db.execute("SELECT cash FROM users WHERE id=:user_id", user_id=session["user_id"])
    total_value=cash[0]["cash"]
    for item in portofolio:
        total_value += item["total_price"]

    return render_template("index.html",
                            stock_list=stock_list,
                            cash=usd(cash[0]["cash"]),
                            total_value=usd(total_value))


@app.route("/buy", methods=["GET", "POST"])
@login_required
def buy():
    """Buy shares of stock"""
    if request.method == "POST":
        # get user input and query for stock price
        symbol = request.form.get("symbol")

        if not request.form.get("shares"):
            return apology("A valid number of shares must be provided!")

        info = lookup(symbol)
        if not symbol or not info:
            return apology("No symbol given or it could not be found!")

        try:
            shares = int(request.form.get("shares"))
        except:
            return apology("You must provide a valid number of shares!")

        if shares <= 0:
            return apology("You must provide a positive number of shares!")

        # get user's data and check if they have enough funds
        user = db.execute("SELECT * FROM users WHERE id=:user_id", user_id=session["user_id"])
        if user[0]["cash"] < (shares * info["price"]):
            return apology("You don't have enough cash!")

        # proceed with purchasing the stocks...
        # first, reduce the amount from user's cash
        db.execute("UPDATE users SET cash=:new_amount WHERE id=:user_id", new_amount=user[0]["cash"] - (shares * info["price"]), user_id=session["user_id"])

        # next, record the purchase in database
        db.execute("INSERT INTO stocks (user_id, symbol, number, price, dt) VALUES (:user_id, :name, :number, :price, CURRENT_TIMESTAMP)",
                    user_id=session["user_id"],
                    name=symbol,
                    number=shares,
                    price=info["price"])

        return redirect("/")

    elif request.method == "GET":
        return render_template("buy.html")


@app.route("/history")
@login_required
def history():
    """Show history of transactions"""
    his = db.execute("SELECT * FROM stocks WHERE user_id=:user_id", user_id=session["user_id"])

    return render_template("/history.html", records=his)


@app.route("/login", methods=["GET", "POST"])
def login():
    """Log user in"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE username=:username",
                          username=request.form.get("username"))

        # Ensure username exists and password is correct
        if len(rows) != 1 or not check_password_hash(rows[0]["hash"], request.form.get("password")):
            return apology("invalid username and/or password", 403)

        # Remember which user has logged in
        session["user_id"] = rows[0]["id"]

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("login.html")


@app.route("/change", methods=["GET", "POST"])
@login_required
def change():
    """Change user password"""
    if request.method == "POST":
        # check requested data is provided and the two new passwords match
        old = request.form.get("password")
        new = request.form.get("new")
        conf = request.form.get("confirmation")

        if not old or not new or not conf:
            return apology("Please provide old and new passwords and a confirmation!")
        if new != conf:
            return apology("The provided new passwords don't match!")

        #old_hashed = generate_password_hash(old, method='pbkdf2:sha256', salt_length=8)
        new_hashed = generate_password_hash(new, method='pbkdf2:sha256', salt_length=8)

        # get the user's current password and check it with the given current one
        user = db.execute("SELECT username, hash FROM users WHERE id=:user_id", user_id=session["user_id"])

        if len(user) == 0 or user[0]["username"] != request.form.get("username"):
            return apology("Given username is invalid!")

        if not check_password_hash(user[0]["hash"], old):
            return apology("The old and new passwords don't match!")

        # at this point we can proceed with updating the user's password
        db.execute("UPDATE users SET hash=:new_hash WHERE id=:user_id", new_hash=new_hashed, user_id=session["user_id"])

        # redirect user to the index page
        return redirect("/")

    elif request.method == "GET":
        return render_template("change.html")


@app.route("/logout")
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    # Redirect user to login form
    return redirect("/")


@app.route("/quote", methods=["GET", "POST"])
@login_required
def quote():
    """Get stock quote."""
    if request.method == "POST":
        quote = lookup(request.form.get("symbol"))
        if not quote:
            return apology("Quote symbol not found!")
        else:
            return render_template("/quoted.html", name=quote["name"], price=usd(quote["price"]), symbol=quote["symbol"])
    elif request.method == "GET":
        return render_template("/quote.html")


@app.route("/register", methods=["GET", "POST"])
def register():
    if request.method == "POST":
        # load user input data from form
        username = request.form.get("username")
        password = request.form.get("password")
        confirmation = request.form.get("confirmation")

        if not username:
            return apology("Please provide a username!", 400)

        # check the username is not already in database
        users = db.execute("SELECT * FROM users WHERE username=:username", username=username)
        if len(users) > 0:
            return apology("User name already is taken!", 400)

        # check passwords (both passwords exist and match)
        if not password or not confirmation:
            return apology("Please provide password!", 400)

        if password != confirmation:
            return apology("Passwords don't match!", 400)

        # everything seems OK, add user to database and login
        hashed = generate_password_hash(password, method='pbkdf2:sha256', salt_length=8)
        db.execute("INSERT INTO users (username, hash) VALUES (:username, :hash)", username=username, hash=hashed)

        return render_template("/login.html")

    elif request.method == "GET":
        return render_template("register.html")


@app.route("/sell", methods=["GET", "POST"])
@login_required
def sell():
    """Sell shares of stock"""
    if request.method == "POST":
        # check for correct data input
        if not request.form.get("symbol"):
            return apology("You should provide a symbol!")
        if not request.form.get("shares"):
            return apology("A numeric value must be given for shares")

        symbol = request.form.get("symbol")

        try:
            shares = int(request.form.get("shares"))
        except:
            return apology("A valid number of shares must be provided")

        if shares <= 0:
            return apology("Number of shares must be a positive number")

        # make sure user has the right stock and enough number of stocks
        user_data = db.execute("SELECT SUM(number) total_number FROM stocks WHERE user_id=:user_id GROUP BY :symbol",
                               user_id=session["user_id"],
                               symbol=symbol)

        if len(user_data) < 1 or user_data[0]["total_number"] < shares:
            return apology("You may not have enough number of stocks to sell!")

        # things seem OK, proceed with the purchase
        # calculate the current value of shares
        info = lookup(symbol)
        if not info:
            return apology("Stock not found!")
        current_value = shares * info["price"]
        new_cash = current_value + (db.execute("SELECT cash FROM users WHERE id=:user_id",
                                                user_id=session["user_id"]))[0]["cash"]

        db.execute("UPDATE users SET cash=:new_cash WHERE id=:user_id",
                   new_cash=new_cash, user_id=session["user_id"])

        # record the selling of the stocks in stocks database
        shares = -shares
        db.execute("INSERT INTO stocks (user_id, symbol, number, price, dt) VALUES (:user_id, :name, :number, :price, CURRENT_TIMESTAMP)",
                   user_id=session["user_id"],
                   name=symbol,
                   number=shares,
                   price=info["price"])

        return redirect("/")

    elif request.method == "GET":
        symbols_and_shares = db.execute("SELECT symbol, SUM(number) total_number FROM stocks GROUP BY symbol")
        symbols = []
        for item in symbols_and_shares:
            if item["total_number"] != 0:
                symbols.append({"symbol": item["symbol"]})
        return render_template("sell.html", symbols=symbols)


def errorhandler(e):
    """Handle error"""
    return apology(e.name, e.code)


# listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)