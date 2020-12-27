def select_pivot(a, l, r)
  # choose first element as pivot
  # l

  # choose last element as pivot
  # r

  # choose median-of-three as pivot
  if r - l < 2
    l
  else
    mid = (r + l) / 2
    three = [a[l], a[mid], a[r]]

    three.delete(three.max)
    three.delete(three.min)
    median_of_three = three.first

    if median_of_three == a[l]
      l
    elsif median_of_three == a[mid]
      mid
    else
      r
    end
  end
end

def partition(a, pivot_index, l, r)
  a[l], a[pivot_index] = a[pivot_index], a[l]
  pivot = a[l]

  i = j = l + 1
  until j > r
    if a[j] < pivot
      a[i], a[j] = a[j], a[i]
      i += 1
    end
    j += 1
  end

  a[l], a[i - 1] = a[i - 1], a[l]
  i - 1
end

def quicksort(a, l, r)
  return 0 if l >= r
  
  pivot_index_before = select_pivot(a, l, r)
  pivot_index_after = partition(a, pivot_index_before, l, r)

  comparisons_1 = quicksort(a, l, pivot_index_after - 1)
  comparisons_2 = quicksort(a, pivot_index_after + 1, r)

  r - l + comparisons_1 + comparisons_2
end

# a = [1, 6, 2]
# a = (1..10).to_a.reverse
# a = [1]
# a = []
# a = [2, 20, 1, 15, 3, 11, 13, 6, 16, 10, 19, 5, 4, 9, 8, 14, 18, 17, 7, 12] 
# a = [4, 1, 3, 6, 2, 5]
a = IO.readlines('quicksort.txt').map { |i| i.to_i }

p quicksort(a, 0, a.size - 1)
# p a
