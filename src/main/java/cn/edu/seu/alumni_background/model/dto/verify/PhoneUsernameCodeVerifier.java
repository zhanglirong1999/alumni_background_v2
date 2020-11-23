package cn.edu.seu.alumni_background.model.dto.verify;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhoneUsernameCodeVerifier extends PhoneUsernameVerifier {

    protected String code;
}
