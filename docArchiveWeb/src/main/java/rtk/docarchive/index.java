/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtk.docarchive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.jboss.logging.Logger;
import org.keycloak.KeycloakSecurityContext;
import rtk.docarchive.dao.beans.TBranch;
import rtk.docarchive.dao.beans.TDocType;
import rtk.docarchive.dao.beans.TProduct;
import rtk.docarchive.dao.beans.TProject;
import rtk.sso.REST.apiREST;

/**
 *
 * @author vasiliy.andricov
 */
@WebServlet(name = "index", urlPatterns = {"/index"})
@DeclareRoles("doc-archive-user")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"doc-archive-user"}))
public class index extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Resource
    private UserTransaction userTransaction;
    private Logger log = Logger.getLogger(getClass().getName());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/upload_main.jsp");
        List branches = null;
        List products = null;
        List projects = null;
        List doctype = null;
        // Филиалы
        if ((request.getServletContext().getAttribute("branches") != null) && (((List) request.getServletContext().getAttribute("branches")).size() > 0)) {
            branches = (List) request.getServletContext().getAttribute("branches");
        } else {
            EntityManager em = entityManagerFactory.createEntityManager();
            branches = em.createQuery("Select t from TBranch t order by t.name_branch").getResultList();
            request.getServletContext().setAttribute("branches", branches);
        }

        Map<Long, TBranch> branchHash = new HashMap();
        for (Object item : branches) {
            branchHash.put(((TBranch) item).getId(), ((TBranch) item));
        }

        // Продукты
        if ((request.getServletContext().getAttribute("products") != null) && (((List) request.getServletContext().getAttribute("products")).size() > 0)) {
            products = (List) request.getServletContext().getAttribute("products");
        } else {
            EntityManager em = entityManagerFactory.createEntityManager();
            products = em.createQuery("Select t from TProduct t order by t.name_product").getResultList();
            request.getServletContext().setAttribute("products", products);
        }
        Map<Long, TProduct> productHash = new HashMap();
        for (Object item : products) {
            productHash.put(((TProduct) item).getId(), ((TProduct) item));
        }

        // Тип документа
        if ((request.getServletContext().getAttribute("doctype") != null) && (((List) request.getServletContext().getAttribute("doctype")).size() > 0)) {
            doctype = (List) request.getServletContext().getAttribute("doctype");
        } else {
            EntityManager em = entityManagerFactory.createEntityManager();
            doctype = em.createQuery("Select t from TDocType t order by t.name_type").getResultList();
            request.getServletContext().setAttribute("doctype", doctype);
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

        // id пользователя
        KeycloakSecurityContext ksc = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        String token = ksc.getTokenString();

        //log.info("token => " + token);
        String user = request.getUserPrincipal().getName();
        log.info("user => " + user);

        apiREST rest = new apiREST(token);
//        List params = new LinkedList();
//        params.add(new BasicNameValuePair("username", "user_003"));
//        rest.getUsers(params);

        request.setAttribute("doctype", doctype);
        request.getSession().setAttribute("doctypeHash", doctypeHash);
        request.setAttribute("branches", branches);
        request.getSession().setAttribute("branchHash", branchHash);
        //log.info("branchHash => " + branchHash);
        request.setAttribute("products", products);
        request.getSession().setAttribute("productHash", productHash);
        //log.info("productHash => " + productHash);
        request.setAttribute("projects", projects);
        request.getSession().setAttribute("projectHash", projectHash);

        dispatcher.forward(request, response);

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
