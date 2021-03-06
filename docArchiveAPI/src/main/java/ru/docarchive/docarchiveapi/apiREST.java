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
import rtk.docarchive.dao.beans.TDocType;
import rtk.docarchive.dao.beans.TProduct;
import rtk.docarchive.dao.beans.TProject;
import rtk.docarchive.dao.beans.TProjectDoc;
import rtk.docarchive.dao.beans.TProjectType;

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

    /**
     *
     */
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

    /**
     *
     * @return
     */
    @Path("/test")
    @GET
    @RolesAllowed("doc-archive-user")
    public String test() {
        return "test";
    }

    //TODO : Функции для работы с проектами
    /**
     * Добавить проект
     *
     * @param item
     * @return - объект типа Response
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
     * Обновить проект
     *
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
        return Response.status(Response.Status.OK).build();
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
    public Response getProjectList() {
        log.info("getProjectList");
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

    //TODO : Функции для работы с филиалами
    /**
     * Добавить филиал
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
            em.close();
        } catch (Exception e) {
            log.log(Priority.ERROR, e);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Удаление филиала
     *
     * @param id
     * @return
     */
    @Path("/branch/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response deleteBranch(@PathParam("id") Long id) {
        try {
            log.info(String.format("deleteBranch => %s", id.toString()));
            getEM();
            TBranch item = em.find(TBranch.class, id);
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
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("delete branch id = %s error => %s", id, e1.getMessage())).build();
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
     * Обновить филиал
     *
     * @param id
     * @param item
     * @return
     */
    @Path("/branch/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response updateBranch(@PathParam("id") Long id, TBranch item) {
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
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Получить список филиалов
     *
     * @return
     */
    @Path("/branch")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getBranchList() {
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

    /**
     * Получить информацию по филиалу с id = {id}
     *
     * @param id
     * @return
     */
    @Path("/branch/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getBranchById(@PathParam("id") Long id) {
        log.info("getBranchById id => " + id);
        TBranch res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TBranch.findById");
            q.setParameter("id", id);
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

    //TODO : Функции для работы с типами документов (t_doc_type)
    /**
     * Добавить тип документа
     *
     * @param item
     * @return
     */
    @Path("/doctype")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response addDocType(TDocType item) {
        log.info("addDocType => " + item);
        try {
            getEM();
            em.getTransaction().begin();
            em.merge(item);
            log.info("commit");
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
     * Удаление тип документа
     *
     * @param id
     * @return
     */
    @Path("/doctype/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response deleteDocType(@PathParam("id") Long id) {
        try {
            log.info(String.format("deleteDocType => %s", id.toString()));
            getEM();
            TDocType item = em.find(TDocType.class, id);
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
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("delete doc_type id = %s error => %s", id, e1.getMessage())).build();
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
     * Обновить тип документа
     *
     * @param id
     * @param item
     * @return
     */
    @Path("/doctype/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response updateDocType(@PathParam("id") Long id, TDocType item) {
        log.info(String.format("updateDocType => %s", id.toString()));
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
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Получить список типов документов
     *
     * @return
     */
    @Path("/doctype")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getDocTypeList() {
        log.info("getDocType");
        List<TDocType> res = null;
        try {
            getEM();
            em.getTransaction().begin();
            res = em.createNamedQuery("TDocType.findAll").getResultList();
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
     * Получить информацию по типу документа с id = {id}
     *
     * @param id
     * @return
     */
    @Path("/doctype/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getDocTypeById(@PathParam("id") Long id) {
        log.info("getDocTypeById id => " + id);
        TDocType res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TDocType.findById");
            q.setParameter("id", id);
            res = (TDocType) q.getSingleResult();
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

    //TODO : Функции для работы с продуктами (t_product)
    /**
     * Добавить продукт
     *
     * @param item
     * @return
     */
    @Path("/product")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response addProduct(TProduct item) {
        log.info("addProduct => " + item);
        try {
            getEM();
            em.getTransaction().begin();
            em.merge(item);
            log.info("commit");
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
     * Удаление продукта
     *
     * @param id
     * @return
     */
    @Path("/product/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            log.info(String.format("deleteProduct => %s", id.toString()));
            getEM();
            TProduct item = em.find(TProduct.class, id);
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
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("delete product id = %s error => %s", id, e1.getMessage())).build();
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
     * Обновить тип документа
     *
     * @param id
     * @param item
     * @return
     */
    @Path("/product/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response updateProduct(@PathParam("id") Long id, TProduct item) {
        log.info(String.format("updateProduct => %s", id.toString()));
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
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Получить список продуктов
     *
     * @return
     */
    @Path("/product")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProductList() {
        log.info("getProductList");
        List<TProduct> res = null;
        try {
            getEM();
            em.getTransaction().begin();
            res = em.createNamedQuery("TProduct.findAll").getResultList();
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
     * Получить информацию по типу документа с id = {id}
     *
     * @param id
     * @return
     */
    @Path("/product/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProductById(@PathParam("id") Long id) {
        log.info("getProductById id => " + id);
        TProduct res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TProduct.findById");
            q.setParameter("id", id);
            res = (TProduct) q.getSingleResult();
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

    //TODO : Функции для работы с документами проекта (t_project_doc)
    /**
     * Добавить документ
     *
     * @param item
     * @return
     */
    @Path("/projectdoc")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response addProjectDoc(TProjectDoc item) {
        log.info("addProjectDoc => " + item);
        try {
            getEM();
            em.getTransaction().begin();
            em.merge(item);
            log.info("commit");
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
     * Удаление документа
     *
     * @param id
     * @return
     */
    @Path("/projectdoc/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response deleteProjectDoc(@PathParam("id") Long id) {
        try {
            log.info(String.format("deleteProjectDoc => %s", id.toString()));
            getEM();
            TProjectDoc item = em.find(TProjectDoc.class, id);
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
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("delete project_doc id = %s error => %s", id, e1.getMessage())).build();
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
     * Обновить документ
     *
     * @param id
     * @param item
     * @return
     */
    @Path("/projectdoc/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response updateProjectDoc(@PathParam("id") Long id, TProjectDoc item) {
        log.info(String.format("updateProjectDoc => %s", id.toString()));
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
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Получить список документов по проекту
     *
     * @param project_id
     * @return
     */
    @Path("/projectdoc/{project_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProjectDocList(@PathParam("project_id") Long project_id) {
        log.info(String.format("getProjectDocList id = > %s", project_id));
        List<TProjectDoc> res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TProjectDoc.findAll");
            q.setParameter("project_id", project_id);
            res = q.getResultList();
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
     * Получить информацию по типу документа с id = {id}
     *
     * @param id
     * @return
     */
    @Path("/product/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProjectDocById(@PathParam("id") Long id) {
        log.info(String.format("getProjectDocById id => %s", id));
        TProjectDoc res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TProjectDoc.findById");
            q.setParameter("id", id);
            res = (TProjectDoc) q.getSingleResult();
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

    //TODO : Функции для работы с типами проектов (t_project_type)
    /**
     * Добавить тип проекта
     *
     * @param item - тип проекта
     * @return - объект Response
     */
    @Path("/projecttype")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response addProjectType(TProjectType item) {
        log.info("addProjectType => " + item);
        try {
            getEM();
            em.getTransaction().begin();
            em.merge(item);
            log.info("commit");
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
     * Удаление типа проекта
     *
     * @param id
     * @return
     */
    @Path("/projecttype/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response deleteProjectType(@PathParam("id") Long id) {
        try {
            log.info(String.format("deleteProjectType => %s", id.toString()));
            getEM();
            TProjectType item = em.find(TProjectType.class, id);
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
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(String.format("delete project_type id = %s error => %s", id, e1.getMessage())).build();
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
     * Обновить тип проекта
     *
     * @param id
     * @param item
     * @return
     */
    @Path("/projecttype/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response updateProjectType(@PathParam("id") Long id, TProjectType item) {
        log.info(String.format("updateProjectType => %s", id.toString()));
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
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Получить список документов по проекту
     *
     * @param project_id
     * @return
     */
    @Path("/projecttype")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProjectTypeList() {
        log.info(String.format("getProjectTypeList"));
        List<TProjectType> res = null;
        try {
            getEM();
            em.getTransaction().begin();
            res = em.createNamedQuery("TProjectType.findAll").getResultList();
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
     * Получить информацию по типу документа с id = {id}
     *
     * @param id
     * @return
     */
    @Path("/projecttype/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("doc-archive-user")
    public Response getProjectTypeById(@PathParam("id") Long id) {
        log.info(String.format("getProjectTypeById id => %s", id));
        TProjectType res = null;
        try {
            getEM();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("TProjectType.findById");
            q.setParameter("id", id);
            res = (TProjectType) q.getSingleResult();
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
