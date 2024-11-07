package cl.lanixerp.wsavisos.modelo;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Aviso implements Serializable{

	private static final long serialVersionUID = 3841958088099693459L;
	Integer id;
	String target;
	String url;
	Integer tiempo_ms;
	
	public Aviso() {
		tiempo_ms=15000;
	}

	public Aviso(Integer id, String target, String url) {
		this();
		this.id = id;
		this.target = target;
		this.url = url;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, target, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aviso other = (Aviso) obj;
		return Objects.equals(id, other.id) && Objects.equals(target, other.target) && Objects.equals(url, other.url);
	}

	@Override
	public String toString() {
		return "Aviso [id=" + id + ", target=" + target + ", url=" + url + "]";
	}

	public Integer getTiempo_ms() {
		return tiempo_ms;
	}

	public void setTiempo_ms(Integer tiempo) {
		this.tiempo_ms = tiempo;
	}
	
}
