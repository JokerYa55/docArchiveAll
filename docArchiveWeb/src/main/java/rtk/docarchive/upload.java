/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtk.docarchive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jboss.logging.Logger;
import rtk.docarchive.dao.beans.TBranch;
import rtk.docarchive.dao.beans.TDocType;
import rtk.docarchive.dao.beans.TProduct;
import rtk.docarchive.dao.beans.TProject;
import rtk.docarchive.dao.beans.TProjectDoc;

/**
 *
 * @author vasiliy.andricov
 */
@WebServlet(name = "upload", urlPatterns = {"/upload"})
public class upload extends HttpServlet {

    private Random random = new Random();
    private Logger log = Logger.getLogger(getClass().getName());
    private String project_id;
    private String description;
    private String branch_id;
    private String product_id;
    private String doctype_id;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Resource
    private UserTransaction userTransaction;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        log.info("processRequest");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet upload</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet upload at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }

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

        log.info("doPost");
        log.info("charSet => " + response.getCharacterEncoding());
        //проверяем является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            System.out.println("no Multipart");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Создаём класс фабрику 
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Максимальный буфера данных в байтах,
        // при его привышении данные начнут записываться на диск во временную директорию
        // устанавливаем один мегабайт
        factory.setSizeThreshold(1024 * 1024 * 30);

        // устанавливаем временную директорию
        File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);

        //Создаём сам загрузчик
        ServletFileUpload upload = new ServletFileUpload(factory);

        //максимальный размер данных который разрешено загружать в байтах
        //по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт. 
        //upload.setSizeMax(1024 * 1024 * 10);
        upload.setSizeMax(-1);

        try {

            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();

            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    //если принимаемая часть данных является полем формы			    	
                    processFormField(item);
                } else {
                    //в противном случае рассматриваем как файл
                    String filename = processUploadedFile(item, request, response);
                    if (filename != null) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        
        //RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
        log.info("path => " + request.getContextPath());
        response.sendRedirect(request.getContextPath()+"/index.html");
    }

    private String processUploadedFile(FileItem item, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("processUploadedFile");
        File uploadetFile = null;
        String path = "/var/upload";
        String product_name = null;
        String branch_name = null;
        String doctype_name = null;
        try {
            Map<Long, TBranch> branchMap = (Map) getServletContext().getAttribute("branchHash");
            log.info("branchMap => " + branchMap);
            Map<Long, TProduct> productMap = (Map) getServletContext().getAttribute("productHash");
            log.info("productMap => " + productMap);
            Map<Long, TDocType> docTypeMap = (Map) getServletContext().getAttribute("doctypeHash");
            log.info("docTypeMap => " + docTypeMap);
            log.info("doctype_id = " + doctype_id);
            branch_name = branchMap.get(new Long(branch_id)).getName_branch();
            log.info(String.format("branch_name = %s", branch_name));
            doctype_name = docTypeMap.get(new Long(doctype_id)).getName_type();
            log.info(String.format("doctype_name = %s", doctype_name));
            product_name = productMap.get(new Long(product_id)).getName_product();
            log.info(String.format("product_name = %s", product_name));
        } catch (Exception ex1) {
            log.log(Logger.Level.ERROR, ex1);
        }

        path = String.format("%s/%s/%s/%s/", path, branch_name, product_name, doctype_name);
        log.info("path = " + path);
        File folder = new File(path);
        if (!folder.exists()) {
            log.info("Folder not exist");
            if (folder.mkdirs()) {
                log.info("Folder created");
            } else {
                log.info("Folder not created");
            }
        }
        //выбираем файлу имя пока не найдём свободное
        do {
            path = String.format("%s%s_%s", path, Math.abs(random.nextInt()), item.getName());
            log.info("path file = " + path);
            uploadetFile = new File(path);
        } while (uploadetFile.exists());

        //создаём файл
        uploadetFile.createNewFile();
        //записываем в него данные
        item.write(uploadetFile);

        try {

            // Получаем project
            EntityManager em = entityManagerFactory.createEntityManager();
            TProject proj = em.find(TProject.class, new Long(project_id));
            // Создаем запись для файла

            String f_name = String.format("%s_%s_%s_%s", branch_name, doctype_name, product_name, (new String(description.getBytes(response.getCharacterEncoding()), "UTF-8")).replaceAll(" ", "_"));
            TProjectDoc file = new TProjectDoc();
            file.setFile_name(f_name);
            file.setProject(proj);
            file.setReal_file_name(path);
            file.setDate_load(new Date());
            TDocType docType = new TDocType();
            docType.setId(new Long(doctype_id));
            file.setDoc_type(docType);
            userTransaction.begin();
            em.merge(file);
            userTransaction.commit();
        } catch (Exception e) {
        }

        return path;
    }

    /**
     * Выводит на консоль имя параметра и значение
     *
     * @param item
     */
    private void processFormField(FileItem item) {
        log.info("processFormField");
        log.info(item.getFieldName() + "=" + item.getString());
        if (item.getFieldName().contains("project_id")) {
            this.project_id = item.getString();
        }
        switch (item.getFieldName()) {
            case "project_id":
                this.project_id = item.getString();
                break;
            case "description":
                this.description = item.getString();
                break;
            case "branch_id":
                this.branch_id = item.getString();
                break;
            case "product_id":
                this.product_id = item.getString();
                break;
            case "doc_type_id":
                this.doctype_id = item.getString();
                break;
        }

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
