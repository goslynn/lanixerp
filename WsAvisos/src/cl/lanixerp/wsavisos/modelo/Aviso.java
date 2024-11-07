package cl.lanixerp.wsavisos.modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "aviso")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aviso.listar" , query = "SELECT a FROM Aviso a")
})
public class Aviso implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "target")
    private String target;

    @Column(name = "url")
    private String url;

    @Column(name = "tiempo_ms", columnDefinition = "int default 5000")
    private int tiempo_ms = 5000;

    public Aviso() {}

    public Aviso(Integer id, String target, String url) {
        this();
        this.id = id;
        this.target = target;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getTiempo_ms() {
        return tiempo_ms;
    }

    public void setTiempo_ms(int tiempo_ms) {
        this.tiempo_ms = tiempo_ms;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, target, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Aviso other = (Aviso) obj;
        return Objects.equals(id, other.id) &&
               Objects.equals(target, other.target) &&
               Objects.equals(url, other.url);
    }

    @Override
    public String toString() {
        return "Aviso [id=" + id + ", target=" + target + ", url=" + url + "]";
    }
}
