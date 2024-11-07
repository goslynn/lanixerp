package cl.lanixerp.wsavisos.modelo;

import cl.lanixerp.wsavisos.rest.WsAvisos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class ControlAvisos {
    
        @PersistenceContext(unitName = "WsAvisos")
        private EntityManager em;
	public Logger logger = Logger.getLogger("WsAvisos");
	private HashMap<Integer, Aviso> avisos;
	private ArrayList<Aviso> av_default;
       


//	public List<Aviso> getAvisos() {
//		List<Aviso> tmp = new ArrayList<Aviso>();
//		if (this.avisos != null) {
//			for (Aviso a : this.avisos.values()) {
//				tmp.add(a);
//			}
//		}
//		return tmp;
//	}

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
                em.persist(a);
	}

	public void delete(Integer id) {
//            EntityTransaction t = em.getTransaction();
            if (WsAvisos.existsAvisos) {;
//                t.begin();
                try {
                    Aviso a = em.find(Aviso.class, id);
                    if (a != null) {
                        logger.info("WSAVISO - Se elimina el aviso: " + a.toString());
                        em.remove(a);
//                        t.commit();
                    } else {
                        logger.warning("Aviso con id " + id + " no encontrado.");
//                        t.rollback(); // No hay ningún aviso que eliminar, hacemos rollback
                    }
                } catch (Exception e) {
                    // En caso de error, hacemos rollback de la transacción
//                    if (t.isActive()) {
//                        t.rollback();
//                    }
                    logger.severe("Error al eliminar el aviso: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        
        public List<Aviso> getListaAvisos(){
            StringBuilder msg = new StringBuilder();
            List<Aviso> avisosList = null;
            try {
                avisosList = em.createNamedQuery("Aviso.listar", Aviso.class).getResultList();
                if (avisosList.isEmpty()) {
                    WsAvisos.existsAvisos = false;
                    msg.append("No se encontraron avisos.");
                    logger.severe(msg.toString());
                }else{
                    WsAvisos.existsAvisos = true;
                    for(Aviso a : avisosList){
                        avisos = new HashMap<Integer, Aviso>();
                        avisos.put(a.getId(), a);
                        msg.append("AVISO: " + a.toString());
                    }
                }
            } catch (Exception e) {
                msg.append("Error: ").append(e.getMessage());
                logger.severe(msg.toString());
                e.printStackTrace();
            }
            logger.info(msg.toString());
            return avisosList;
        
        }
        
        public String testListarAvisos(){
            StringBuilder msg = new StringBuilder();
            try {
                List<Aviso> avisos = em.createNamedQuery("Aviso.listar", Aviso.class).getResultList();
                if (avisos.isEmpty()) {
                    msg.append("No se encontraron avisos.");
                } else {
                    for (Aviso aviso : avisos) {
                        msg.append(aviso.toString()).append("\n"); 
                    }
                }
            } catch (Exception e) {
                msg.append("Error: " + e.toString());
                e.printStackTrace(); 
            }
            return msg.toString();
        }
}
