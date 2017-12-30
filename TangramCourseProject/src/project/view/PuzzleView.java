package project.view;

import java.awt.*;
import java.util.*;

import javax.swing.JPanel;

import project.model.Model;
import project.model.PlacedPiece;
import project.model.Puzzle;

/**
 * Here is where the pieces are to be played (in 512x512 size). 
 */
public class PuzzleView extends JPanel {

	/** Core model. */
	Model model;
	
	/** around edges. */
	int offset = 32;
	
	/** Off-screen image for drawing (and Graphics object). */
	Image offScreenImage = null;
	Graphics offScreenGraphics = null;
	
	/** Used to create random colors for pieces. */
	Random rand = new Random();
	Hashtable<PlacedPiece,Color> colorMap = new Hashtable<>();
	
	/** Assign colors randomly as needed. */
	Color getColor(PlacedPiece piece) {
		// initially allocate random color with each piece
		if (!colorMap.containsKey(piece)) {
			colorMap.put(piece, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		}
		return colorMap.get(piece);
	}
	
	/** Given a set of Tangram pieces, draw them in this panel. */
	public PuzzleView(Model model) {
		this.model = model;
	}

	/** 
	 * Swing thing. We must be large enough to draw all Tangram pieces. 
	 */
	@Override
	public Dimension getPreferredSize() {
		int width = 2*Puzzle.Scale + 2*offset;
		int height = 2*Puzzle.Scale + 2*offset;
		
		return new Dimension (width, height);
	}

	/**
	 * Draw background puzzle and all active pieces.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (offScreenImage == null) {
			Dimension s = getPreferredSize();
			offScreenImage = this.createImage(s.width, s.height);
			offScreenGraphics = offScreenImage.getGraphics();

			redraw();
		}

		// if no offscreenImage, then Swing hasn't fully initialized; leave now
		if (offScreenImage == null) {
			System.err.println("Swing not ready for drawing.");
			return;
		}

		// copy image into place.
		g.drawImage(offScreenImage, 0, 0, this);
		
		// double check if no model (for WindowBuilder)
		if (model == null) { return; }
	}
	
	/** Draw background and then all pieces on top of it. */
	public void redraw() {
		
		// Redraw puzzle state, if present.
		if (!model.getPuzzle().isPresent()) { return; }
		
		Dimension dim = getPreferredSize();
		offScreenGraphics.clearRect(0, 0, dim.width, dim.height);
		
		// solution drawn. Could optimize to do just once...
		Puzzle puzzle = model.getPuzzle().get();
		for (Iterator<PlacedPiece> it = puzzle.solution(); it.hasNext(); ) {
			PlacedPiece p = it.next();
			Polygon poly = p.getPolygon();
			offScreenGraphics.setColor(Color.black);
			offScreenGraphics.fillPolygon(poly);
		}
		
		// placed pieces.
		for (Iterator<PlacedPiece> it = puzzle.pieces(); it.hasNext(); ) {
			PlacedPiece p = it.next();
			offScreenGraphics.setColor(getColor(p));
			offScreenGraphics.fillPolygon(p.getPolygon());
		}
	}

	/**
	 * Purpose of this method is to redraw image in the face of changes to the pieces.
	 * In our case, this only means whether a piece is selected or not. 
	 */
	public void refresh() {
		offScreenGraphics.clearRect(0, 0, offScreenImage.getWidth(this), offScreenImage.getHeight(this));
		redraw();
		repaint();
	}
}
