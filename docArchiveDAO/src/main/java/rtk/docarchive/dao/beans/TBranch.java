/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtk.docarchive.dao.beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vasiliy.andricov
 */
@Entity
@Table(name = "t_branch")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TBranch.findAll", query = "SELECT t FROM TBranch t ")
    , @NamedQuery(name = "TBranch.findById", query = "SELECT t FROM TBranch t WHERE t.id = :id")})
public class TBranch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false, unique = true)
    private String name_branch;

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
        if (!(object instanceof TBranch)) {
            return false;
        }
        TBranch other = (TBranch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rtk.beans.t_branch[ id=" + id + " ]";
    }

    public String getName_branch() {
        return name_branch;
    }

    public void setName_branch(String name_branch) {
        this.name_branch = name_branch;
    }

}
