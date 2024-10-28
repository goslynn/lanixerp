package cl.lanixerp.wsavisos.modelo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaAvisos implements Serializable{

	private Integer cantidad;
	private List<Aviso> lista = new LinkedList<Aviso>();

	public List<Aviso> getLista() {
		return lista;
	}

	public void setLista(List<Aviso> lista) {
		this.lista = lista;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
}
