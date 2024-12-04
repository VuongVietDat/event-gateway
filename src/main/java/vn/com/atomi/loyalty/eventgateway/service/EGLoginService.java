package vn.com.atomi.loyalty.eventgateway.service;

import vn.com.atomi.loyalty.eventgateway.dto.output.EGCBiometricOutput;
import vn.com.atomi.loyalty.eventgateway.dto.output.EGLoginOutput;

import java.util.List;

public interface EGLoginService {

    List<EGLoginOutput> getListEGLogin();
}
