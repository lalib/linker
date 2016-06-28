package com.bilalalp.common.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@Access(AccessType.FIELD)
public abstract class AbstractEntity implements Serializable{

    @Version
    @Column(name = "C_VERSION", nullable = false)
    private Integer version;

    @Column(name = "C_DATE_CREATED", insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "C_DATE_UPDATED", insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    public abstract Long getId();
}