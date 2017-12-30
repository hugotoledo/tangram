package project.view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;

import project.controller.AddPieceController;
import project.model.Model;
import project.model.PlacedPiece;


// Native construction using WindowBuilder

public class TangramApplication extends JFrame {

	/** Content for this Frame. */
	JPanel contentPane;
	
	/** Represents model for application domain. */
	Model model;
	
	/** View for Tangram pieces. */
	PiecesView piecesView;
	
	/** View for the solution. */
	PuzzleView puzzleView;
	
	public PiecesView getPiecesView() { return piecesView; }
	public PuzzleView getPuzzleView() { return puzzleView; }
	
	/**
	 * Create the frame.
	 */
	public TangramApplication(Model m) {
		this.model = m;
		
		setTitle("Tangram Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 770, 479);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnTangram = new JMenu("Tangram");
		menuBar.add(mnTangram);
		
		JMenuItem mntmLoadSet = new JMenuItem("Load Set...");
		mntmLoadSet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnTangram.add(mntmLoadSet);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnTangram.add(mntmQuit);
		
		JMenu mnPuzzle = new JMenu("Puzzle");
		menuBar.add(mnPuzzle);
		
		JMenuItem mntmLoadPuzzle = new JMenuItem("Load Puzzle...");
		mntmLoadPuzzle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnPuzzle.add(mntmLoadPuzzle);
		
		JMenuItem mntmCreatePuzzle = new JMenuItem("Make Puzzle...");
		mntmCreatePuzzle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		mnPuzzle.add(mntmCreatePuzzle);
		
		JMenu mnAbout = new JMenu("Help");
		menuBar.add(mnAbout);
		
		JMenuItem mntmAboutTangram = new JMenuItem("About Tangram");
		mnAbout.add(mntmAboutTangram);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		JScrollPane tangramSetPane = new JScrollPane();

		// Install PiecesView in the left scrolling panel.
		piecesView = new PiecesView(model);
		tangramSetPane.setViewportView(piecesView);

		// For each mouse event, locate actual piece in PiecesView and have it added to puzzle.
		piecesView.addMouseListener(new MouseAdapter() { 
			@Override
			public void mousePressed(MouseEvent me) {
				Optional<PlacedPiece> found = piecesView.find(me.getPoint());
				if (found.isPresent()) {
					new AddPieceController(TangramApplication.this, model).add(found.get().getPiece());
				}
			}
		});
		
		JScrollPane solutionSetPane = new JScrollPane();
		
		// install PuzzleView in the right scrolling panel.
		puzzleView = new PuzzleView(model);
		solutionSetPane.setViewportView(puzzleView);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tangramSetPane, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(solutionSetPane, GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(solutionSetPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
						.addComponent(tangramSetPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	// Only here for WindowBuilder
	TangramApplication() {
		this (Model.defaultModel());
	}

	
}
