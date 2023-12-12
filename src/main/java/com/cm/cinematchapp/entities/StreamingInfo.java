package com.cm.cinematchapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.List;
/**
 * The `StreamingInfoEntity` class defines an Entity that is used to store information about part of the results
 * provided from the API into the database.
 *
 * @author Mateus Souza
 */
@Embeddable
@Data
public class StreamingInfo {
    @ElementCollection
    @CollectionTable(name = "country_info", joinColumns = @JoinColumn(name = "streaming_info_id"))
    private List<CountryInfo> ca;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Embeddable
    @Data
    public static class CountryInfo {
        private String service;
        private String streamingType;
        private String link;
    }
}
