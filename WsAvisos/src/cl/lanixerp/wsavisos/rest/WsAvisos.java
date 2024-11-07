package cl.lanixerp.wsavisos.rest;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import cl.lanix.librerias.util.LxThreadFactory;
import cl.lanixerp.wsavisos.modelo.Aviso;
import cl.lanixerp.wsavisos.modelo.ControlAvisos;
import cl.lanixerp.wsavisos.modelo.ListaAvisos;


@Path(value = "avisos")
public class WsAvisos {
        
    public static boolean existsAvisos;
    public Logger logger = Logger.getLogger("WsAvisos");
    private static final ExecutorService executorService = java.util.concurrent.Executors
                    .newCachedThreadPool(new LxThreadFactory("WsAvisos", WsAvisos.class));

    @EJB
    private ControlAvisos ctrlAvisos;


    @GET
    @Path("listar")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void getListarAvisos(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo uriInfo) {
            executorService.submit(() -> {
                    asyncResponse.resume(doGetListarAvisos());
            });
    }




    private Response doGetListarAvisos() {
        List<Aviso> avisosdb = ctrlAvisos.getListaAvisos();
        ListaAvisos l = new ListaAvisos();
        if (avisosdb.size() > 0) {
                l.setLista(avisosdb);
        }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document domDocument;
            try {
                    domDocument = dbf.newDocumentBuilder().newDocument();
                    JAXBContext jbc = JAXBContext.newInstance(ListaAvisos.class);
                    Marshaller marshaller = jbc.createMarshaller();
                    marshaller.marshal(l, domDocument);

                    return Response.ok(domDocument).build();
            } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    return Response.ok("" + e.getMessage()).build();
            } catch (JAXBException e) {
                    e.printStackTrace();
                    return Response.ok("" + e.getMessage()).build();
            }



    }

    @POST
    @Path("registrar")
    public void putAviso(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo uriInfo, Aviso aviso) {
            executorService.submit(() -> {
                    asyncResponse.resume(doPutAviso(aviso));
            });
    }

    private Response doPutAviso(Aviso aviso) {
            logger.info("WsAviso" + aviso.toString());
            ctrlAvisos.add(aviso);
            return Response.status(Response.Status.ACCEPTED).build();
    }

    @DELETE
    @Path(value = "borrar/{id}")
    public void deleteAviso(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo uriInfo,
                    @PathParam(value = "id") final Integer id) {
            executorService.submit(() -> {
                    asyncResponse.resume(doDeleteAviso(id));
            });
    }

    private Response doDeleteAviso(Integer id) {
            ctrlAvisos.delete(id);
            return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("test")
    public Response test() {
        try {
            return Response.ok("Conexion exitosa: " + ctrlAvisos.testListarAvisos() + " avisos encontrados.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de conexión: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("test/delete/{id}")
    public Response testDelete(@PathParam(value = "id") final Integer id) {
        try { //Quitar luego
            ctrlAvisos.delete(id);
            return Response.ok("Eliminando aviso " + id + "valor variable existsAvisos: " + existsAvisos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de conexión: " + e.getMessage() + "valor variable existsAvisos: " + existsAvisos).build();
        }
    }


}
