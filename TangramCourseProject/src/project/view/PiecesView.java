package project.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import project.model.Coordinate;
import project.model.Model;
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

	/** Pieces to be drawn. */
	List<TangramPiece> pieces = new ArrayList<>();

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
		
		for (TangramPiece piece : model.getSet()) {
			pieces.add(piece);
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

	/** Redraws all pieces into offscreen image, offset by center point. */
	void redraw() {
		
		// compute and center all pieces
		int offset_y = 0;
		for (TangramPiece piece : pieces) {
			
			int[] xpoints = new int[piece.size()];
			int[] ypoints = new int[piece.size()];
			
			// convert coordinate sequence into (x,y) points. 
			int idx = 0;
			for (Coordinate c : piece) {
				xpoints[idx] = (int) (squareSize*c.x);
				ypoints[idx] = (int) (offset_y + squareSize*c.y);
				idx++;
			}

			Polygon polyshape = new Polygon(xpoints, ypoints, piece.size());
			
			offScreenGraphics.setColor(Color.black);
			offScreenGraphics.fillPolygon(polyshape);

			offset_y += squareSize;
		}
	}
}
