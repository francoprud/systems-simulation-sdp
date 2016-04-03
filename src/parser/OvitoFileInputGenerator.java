package parser;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import model.Particle;
import model.SimulationData;

/*
 * Generates an input file for Ovito (Visualization Tool)
 */
public class OvitoFileInputGenerator {
	private static final String ENCODING = "UTF-8";
	private static final String BLUE = "0 0 255";
	
	private PrintWriter writer;
	private String filePath;
	
	public OvitoFileInputGenerator(String filePath) {
		this.filePath = filePath;
	}

	public void generateFile(int particlesAmount) throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter(filePath, ENCODING);
		printHeaders(particlesAmount);
	}
	
	public void printSimulationInstance(SimulationData simulationData, int instance) {
		writer.println(instance);
		for (Particle particle: simulationData.getParticles()) {
			writer.println(generateLine(particle));
		}
		printBoundariesParticles(simulationData.getSpaceDimension());
	}
	
	public void endSimulation() {
		writer.close();
	}
	
	private void printBoundariesParticles(int spaceDimension) {
		printBoundaryParticle(0, 0);
		printBoundaryParticle(0, spaceDimension);
		printBoundaryParticle(spaceDimension, 0);
		printBoundaryParticle(spaceDimension, spaceDimension);
	}
	
	private void printBoundaryParticle(int x, int y) {
		writer.println("id" + x + " " + y + " 0 0 " + BLUE + " 0");
	}
	
	private void printHeaders(int particlesAmount) {
		writer.println(particlesAmount + 4);
		writer.println("ID X Y Xv Yv R G B r");
	}
	
	private String generateLine(Particle particle) {
		StringBuilder line = new StringBuilder();
		line.append(particle.getId()).append(" ").append(particle.getX())
				.append(" ").append(particle.getY()).append(" ").append(generateParticleColor(particle))
				.append(" ").append(particle.getRadius());
		return line.toString();
	}
	
	private String generateParticleColor(Particle particle) {
		float hue = calculateHue(particle.getAngle());
		int colorValue = Color.HSBtoRGB(hue, 1.0f, 1.0f);
		Color color = new Color(colorValue, true);
		return color.getRed() + " " + color.getGreen() + " " + color.getBlue();
	}
	
	private float calculateHue(double angle) {
		if (angle < 0) {
			while (angle < 0) {
				angle += 2 * Math.PI;
			}
		} else if (angle > 2 * Math.PI) {
			while (angle > 2 * Math.PI) {
				angle -= 2 * Math.PI;
			}
		}
		return (float) (angle / (2 * Math.PI));
	}
}
