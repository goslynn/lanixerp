package cl.lanixerp.wsavisos.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

@Stateless
public class ControlAvisos {

	public Logger logger = Logger.getLogger("WsAvisos");

	private HashMap<Integer, Aviso> avisos;

	private ArrayList<Aviso> av_default;

	public List<Aviso> getAvisos() {
		List<Aviso> tmp = new ArrayList<Aviso>();
		if (this.avisos != null) {
			for (Aviso a : this.avisos.values()) {
				tmp.add(a);
			}
		}
		return tmp;
	}

	private void cargarDefault() {
		if (av_default == null) {
			this.av_default = new ArrayList<Aviso>();

			{
				Aviso a = new Aviso(-100, "TODOS", "https://test4.lanixerp.cl/avisos/aviso01.php");
				this.av_default.add(a);
			}
			{
				Aviso a = new Aviso(-101, "TODOS", "https://test4.lanixerp.cl/avisos/aviso02.php");

				this.av_default.add(a);
			}
			{
				Aviso a = new Aviso(-102, "TODOS", "https://test4.lanixerp.cl/avisos/aviso03.php");
				this.av_default.add(a);
			}

		}
	}

	public void add(Aviso a) {
		if (avisos == null) {
			avisos = new HashMap<Integer, Aviso>();
		}
		avisos.put(a.getId(), a);
	}

	public void delete(Integer id) {
		if (avisos != null) {
			Aviso a = avisos.get(id);
			if (a != null) {
				logger.info("WSAVISO - Se elimina el aviso " + a.toString());
				avisos.remove(id);
			}
		}
	}

}
