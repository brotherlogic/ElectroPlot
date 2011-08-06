package uk.co.brotherlogic.electro;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class SuperPlot {
	public static void main(String[] args) {
		Model m1 = new Model(0);
		Model m2 = new Model(1);
		Model m3 = new Model(2);
		Model m4 = new Model(3);
		PlotPane p1 = new PlotPane(m1);
		PlotPane p2 = new PlotPane(m2);
		PlotPane p3 = new PlotPane(m3);
		PlotPane p4 = new PlotPane(m4);
		JFrame framer = new JFrame();
		framer.setLayout(new GridLayout(4, 1));
		framer.add(p1);
		framer.add(p2);
		framer.add(p3);
		framer.add(p4);
		framer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framer.setSize(500, 500);
		framer.setVisible(true);
	}
}
