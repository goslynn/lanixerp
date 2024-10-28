package cl.lanixerp.pca.modelo;

import java.util.Objects;


public class Aviso {
	
	Integer id;
	String target;
	String url;
	Integer tiempo_ms;
	
	public Aviso() {
		tiempo_ms=5000;
	}

	public Aviso(Integer id, String target, String url) {
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
		return "Aviso " + id+ " ["+tiempo_ms+"ms]";
	}

	public Integer getTiempo_ms() {
		return tiempo_ms;
	}

	public void setTiempo_ms(Integer tiempo) {
		this.tiempo_ms = tiempo;
	}
}
