def greater_than(a, b)
  if a.size == b.size
    a_digits = a.split('').map { |digit| digit.to_i }
    b_digits = b.split('').map { |digit| digit.to_i }
    comparison = 0

    until a_digits.empty? || !comparison.zero?
      a_digit = a_digits.shift
      b_digit = b_digits.shift
      comparison =  1 if a_digit > b_digit 
      comparison = -1 if a_digit < b_digit
    end
  else
    comparison = a.size > b.size ? 1 : -1
  end

  comparison
end

def negative_number?(a)
  a[0] == '-'
end

def positive_number?(a)
  !negative_number?(a)
end

def simple_add(a, b)
  if a.size > b.size
    longer = a.split('')
    shorter = b.split('')
  else
    longer = b.split('')
    shorter = a.split('')
  end
  
  addition = []
  carry = 0
  
  longer.reverse_each do |longer_digit|
    longer_digit = longer_digit.to_i
    shorter_digit = shorter.pop.to_i
    
    digits_addition = longer_digit + shorter_digit + carry
    carry = digits_addition / 10
    next_digit = digits_addition % 10
    
    addition.insert(0, next_digit)
  end
  
  addition.insert(0, 1) if carry == 1
  
  addition.join
end

def simple_subtract(a, b)
  comparison = greater_than(a, b)
  
  if comparison == 1
    bigger = a.split('')
    smaller = b.split('')
  elsif comparison == -1
    bigger = b.split('')
    smaller = a.split('')
  else
    return '0'
  end
  
  difference = []
  carry = 0
  
  bigger.reverse_each do |bigger_digit|
    bigger_digit = bigger_digit.to_i
    smaller_digit = smaller.pop.to_i
    
    digits_difference = bigger_digit - smaller_digit + carry
    
    if digits_difference.negative?
      digits_difference += 10
      carry = -1
    else
      carry = 0
    end
    
    difference.insert(0, digits_difference)
  end
  
  difference = difference.drop_while { |digit| digit == 0 }
  difference.insert(0, '-') if comparison == -1
  
  difference.join
end

def add(a, b)
  if positive_number?(a) && positive_number?(b)
    simple_add(a, b)
  elsif positive_number?(a) && negative_number?(b)
    b = b.delete_prefix('-')
    simple_subtract(a, b)
  elsif negative_number?(a) && positive_number?(b)
    a = a.delete_prefix('-')
    simple_subtract(b, a)
  else
    a = a.delete_prefix('-')
    b = b.delete_prefix('-')
    addition = simple_add(a, b)
    "-#{addition}"
  end
end

def subtract(a, b)
  if positive_number?(a) && positive_number?(b)
    simple_subtract(a, b)
  elsif positive_number?(a) && negative_number?(b)
    b = b.delete_prefix('-')
    simple_add(a, b)
  elsif negative_number?(a) && positive_number?(b)
    a = a.delete_prefix('-')
    addition = simple_add(b, a)
    "-#{addition}"
  else
    a = a.delete_prefix('-')
    b = b.delete_prefix('-')
    simple_subtract(b, a)
  end 
end

def multiply(x, y)
  if x.size < y.size
    x.insert(0, '0') until x.size == y.size
  elsif x.size > y.size
    y.insert(0, '0') until x.size == y.size
  end
  
  if x.size == 1
    (x.to_i * y.to_i).to_s
  else
    m = (x.size / 2).ceil
    split_point =  x.size - m
    
    a = x[0...split_point]
    b = x[split_point..-1]
    c = y[0...split_point]
    d = y[split_point..-1]
    
    s = add a, b
    t = add c, d

    ac = multiply a, c
    bd = multiply b, d
    st = multiply s, t

    adbc = subtract st, (add ac, bd)
    
    ac_with_zeros = "#{ac}#{'0' * (m * 2)}"
    adbc_with_zeros = "#{adbc}#{'0' * m}"
    add ac_with_zeros,  (add adbc_with_zeros, bd)
  end
end

long_integer1 = 3141592653589793238462643383279502884197169399375105820974944592
long_integer2 = 2718281828459045235360287471352662497757247093699959574966967627

a = multiply long_integer1.to_s, long_integer2.to_s
b = long_integer1 * long_integer2 

puts subtract a, b.to_s