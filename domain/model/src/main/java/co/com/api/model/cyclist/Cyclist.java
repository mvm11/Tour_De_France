package co.com.api.model.cyclist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cyclist {

    private String cyclistName;
    private String cyclistNumber;
    private String teamCode;
    private String nationality;

    public String[] cyclistFields(){
        return Stream.of(cyclistName, cyclistNumber, teamCode, nationality).toArray(String[]::new);
    }
}
