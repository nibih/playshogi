package com.playshogi.website.gwt.client.widget.openings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.playshogi.website.gwt.client.events.PositionStatisticsEvent;
import com.playshogi.website.gwt.client.mvp.AppPlaceHistoryMapper;
import com.playshogi.website.gwt.client.place.OpeningsPlace;
import com.playshogi.website.gwt.shared.models.PositionDetails;
import com.playshogi.website.gwt.shared.models.PositionMoveDetails;

public class PositionStatisticsPanel extends Composite implements ClickHandler {
	interface MyEventBinder extends EventBinder<PositionStatisticsPanel> {
	}

	private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	private EventBus eventBus;

	private PositionDetails positionDetails;

	private final FlowPanel verticalPanel;

	private final AppPlaceHistoryMapper historyMapper;

	public PositionStatisticsPanel(final AppPlaceHistoryMapper historyMapper) {
		this.historyMapper = historyMapper;
		verticalPanel = new FlowPanel();

		verticalPanel.add(new HTML(SafeHtmlUtils.fromSafeConstant("<br>")));

		verticalPanel.getElement().getStyle().setBackgroundColor("#DBCBCB");

		initWidget(verticalPanel);
	}

	public void activate(final EventBus eventBus) {
		GWT.log("Activating position statistics panel");
		this.eventBus = eventBus;
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@Override
	public void onClick(final ClickEvent event) {
		Object source = event.getSource();
		if (source == verticalPanel) {

		}
	}

	@EventHandler
	public void onGameInformationChangedEvent(final PositionStatisticsEvent event) {
		GWT.log("Position statistics: handle PositionStatisticsEvent");
		positionDetails = event.getPositionDetails();
		refreshInformation();
	}

	private void refreshInformation() {
		GWT.log("Displaying position details: " + positionDetails);
		verticalPanel.clear();

		if (positionDetails != null) {

			int senteRate = (positionDetails.getSente_wins() * 100) / positionDetails.getTotal();
			String winRate = "Sente win rate: " + senteRate;
			verticalPanel.add(new HTML(SafeHtmlUtils.fromTrustedString(winRate)));

			verticalPanel.add(new HTML(SafeHtmlUtils.fromSafeConstant("<br>")));

			PositionMoveDetails[] positionMoveDetails = positionDetails.getPositionMoveDetails();

			Grid grid = new Grid(positionMoveDetails.length + 1, 3);

			grid.setHTML(0, 0, SafeHtmlUtils.fromSafeConstant("Move"));
			grid.setHTML(0, 1, SafeHtmlUtils.fromSafeConstant("#"));
			grid.setHTML(0, 2, SafeHtmlUtils.fromSafeConstant("Win rate"));

			for (int i = 0; i < positionMoveDetails.length; i++) {
				PositionMoveDetails moveDetails = positionMoveDetails[i];

				Hyperlink hyperlink = new Hyperlink(moveDetails.getMove(), historyMapper.getToken(new OpeningsPlace(moveDetails.getNewSfen())));

				grid.setWidget(i + 1, 0, hyperlink);

				// grid.setHTML(i + 1, 0, moveDetails.getMove());
				grid.setHTML(i + 1, 1, String.valueOf(moveDetails.getTotal()));

				int senteMoveRate = (moveDetails.getSente_wins() * 1000) / moveDetails.getTotal();
				int goteMoveRate = (moveDetails.getGote_wins() * 1000) / moveDetails.getTotal();

				String bar = "<table bgcolor=\"#666666\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" class=\"percent\"><tr><td>"
						+ "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr height=\"13\">"
						+ "<td align=\"center\" background=\"green.gif\" width=\"84\">  " + (senteMoveRate / 10.) + " </td>"
						+ "<td align=\"center\" background=\"gray.gif\" width=\"66\">   </td>" + "<td align=\"center\" background=\"red.gif\" width=\"70\">  "
						+ (goteMoveRate / 10.) + " </td>" + "</tr></table>" + "</td></tr></table>";

				grid.setHTML(i + 1, 2, bar);
				// grid.setHTML(i + 1, 2, String.valueOf(moveRate / 10.));
			}

			verticalPanel.add(grid);
		}
	}

}