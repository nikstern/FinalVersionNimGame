package FinalVersion5.Graph;
// Nick Dolan-Stern Comp 151 December 2013
/**
 * Created with IntelliJ IDEA.
 * User: nicksirock
 * Date: 11/27/13
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
class Edge<T> {
    private final VertexInterface<T> vertex; // end vertex

    public Edge(VertexInterface<T> endVertex) {
        this.vertex = endVertex;
    } // end constructor

    public VertexInterface<T> getEndVertex() {
        return this.vertex;
    } // end getEndVertex

    public String toString() // for testing only
    {
        return this.vertex.toString();
    } // end toString
} // end Edge

