def min_cuts(g)
  until g.size <= 2
    # select a random edge by selecting
    # two connected vertices at random
    random_vertex_start = g.sample
    label = random_vertex_start[1..].sample
    random_vertex_end = g.find { |v| v[0] == label }

    # copy all edges from the end vertex to the other vertex
    random_vertex_end[1..].each { |j| random_vertex_start << j }

    # replace the label for the end vertex everywhere
    # with the label of the start vertex (effectively
    # connect every vertex connected to end vertex to
    # the start vertex)
    g.each do |vertex|
      vertex.each_with_index do |e, index|
        if index.positive?
          vertex[index] = random_vertex_start[0] if e == label
        end
      end
    end

    # delete self-loops and also delete the contracted vertex
    label = random_vertex_start[0]
    random_vertex_start.delete(label)
    random_vertex_start.unshift(label)
    g.delete(random_vertex_end)
  end

  # the number of edges connecting the two remaning
  # edges would be the supposed min cut
  g[0].size - 1
end

# the first element of each vertex is the label
# for the vertex and must be ignored in some of
# the calculations
g = IO.readlines('MinCut.txt').map do |line|
  line.split(/\t/).map { |i| i.to_i }
end

n = (g.size ** 2 * Math.log(g.size)).to_i
min = g.size

n.times do
  new_min = min_cuts(g)
  min = new_min if new_min < min
end

p min
