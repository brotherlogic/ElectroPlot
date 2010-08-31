package uk.co.brotherlogic.electro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Model {
	public static void main(String[] args) {
		Model m = new Model();
		PlotPane p = new PlotPane(m);
		JFrame framer = new JFrame();
		framer.add(p);
		framer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framer.setSize(500, 500);
		framer.setVisible(true);
	}

	private Connection locDB;

	private final List<Reading> readings = new LinkedList<Reading>();

	public Model() {
		long sTime = System.currentTimeMillis();
		initModel();
	}

	private List<Reading> getReadingsFromPastHour() {
		initModel();
		List<Reading> tReadings = new LinkedList<Reading>();
		long cTime = System.currentTimeMillis() - 2 * 60 * 60 * 1000;
		for (Reading r : readings)
			if (r.getTime() > cTime)
				tReadings.add(r);
		return tReadings;
	}

	public List<Reading> getReadingsFromPastHour(final ModelListener listener) {

		List<Reading> sReadings = getReadingsFromPastHour();

		// Start a timer task
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				listener.update(getReadingsFromPastHour());
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 1000, 1000);

		return sReadings;
	}

	public void initModel() {
		try {
			
			if (locDB == null)
			{
			Class.forName("org.postgresql.Driver");

			locDB = DriverManager
					.getConnection("jdbc:postgresql://192.168.1.100/leccy?user=leccy");
			}

			String sql = "SELECT dt,watts from leccy";
			PreparedStatement ps = locDB.prepareStatement(sql);
			if (readings.size() > 0) {
				sql = "SELECT dt,watts from leccy WHERE dt > ?";
				ps = locDB.prepareStatement(sql);
				ps.setTimestamp(1, new Timestamp(readings.get(
						readings.size() - 1).getTime().longValue()));
				System.out.println(ps);
			}

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Reading r = new Reading(rs.getTimestamp(1).getTime(), rs
						.getInt(2));
				readings.add(r);
			}
			rs.close();
			long sTime = System.currentTimeMillis();
			Collections.sort(readings);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
		}
	}
}

class Reading implements Comparable<Reading> {
	private double temp;
	private final Double time;
	private final double value;

	public Reading(long t, double v) {
		time = new Double(t);
		value = v;
	}

	@Override
	public int compareTo(Reading o) {
		return time.compareTo(o.time);
	}

	public double getReading() {
		return value;
	}

	public Double getTime() {
		return time;
	}
}
