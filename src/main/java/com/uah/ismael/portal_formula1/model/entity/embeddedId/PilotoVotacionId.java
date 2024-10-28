package com.uah.ismael.portal_formula1.model.entity.embeddedId;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PilotoVotacionId  implements Serializable {
    private Integer votacionID;
    private Integer pilotoID;

    public Integer getVotacionID() {
        return votacionID;
    }

    public void setVotacionID(Integer votacionID) {
        this.votacionID = votacionID;
    }

    public Integer getPilotoID() {
        return pilotoID;
    }

    public void setPilotoID(Integer pilotoID) {
        this.pilotoID = pilotoID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PilotoVotacionId that = (PilotoVotacionId) o;
        return Objects.equals(votacionID, that.votacionID) && Objects.equals(pilotoID, that.pilotoID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(votacionID, pilotoID);
    }
}