package uk.co.brotherlogic.electro;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class PlotPane extends JPanel implements ModelListener {

	private List<Reading> readings;

	public PlotPane(Model m) {
		readings = m.getReadingsFromPastHour(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		double minTime = readings.get(0).getTime();
		double maxTime = minTime;

		double minRead = readings.get(0).getReading();
		double maxRead = minRead;

		for (Reading r : readings.subList(1, readings.size())) {
			minTime = Math.min(minTime, r.getTime());
			maxTime = Math.max(maxTime, r.getTime());

			minRead = Math.min(minRead, r.getReading());
			maxRead = Math.max(maxRead, r.getReading());
		}

		int sPointX = 0;
		int sPointY = 0;

		for (Reading r : readings) {
			int nx = (int) (((r.getTime() - minTime) / (maxTime - minTime)) * getWidth());
			int ny = getHeight()
					- (int) (((r.getReading() - minRead) / (maxRead - minRead)) * getHeight());

			g.drawLine(sPointX, sPointY, nx, ny);

			sPointX = nx;
			sPointY = ny;
		}

	}

	@Override
	public void update(List<Reading> readings) {
		this.readings = readings;
		repaint();
	}
}
