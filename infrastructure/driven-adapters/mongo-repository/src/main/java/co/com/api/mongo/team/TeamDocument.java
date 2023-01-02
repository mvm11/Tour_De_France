package co.com.api.mongo.team;

import co.com.api.model.cyclist.Cyclist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Document(collection = "team")
@NoArgsConstructor
@AllArgsConstructor
public class TeamDocument {

    @Id
    private String id;
    private String teamName;
    private String teamCode;
    private String country;
    private List<Cyclist> cyclists;
}
