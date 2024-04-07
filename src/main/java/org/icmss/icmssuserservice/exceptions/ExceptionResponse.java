package org.icmss.icmssuserservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private HttpStatus httpStatus;

    private String message;

    private String details;

}
