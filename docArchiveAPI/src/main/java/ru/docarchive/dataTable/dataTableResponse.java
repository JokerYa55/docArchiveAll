/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.docarchive.dataTable;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONArray;

/**
 *
 * @author vasil
 */

@XmlRootElement
public class dataTableResponse {
    int iTotalRecords = 10;
    int iTotalDisplayRecords = 10;
    int sEcho = 10;
    List<JSONArray> aaData;
}
