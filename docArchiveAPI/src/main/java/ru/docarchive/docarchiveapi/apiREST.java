/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.docarchive.docarchiveapi;

import java.util.List;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import rtk.docarchive.dao.beans.TBranch;
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

    /**
     * Функции для работы с проектами
     */
    /**
     * Добавить проект
     *
     * @param item
     * @return
     */
    @Path("/project")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response addProject(TProject item) {
        try {
            getEM();
            em.getTransaction().begin();
            em.merge(item);
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Удаление проекта
     *
     * @param id
     * @return
     */
    @Path("/project/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response deleteProject(@PathParam("id") String id) {
        try {
            getEM();
            TProject item = em.find(TProject.class, new Long(id));
            if (item != null) {
                try {
                    em.getTransaction().begin();
                    em.remove(item);
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().commit();
                    }
                    em.close();
                    return Response.status(Response.Status.OK).build();
                } catch (Exception e1) {
                    em.close();
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("delete project id = %s error => %s", id, e1.getMessage())).build();
                }
            } else {
                em.close();
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Обновить ghjtrn
     * @param id
     * @param item
     * @return
     */
    @Path("/project/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response updateProject(@PathParam("id") Long id, TProject item) {
        log.info(String.format("updateProject => %s", id.toString()));
        try {
            getEM();
            em.getTransaction().begin();
            item.setId(id);
            em.merge(item);
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();

        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Получить список всех проектов
     *
     * @return
     */
    @Path("/project")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProject() {
        log.info("getProject");
        List<TProject> res = null;
        try {
            getEM();
            em.getTransaction().begin();
            res = em.createNamedQuery("TProject.findAll").getResultList();
            log.info(String.format("res = %s", res));
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }

        if (res != null) {
            return Response.status(Response.Status.OK).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Получить информацию по конкретному проекту
     *
     * @param id
     * @return
     */
    @Path("/project/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProjectById(@PathParam("id") String id) {
        log.info("getProjectById id => " + id);
        TProject res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TProject.findById");
            q.setParameter("id", new Long(id));
            res = (TProject) q.getSingleResult();
            log.info(String.format("res = %s", res));
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }
        if (res != null) {
            return Response.status(Response.Status.OK).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Функции для работы с филиалами
     */
    /**
     *
     * @param item
     * @return
     */
    @Path("/branch")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response addBranch(TBranch item) {
        log.info("addBranch => " + item);
        try {
            getEM();
            em.getTransaction().begin();
            em.merge(item);
            log.info("commit");
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            //log.info("flush");
            //em.flush();
            em.close();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     *
     * @return
     */
    @Path("/branch")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getBranch() {
        log.info("getBranch");
        List<TBranch> res = null;
        try {
            getEM();
            em.getTransaction().begin();
            res = em.createNamedQuery("TBranch.findAll").getResultList();
            log.info(String.format("res = %s", res));
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }

        if (res != null) {
            return Response.status(Response.Status.OK).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/branch/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getBranchById(@PathParam("id") String id) {
        log.info("getBranchById id => " + id);
        TBranch res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TBranch.findById");
            q.setParameter("id", new Long(id));
            res = (TBranch) q.getSingleResult();
            log.info(String.format("res = %s", res));
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }
        if (res != null) {
            return Response.status(Response.Status.OK).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
