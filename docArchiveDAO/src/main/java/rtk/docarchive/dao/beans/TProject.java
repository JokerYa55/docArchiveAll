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
import javax.persistence.FetchType;
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
@Table(name = "t_project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TProject.findAll", query = "SELECT t FROM TProject t ")
    , @NamedQuery(name = "TProject.findById", query = "SELECT t FROM TProject t WHERE t.id = :id")})
public class TProject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String name_doc;
    @Column(nullable = false, length = 255)
    private String num_doc;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    private Date date_doc;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    private Date begin_date;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date end_date;
    //@Column(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private TBranch branch;
    //@Column(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private TProduct product;
    //@Column(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private TProjectType project_type;

    public Long getId() {
        return id;
    }

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
        if (!(object instanceof TProject)) {
            return false;
        }
        TProject other = (TProject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rtk.beans.t_project[ id=" + id + " ]";
    }

    public String getName_doc() {
        return name_doc;
    }

    public void setName_doc(String name_doc) {
        this.name_doc = name_doc;
    }

    public String getNum_doc() {
        return num_doc;
    }

    public void setNum_doc(String num_doc) {
        this.num_doc = num_doc;
    }

    public Date getDate_doc() {
        return date_doc;
    }

    public void setDate_doc(Date date_doc) {
        this.date_doc = date_doc;
    }

    public Date getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(Date begin_date) {
        this.begin_date = begin_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public TBranch getBranch() {
        return branch;
    }

    public void setBranch(TBranch branch) {
        this.branch = branch;
    }

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public TProjectType getProject_type() {
        return project_type;
    }

    public void setProject_type(TProjectType project_type) {
        this.project_type = project_type;
    }

}
