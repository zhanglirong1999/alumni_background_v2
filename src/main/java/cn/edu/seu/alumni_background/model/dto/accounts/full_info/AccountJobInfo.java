package cn.edu.seu.alumni_background.model.dto.accounts.full_info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountJobInfo {

    private Long jobId;
    private String company;
    private String position;

    @JsonIgnore
    public String getKeyString() {
        return company + ": " + position;
    }

    public int hashCode() {
        return getKeyString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountJobInfo that = (AccountJobInfo) o;
        return this.getKeyString().equals(that.getKeyString());
    }
}