/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtk.filter;

import java.io.*;
import javax.servlet.*;

/**
 *
 * @author vasiliy.andricov
 */
public class CharsetFilter implements Filter {
    // кодировка

    private String encoding;

    /**
     *
     * @param config
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {
        // читаем из конфигурации
        encoding = config.getInitParameter("requestEncoding");

        // если не установлена — устанавливаем UTF-8
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    /**
     *
     * @param request
     * @param response
     * @param next
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request,
            ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        next.doFilter(request, response);
    }

    /**
     *
     */
    public void destroy() {
    }
}

