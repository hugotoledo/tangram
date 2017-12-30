package project.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;

import project.model.Model;
import project.model.PlacedPiece;
import project.model.TangramPiece;

/**
 * Shows all Tangram pieces in single panel, meant to be scrolled over.
 * 
 * Each piece is shows in its normal orientation, assuming squareSize pixels for square length
 * 
 */
public class PiecesView extends JPanel {

	/** Model. */
	Model model;

	/** Pieces to be drawn. PlacedPiece knows the TangramPiece and a designated (x,y) location. */
	List<PlacedPiece> pieces = new ArrayList<>();

	/** Size of each square. */
	final int squareSize = 128;

	/** Buffer for between and around pieces. */
	public final int offset = 4;

	/** Off-screen image for drawing (and Graphics object). */
	Image offScreenImage = null;
	Graphics offScreenGraphics = null;

	/** Given a set of Tangram pieces, draw them in this panel. */
	public PiecesView(Model model) {
		this.model = model;
		
		// Compute PlacedPiece for each TangramPiece in set.
		int offset_y = 0;
		for (TangramPiece piece : model.getSet()) {
			PlacedPiece pp = new PlacedPiece (piece, squareSize, new Point (0, offset_y));
			pieces.add(pp);

			offset_y += squareSize;
		}
	}

	/** 
	 * Swing thing. We must be large enough to draw all Tangram pieces. 
	 */
	@Override
	public Dimension getPreferredSize() {
		int width = squareSize + 2*offset;
		int height = 2*offset + pieces.size()*(squareSize+offset);

		return new Dimension (width, height);
	}

	/**
	 * Draw each piece vertically
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (offScreenImage == null) {
			// create on demand
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

	/** Redraws all pieces into offscreen image, simplified by PlacedPiece. */
	void redraw() {
		for (PlacedPiece piece : pieces) {
			Polygon polyshape = piece.getPolygon();
			offScreenGraphics.setColor(Color.black);
			offScreenGraphics.fillPolygon(polyshape);
		}
	}
	
	/** Helper function to map a point to a specific PlacedPiece in our view. */
	Optional<PlacedPiece> find (Point p) {
		for (PlacedPiece piece : pieces) {
			Polygon poly = piece.getPolygon();
			
			if (poly.contains(p)) {
				return Optional.of(piece);
			}
		}
		
		return Optional.empty();
	}
}
