package uk.co.brotherlogic.electro;

import java.awt.Color;
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

		double minRead = 30;
		double maxRead = 0;

		for (Reading r : readings.subList(1, readings.size())) {
			minTime = Math.min(minTime, r.getTime());
			maxTime = Math.max(maxTime, r.getTime());

			minRead = Math.min(minRead, r.getReading());
			maxRead = Math.max(maxRead, r.getReading());
		}
		
		Color c = g.getColor();
		g.setColor(Color.red);
		g.drawString("" + maxRead, this.getWidth()-100, 10);
		g.drawString("" + minRead,this.getWidth()-100,this.getHeight()-50);   
		g.setColor(c);

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
