package cl.lanixerp.pca.gui;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import cl.lanixerp.pca.PCA;
import cl.lanixerp.pca.PCA.Argumento;
import cl.lanixerp.pca.modelo.Aviso;

public class CargadorAvisos {

	protected Client client;
	private final String hostDEV = "https://dev.lanixerp.cl";
	private final String hostPRD = "https://ws.lanixerp.cl";
        private final String hostLCL = "http://localhost:8080/";
	private final String listar = "/WsAvisos/avisos/listar";
	private final String registrar = "/WsAvisos/avisos/registrar";
	private final String borrar = "/WsAvisos/avisos/borrar";
        private final String test = "WsAvisos/avisos/testConnection";
	
	private Gson gson;
	
	private Integer idmax=1;
        
        private WebResource call = null;

	public CargadorAvisos() {
		inicializar();
	}

	private void inicializar() {
		gson = new Gson();
		ClientConfig cc = new DefaultClientConfig();
		client = Client.create(cc);
		client.setReadTimeout(30000);
		client.setConnectTimeout(10000);
	}

	protected ArrayList<Aviso> cargarAvisos() {
		ArrayList<Aviso> str = new ArrayList<Aviso>();

		try {
			setCall();

			String respStr = call.path(listar).accept(MediaType.APPLICATION_XML).get(String.class);

			System.out.println("Cargando Avisos");
			System.out.println("call:" + respStr);
			SAXBuilder saxBuilder = new SAXBuilder();
			try {
				Document document = saxBuilder.build(new StringReader(respStr));
				Element rootElement = document.getRootElement();
				List<Element> listaElements = rootElement.getChildren("lista");
				for (Element listaElement : listaElements) {
					Aviso a = new Aviso();

					String str_id = listaElement.getChildText("id");
					if (str_id != null) {
						a.setId(Integer.parseInt(str_id));
					}
					else {
						a.setId(0);
					}
					a.setTarget(listaElement.getChildText("target"));
					a.setUrl(listaElement.getChildText("url"));
					a.setTiempo_ms(Integer.parseInt(listaElement.getChildText("tiempo_ms")));
					
					if (a.getId()>idmax) {
						idmax=a.getId();
					}
					
					str.add(a);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}

		
		System.out.println("Se cargaron :"+str.size());
		return str;
	}

	public Integer getNuevoID() {
		return idmax+1;
	}
	
	public void enviarAviso(Aviso aviso) {
		try {
			setCall();

			// Convertir el Aviso a JSON usando Gson
			String avisoJson = gson.toJson(aviso);

			// Enviar el JSON al endpoint
			System.out.println("Registrando Aviso: "+aviso.getUrl());
			ClientResponse response = call.path(registrar).type(MediaType.APPLICATION_JSON).post(ClientResponse.class,
					avisoJson);

			// Manejar la respuesta
			if (response.getStatus() == 202) {
				System.out.println("Aviso enviado correctamente.");
			} else {
				System.out.println("Error al enviar aviso: " + response.getStatus());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void borrarAviso(Integer id) {
		try {
			setCall();
                        
			// Enviar el JSON al endpoint
			System.out.println("Borrando Aviso: "+id);
			ClientResponse response = call.path(borrar+"/"+id).type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

			// Manejar la respuesta
			if (response.getStatus() == 200) {
				System.out.println("Aviso Borrado correctamente.");
			} else {
				System.out.println("Error al Borrar aviso: " + response.getStatus());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        
        public void test(){
            try {
                setCall();
                
                // Enviar el JSON al endpoint
                System.out.println("Borrando Aviso: Probando conexion");
                ClientResponse response = call.path(test+"/").type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

                // Manejar la respuesta
                if (response.getStatus() == 200) {
                        System.out.println("Conexion Exitosa");
                        System.out.println(response.toString());
                } else {
                        System.out.println("Error al Borrar aviso: " + response.getStatus());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    public void setCall(){
        call = null;
        
        if (PCA.is(Argumento.dev)) {
                call = client.resource(hostDEV);
        } else if (PCA.is(Argumento.local)) {
                call = client.resource(hostLCL);
        }else {
                call = client.resource(hostPRD);
        }
    }
	
}
