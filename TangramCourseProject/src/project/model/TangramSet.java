package project.model;

import java.util.*;

/**
 * A Tangram set is a collection of TangramPieces.
 * 
 * The representation is in Normal Form, which means all pieces are initially identified
 * by their location within the [0, 1] unit square.
 */
public class TangramSet implements Iterable<TangramPiece> {

	List<TangramPiece> pieces = new ArrayList<>();
	
	public void add(TangramPiece piece) {
		pieces.add(piece);
	}
	
	/** Convenient iteration over pieces. */
	public Iterator<TangramPiece> iterator() {
		return pieces.iterator();
	}
	
}
