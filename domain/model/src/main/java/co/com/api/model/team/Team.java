package co.com.api.model.team;
import co.com.api.model.cyclist.Cyclist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    private String teamId;
    private String teamName;
    private String teamCode;
    private String country;
    private List<Cyclist> cyclists;
}
