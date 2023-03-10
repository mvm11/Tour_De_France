package co.com.api.model.team;
import co.com.api.model.cyclist.Cyclist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    private String id;
    private String teamName;
    private String teamCode;
    private String country;
    private List<Cyclist> cyclists;

    public String[] teamFields(){
        return Stream.of(teamName, teamCode, country).toArray(String[]::new);
    }
}
