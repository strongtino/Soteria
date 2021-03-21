package dev.strongtino.soteria.service.license.rest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LicenseRequestResponse {

    private final ValidationType validationType;
    private String user, product;
}
