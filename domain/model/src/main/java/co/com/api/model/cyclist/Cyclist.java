package co.com.api.model.cyclist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cyclist {

    private String cyclistId;
    private String cyclistName;
    private String cyclistNumber;
    private String teamCode;
    private String nationality;
}
