package project.model;

import java.util.Optional;


/**
 * There is always a working Tangram set, and perhaps a puzzle that is being solved.
 */
public class Model {
	
	/** Returns a reasonable default. */
	public static Model defaultModel() {
		Model m = new Model();
		
		TangramSet set = StandardSet.produce();
		
		m.setPieces(set);
		
		// use original 'square configuration' as a reasonable default puzzle to start with 
		m.setPuzzle(new Puzzle(set));
		return m;
	}

	TangramSet set;
	Puzzle puzzle;
	
	public void setPieces(TangramSet set) {
		this.set = set;
	}

	public TangramSet getSet() {
		return set;
	}

	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}
	
	/** Return Puzzle as optional, since user must select it. */
	public Optional<Puzzle> getPuzzle() {
		if (puzzle == null) {
			return Optional.empty();
		}
			
		return Optional.of(puzzle);
	}
}
