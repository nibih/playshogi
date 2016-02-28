package com.playshogi.website.gwt.client.board;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.playshogi.library.models.Square;
import com.playshogi.library.shogi.models.Piece;
import com.playshogi.library.shogi.models.position.ShogiPosition;
import com.playshogi.library.shogi.models.shogivariant.ShogiInitialPositionFactory;
import com.playshogi.website.gwt.client.board.Komadai.Point;

public class ShogiBoard implements EntryPoint, ClickHandler {

	private static final int TATAMI_LEFT_MARGIN = 10;
	private static final int TATAMI_TOP_MARGIN = 10;
	private static final int TATAMI_INSIDE_MARGIN = 5;
	private static final int BOARD_LEFT_MARGIN = 11;
	private static final int BOARD_TOP_MARGIN = 11;
	public static final int SQUARE_WIDTH = 43;
	public static final int SQUARE_HEIGHT = 48;

	private ShogiPosition position;
	private boolean pieceSelected = false;
	private Piece selectedPiece = null;
	private int selectionRow = 0;
	private int selectionColumn = 0;
	private Image[][] pieceImages;
	private Image ban;
	private AbsolutePanel absolutePanel;
	private Image grid;
	private int boardLeft;
	private int boardTop;
	private Image goteKomadaiImage;
	private Image senteKomadaiImage;
	private Komadai senteKomadai;
	private Komadai goteKomadai;
	private int senteKomadaiX;
	private int senteKomadaiY;

	@Override
	public void onModuleLoad() {

		BoardBundle boardResources = GWT.create(BoardBundle.class);

		absolutePanel = new AbsolutePanel();
		ban = new Image(boardResources.ban_kaya_a());
		grid = new Image(boardResources.masu_dot());
		Image tatami = new Image(boardResources.bg_tatami());
		goteKomadaiImage = new Image(boardResources.ghand());
		senteKomadaiImage = new Image(boardResources.shand());

		senteKomadai = new Komadai(true);
		goteKomadai = new Komadai(false);

		absolutePanel.setSize(tatami.getWidth() + "px", tatami.getHeight() + "px");
		absolutePanel.add(tatami, 0, 0);
		absolutePanel.add(goteKomadaiImage, TATAMI_LEFT_MARGIN, TATAMI_TOP_MARGIN);

		boardLeft = TATAMI_LEFT_MARGIN + goteKomadaiImage.getWidth() + TATAMI_INSIDE_MARGIN;
		boardTop = TATAMI_TOP_MARGIN;

		absolutePanel.add(ban, boardLeft, boardTop);
		absolutePanel.add(grid, boardLeft, boardTop);

		senteKomadaiX = boardLeft + ban.getWidth() + TATAMI_INSIDE_MARGIN;
		senteKomadaiY = TATAMI_TOP_MARGIN + ban.getHeight() - senteKomadaiImage.getHeight();
		absolutePanel.add(senteKomadaiImage, senteKomadaiX, senteKomadaiY);

		position = new ShogiInitialPositionFactory().createInitialPosition();

		displayPosition();

		grid.addClickHandler(this);
		goteKomadaiImage.addClickHandler(this);
		senteKomadaiImage.addClickHandler(this);

		DecoratorPanel absolutePanelWrapper = new DecoratorPanel();
		absolutePanelWrapper.setWidget(absolutePanel);

		RootPanel.get().add(absolutePanelWrapper);
	}

	private void displayPosition() {
		int rows = position.getShogiBoardState().getHeight();
		int columns = position.getShogiBoardState().getWidth();

		pieceImages = new Image[rows][columns];

		// Put some values in the grid cells.
		for (int row = 0; row < rows; ++row) {
			for (int col = 0; col < columns; ++col) {
				Piece piece = getPiece(row, col);
				if (piece != null) {
					final Image image = new Image(PieceGraphics.getPieceImage(piece));

					final int imageRow = row;
					final int imageCol = col;
					pieceImages[imageRow][imageCol] = image;

					setupPieceClickHandler(image, imageRow, imageCol, piece);
					image.setStyleName("gwt-piece-unselected");

					absolutePanel.add(image, getX(col), getY(row));
				}
			}
		}
	}

	private Piece getPiece(final int row, final int col) {
		return position.getShogiBoardState().getPieceAt(new Square(((8 - col) + 1), row + 1));
	}

	private int getY(final int row) {
		return boardTop + BOARD_TOP_MARGIN + row * SQUARE_HEIGHT;
	}

	private int getX(final int col) {
		return boardLeft + BOARD_LEFT_MARGIN + col * SQUARE_WIDTH;
	}

	private int getColumn(final int x) {
		return (x - BOARD_LEFT_MARGIN) / SQUARE_WIDTH;
	}

	private int getRow(final int y) {
		return (y - BOARD_TOP_MARGIN) / SQUARE_HEIGHT;
	}

	private void setupPieceClickHandler(final Image image, final int imageRow, final int imageCol, final Piece piece) {
		image.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(final MouseDownEvent event) {
				event.preventDefault();
			}
		});

		image.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {

				if (image.getStyleName().equals("gwt-piece-selected")) {
					image.setStyleName("gwt-piece-unselected");
					pieceSelected = false;
				} else {
					if (pieceSelected) {
						unselect();
					}
					selectedPiece = piece;
					selectionRow = imageRow;
					selectionColumn = imageCol;
					pieceSelected = true;
					image.setStyleName("gwt-piece-selected");
				}
			}

		});
	}

	private void unselect() {
		pieceImages[selectionRow][selectionColumn].setStyleName("gwt-piece-unselected");
		pieceSelected = false;
	}

	@Override
	public void onClick(final ClickEvent event) {

		Object source = event.getSource();
		if (source == grid) {
			if (pieceSelected) {
				absolutePanel.add(pieceImages[selectionRow][selectionColumn], getX(getColumn(event.getX())),
						getY(getRow(event.getY())));
				unselect();
			}
		} else if (source == senteKomadaiImage && pieceSelected) {
			Piece piece = selectedPiece;

			Point point = senteKomadai.addPiece(piece);

			absolutePanel.add(pieceImages[selectionRow][selectionColumn], senteKomadaiX + point.x,
					senteKomadaiY + point.y);
			unselect();
		} else if (source == goteKomadaiImage && pieceSelected) {
			Piece piece = selectedPiece;

			Point point = goteKomadai.addPiece(piece);

			absolutePanel.add(pieceImages[selectionRow][selectionColumn], TATAMI_LEFT_MARGIN + point.x,
					TATAMI_TOP_MARGIN + point.y);
			unselect();
		}
	}
}
