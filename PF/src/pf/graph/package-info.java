/**
 * Provides classes for graph on a board.
 * <p>
 * {@link Graph} contains subgraphs and {@link Vertex}es.
 * It also contains iterators to iterate over subgraphs, vertices, edges
 * <p>
 * {@link Vertex} maps edges between this vertex and another one to {@link Direction}s.
 * Vertex can iterate over all used or unused edges.
 * <p>
 * {@link Edge} joins two {@link Vertex}es. It has a {@link Direction} and used flag.
 * Usage change propagates to both vertices.
 * <p>
 * {@link Path} is a chain of {@link Edge}s.
 * <p>
 * This package also contains default implementations of all interfaces.
 * This packege is independent on other packages.  
 */
package pf.graph;