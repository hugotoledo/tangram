package project.controller;

import java.awt.Point;

import project.model.Model;
import project.model.PlacedPiece;
import project.model.Puzzle;
import project.model.TangramPiece;
import project.view.TangramApplication;

public class AddPieceController {

	TangramApplication app;
	Model model;

	public AddPieceController(TangramApplication app, Model model) {
		this.app = app;
		this.model = model;
	}
	
	public boolean add(TangramPiece piece) {
		if (!model.getPuzzle().isPresent()) { return false; }
		
		PlacedPiece p = new PlacedPiece(piece, Puzzle.Scale, new Point(0,0));
		model.getPuzzle().get().add(p);
		
//		int x = Puzzle.Scale/2 - (int)(Puzzle.Scale*p.getPiece().getCenter().x);
//		int y = Puzzle.Scale/2 - (int)(Puzzle.Scale*p.getPiece().getCenter().y);
//		p.translate(x, y);
//		
		app.getPiecesView().refresh();
		app.getPuzzleView().refresh();

		return true;
	}
}
