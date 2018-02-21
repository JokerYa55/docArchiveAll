/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtk.docarchive.dao.beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vasiliy.andricov
 */
@Entity
@Table(name = "t_project_doc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TProjectDoc.findAll", query = "SELECT t FROM TProjectDoc t where t.project.id = :project_id")
    , @NamedQuery(name = "TProjectDoc.findById", query = "SELECT t FROM TProjectDoc t WHERE t.id = :id")})
public class TProjectDoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    @Column(columnDefinition = "timestamp without time zone NOT NULL DEFAULT now()")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date_load;
    @Column(unique = true, nullable = false)
    private String file_name;
    @Column(unique = true, nullable = false)
    private String real_file_name;
    @ManyToOne
    private TProject project;
    @ManyToOne
    private TDocType doc_type;

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TProjectDoc)) {
            return false;
        }
        TProjectDoc other = (TProjectDoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rtk.beans.TProjectDoc[ id=" + id + " ]";
    }

    /**
     *
     * @return
     */
    public String getFile_name() {
        return file_name;
    }

    /**
     *
     * @param file_name
     */
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    /**
     *
     * @return
     */
    public String getReal_file_name() {
        return real_file_name;
    }

    /**
     *
     * @param real_file_name
     */
    public void setReal_file_name(String real_file_name) {
        this.real_file_name = real_file_name;
    }

    /**
     *
     * @return
     */
    public TProject getProject() {
        return project;
    }

    /**
     *
     * @param project
     */
    public void setProject(TProject project) {
        this.project = project;
    }

    /**
     *
     * @return
     */
    public Date getDate_load() {
        return date_load;
    }

    /**
     *
     * @param date_load
     */
    public void setDate_load(Date date_load) {
        this.date_load = date_load;
    }

    /**
     *
     * @return
     */
    public TDocType getDoc_type() {
        return doc_type;
    }

    /**
     *
     * @param doc_type
     */
    public void setDoc_type(TDocType doc_type) {
        this.doc_type = doc_type;
    }
    
}
