package dev.strongtino.soteria.license;

import dev.strongtino.soteria.Soteria;

public class LicenseThread extends Thread {

    @Override
    public void run() {
        while (true) {
            Soteria.INSTANCE.getLicenseService().getLicenses()
                    .stream()
                    .filter(license -> license.isExpired() && license.isActive())
                    .forEach(License::revoke);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
