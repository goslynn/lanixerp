package cl.lanixerp.pca;

import java.util.Properties;

import cl.lanixerp.pca.gui.PanelAvisos;

public class PCA {

	private static Properties propiedades;

	public enum Argumento {
		dev("dev=true: Se usara el ambiente dev.lanixerp.cl, sino se usara ws.lanixerp.cl");

		String desc;

		Argumento(String s) {
			this.desc = s;
		}

		public String toString() {
			return name() + " - " + desc;
		}

	}

	private static void capturarArgumentos(String[] args) {

		propiedades = new Properties();
		for (String s : args) {
			s = s.toLowerCase();
			if (s.contains("=")) {
				String sp[] = s.split("=");
				String k = sp[0];
				try {
					Argumento a = Argumento.valueOf(k);
					String v = sp[1];
					propiedades.put(a.name(), v);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}

	}

	public static Boolean is(Argumento a) {
		String v = get(a);
		return "true".equals(v == null ? "" : v);
	}

	public static String get(Argumento a) {
		return propiedades.getProperty(a.name());
	}

	public static void main(String[] args) {

		capturarArgumentos(args);

		PanelAvisos gui = new PanelAvisos();
		gui.setVisible(true);

	}

}
