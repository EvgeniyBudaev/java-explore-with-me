package ru.practicum.ewmservice.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@ToString
@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "lat", column = @Column(name = "location_lat")),
        @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
})
public class Location {

    private Float lat;

    private Float lon;
}
