/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.docarchive.docarchiveapi;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
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
    
    public apiREST() {
        log.info("Constructor");
    }
    
    @Path("/test")
    @GET
    @RolesAllowed("doc-archive-user")
    public String test() {
        return "test";
    }
    
    @Path("/addProject")
    @GET
    @RolesAllowed("doc-archive-user")
    public String addProject(TProject project) {
        return "addProject";
    }
    
}
