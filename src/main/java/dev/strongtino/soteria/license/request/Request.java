package dev.strongtino.soteria.license.request;

import dev.strongtino.soteria.license.LicenseController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Request {

    private final int index;
    private final String address;
    private final String key;
    private final String software;
    private final long requestedAt;
    private final LicenseController.ValidationType validationType;
}
