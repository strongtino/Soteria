package dev.strongtino.soteria.license;

import dev.strongtino.soteria.Soteria;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LicenseController {

    @GetMapping("/license")
    public LicenseRequestResponse licenseRequestResponse(
            HttpServletRequest request,
            @RequestParam(value = "key", defaultValue = "Unknown") String key,
            @RequestParam(value = "software", defaultValue = "Unknown") String software) {

        License license = Soteria.INSTANCE.getLicenseService().getLicenseByKeyAndSoftware(key, software);
        ValidationType type = license == null ? ValidationType.INVALID : ValidationType.VALID;

        Soteria.INSTANCE.getRequestService().insertRequest(request.getRemoteAddr(), key, software, type);

        if (license == null) {
            return new LicenseRequestResponse(type);
        }
        LicenseRequestResponse response = new LicenseRequestResponse(type);

        response.setUser(license.getUser());
        response.setProduct(license.getSoftware());

        return response;
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    public static class LicenseRequestResponse {

        private final ValidationType validationType;

        private String user;
        private String product;
    }

    public enum ValidationType {
        VALID,
        INVALID,
    }
}
