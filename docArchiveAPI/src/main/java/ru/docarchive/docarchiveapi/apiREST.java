/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.docarchive.docarchiveapi;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import rtk.docarchive.dao.beans.TProject;

/**
 *
 * @author vasil
 */
@Path("/")
@Stateless
@PermitAll
public class apiREST {

    Logger log = Logger.getLogger(getClass().getName());
    private static EntityManagerFactory emf;
    private EntityManager em;

    public apiREST() {
        log.info("Constructor");
    }

    private EntityManager getEM() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("docArchiveAPI_JPA");
        }
        this.em = this.emf.createEntityManager();
        return this.em;
    }

    @Path("/test")
    @GET
    @RolesAllowed("doc-archive-user")
    public String test() {
        return "test";
    }

    @Path("/project")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response addProject(TProject project) {
        return Response.status(Response.Status.CREATED).build();
    }

    @Path("/project")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed("doc-archive-user")
    public Response getProject() {
        List<TProject> res = null;
        try {
            getEM();
            em.getTransaction().begin();
            res = em.createQuery("from TProject u").getResultList();
            log.info(String.format("res = %s", res));
            em.getTransaction().commit();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }
        return Response.status(Response.Status.OK).entity(res).build();
    }

}
