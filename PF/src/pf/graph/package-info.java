/**
 * Provides classes for graph on a board.
 * <p>
 * {@link pf.graph.Graph} contains subgraphs and {@link pf.graph.Vertex}es.
 * It also contains iterators to iterate over subgraphs, vertices, edges
 * <p>
 * {@link pf.graph.Vertex} maps edges between this vertex and another one to {@link pf.graph.Direction}s.
 * Vertex can iterate over all used or unused edges.
 * <p>
 * {@link pf.graph.Edge} joins two {@link pf.graph.Vertex}es.
 * It has a {@link pf.graph.Direction} and used flag.
 * Usage change propagates to both vertices.
 * <p>
 * {@link pf.graph.Path} is a chain of {@link pf.graph.Edge}s.
 * <p>
 * This package also contains default implementations of all interfaces.
 * This package is independent on other packages.  
 */
package pf.graph;