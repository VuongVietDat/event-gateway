package vn.com.atomi.loyalty.eventgateway.service;

import vn.com.atomi.loyalty.eventgateway.dto.output.EGCBiometricOutput;

import java.util.List;

public interface EGComBiometricService {

    List<EGCBiometricOutput> getListEGComBiometrics();

    void updatePlusPoint(Integer id);
}
