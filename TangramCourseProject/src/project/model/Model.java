package project.model;

/**
 * There is always a working Tangram set, and perhaps a puzzle that is being solved.
 */
public class Model {
	
	public static Model defaultModel() {
		// Returns a reasonable default
		Model m = new Model();
		
		TangramSet set = StandardSet.produce();
		m.setPieces(set);
		return m;
	}

	TangramSet set;
	
	public void setPieces(TangramSet set) {
		this.set = set;
	}

	public TangramSet getSet() {
		return set;
	}
}
