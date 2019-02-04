import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {
    Integer id;
    String country;
    String illumination;
    String roadType;
    String severity;
    String driverName;
}
