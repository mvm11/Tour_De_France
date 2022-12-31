package co.com.api.mongo.cyclist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document(collection = "cyclist")
@NoArgsConstructor
@AllArgsConstructor
public class CyclistDocument {

    @Id
    private String cyclistId;
    private String cyclistName;
    private String cyclistNumber;
    private String teamCode;
    private String nationality;


}
