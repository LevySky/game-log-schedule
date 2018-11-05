package com.spc.mysql.utils;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class ResultSetSize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Column(name = "size")
    private Long size;

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
