/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtk.docarchive.docarchiveweb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Request;
import rtk.docarchive.dao.beans.TBranch;
import rtk.docarchive.dao.beans.TDocType;
import rtk.docarchive.dao.beans.TProduct;
import rtk.docarchive.dao.beans.TProject;

/**
 *
 * @author vasil
 */
public class startListener implements ServletContextListener {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Resource
    private UserTransaction userTransaction;
    final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("contextInitialized => " + entityManagerFactory);
        List branches = null;
        List products = null;
        List projects = null;
        List doctype = null;
                
        // Филиалы
        if ((sce.getServletContext().getAttribute("branches") != null) && (((List) sce.getServletContext().getAttribute("branches")).size() > 0)) {
            branches = (List) sce.getServletContext().getAttribute("branches");
        } else {
            EntityManager em = entityManagerFactory.createEntityManager();
            branches = em.createQuery("Select t from TBranch t order by t.name_branch").getResultList();
            sce.getServletContext().setAttribute("branches", branches);
        }

        Map<Long, TBranch> branchHash = new HashMap();
        for (Object item : branches) {
            branchHash.put(((TBranch) item).getId(), ((TBranch) item));
        }
        
        // Продукты
        if ((sce.getServletContext().getAttribute("products") != null) && (((List) sce.getServletContext().getAttribute("products")).size() > 0)) {
            products = (List) sce.getServletContext().getAttribute("products");
        } else {
            EntityManager em = entityManagerFactory.createEntityManager();
            products = em.createQuery("Select t from TProduct t order by t.name_product").getResultList();
            sce.getServletContext().setAttribute("products", products);
        }
        Map<Long, TProduct> productHash = new HashMap();
        for (Object item : products) {
            productHash.put(((TProduct) item).getId(), ((TProduct) item));
        }

        // Тип документа
        if ((sce.getServletContext().getAttribute("doctype") != null) && (((List) sce.getServletContext().getAttribute("doctype")).size() > 0)) {
            doctype = (List) sce.getServletContext().getAttribute("doctype");
        } else {
            EntityManager em = entityManagerFactory.createEntityManager();
            doctype = em.createQuery("Select t from TDocType t order by t.name_type").getResultList();
            sce.getServletContext().setAttribute("doctype", doctype);
        }
        Map<Long, TDocType> doctypeHash = new HashMap();
        for (Object item : doctype) {
            doctypeHash.put(((TDocType) item).getId(), ((TDocType) item));
        }

        // Проекты
        EntityManager em = entityManagerFactory.createEntityManager();
        projects = em.createQuery("Select t from TProject t order by t.num_doc").getResultList();

        Map<Long, TProject> projectHash = new HashMap();
        for (Object item : projects) {
            projectHash.put(((TProject) item).getId(), ((TProject) item));
        }

//        // id пользователя
//        KeycloakSecurityContext ksc = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
//        String token = ksc.getTokenString();
//
//        //log.info("token => " + token);
//        String user = request.getUserPrincipal().getName();
//        log.info("user => " + user);
//
//        apiREST rest = new apiREST(token);
//        List params = new LinkedList();
//        params.add(new BasicNameValuePair("username", "user_003"));
//        rest.getUsers(params);

        sce.getServletContext().setAttribute("doctype", doctype);
        sce.getServletContext().setAttribute("doctypeHash", doctypeHash);
        sce.getServletContext().setAttribute("branches", branches);
        sce.getServletContext().setAttribute("branchHash", branchHash);
        //log.info("branchHash => " + branchHash);
        sce.getServletContext().setAttribute("products", products);
        sce.getServletContext().setAttribute("productHash", productHash);
        //log.info("productHash => " + productHash);
        sce.getServletContext().setAttribute("projects", projects);
        sce.getServletContext().setAttribute("projectHash", projectHash);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("contextDestroyed");
    }

}
