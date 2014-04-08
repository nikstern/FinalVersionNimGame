package FinalVersion5.Graph;

import java.util.*;
// Nick Dolan-Stern Comp 151 December 2013
/**
 * A class that implements the ADT directed graph.
 *
 * @author Frank M. Carrano
 * @version 11/26/2013
 * @modifiedBy Anna Bieszczad
 */
public class DirectedGraph<T> {
    private final Map<T, VertexInterface<T>> vertices;

    public DirectedGraph() {
        this.vertices = new TreeMap<>();
    } // end default constructor

    public void addVertex(T vertexLabel) {
        this.vertices.put(vertexLabel, new Vertex<>(vertexLabel));

    } // end addVertex

    public void addEdge(T begin, T end) {

        VertexInterface<T> beginVertex = this.vertices.get(begin);
        VertexInterface<T> endVertex = this.vertices.get(end);

        if ((beginVertex != null) && (endVertex != null))
             beginVertex.connect(endVertex);

    } // end addEdge



    int getNumberOfVertices() {
        return this.vertices.size();
    } // end getNumberOfVertices

    /**
     * Precondition: path is an empty stack (NOT null)
     */
    /**
     * Precondition: path is an empty stack (NOT null)
     */


    // Used for testing
    public void display() {
        System.out.println("Graph has " + getNumberOfVertices() + " vertices.");

        System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
        System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
        Collection<VertexInterface<T>> values = this.vertices.values();
        for (VertexInterface<T> value : values) {
            ((Vertex<T>) (value)).display();
        } // end while
    } // end display

    public ArrayList<VertexInterface<T>> getMapping() {
        ArrayList<VertexInterface<T>> result = new ArrayList<>(0);
        Collection<VertexInterface<T>> values = this.vertices.values();
        Iterator<VertexInterface<T>> vertexIterator = values.iterator();
        for (int i = 0; i < this.vertices.size(); i++)
            result.add(vertexIterator.next());
        return result;
    }
}

