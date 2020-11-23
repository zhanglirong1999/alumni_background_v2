package cn.edu.seu.alumni_background.model.dto.verify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneUsernameVerifier {

    protected String phone;
    protected String username;
}
