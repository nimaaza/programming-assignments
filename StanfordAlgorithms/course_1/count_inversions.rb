def count_split_inversions(left_half, right_half)
  both_halves = []
  split_inversions = 0

  until left_half.empty? || right_half.empty?
    if left_half[0] < right_half[0]
      both_halves << left_half.shift
    else
      both_halves << right_half.shift
      split_inversions += left_half.size
    end
  end

  left_half.each { |i| both_halves << i }
  right_half.each { |i| both_halves << i }

  [split_inversions, both_halves]
end

def count_inversions(a)
  if a.size == 1
    [0, [a[0]]]
  else
    mid = a.size / 2

    left_inversions, left_half = count_inversions(a[0...mid])
    right_inversions, right_half = count_inversions(a[mid..])
    split_inversions, both_halves = count_split_inversions(left_half, right_half)

    [left_inversions + split_inversions + right_inversions, both_halves]
  end
end

ints = IO.readlines('inversions.txt').map { |i| i.to_i }
p count_inversions(ints)[0]
