package vn.com.atomi.loyalty.eventgateway.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.eventgateway.dto.output.EGCBiometricOutput;
import vn.com.atomi.loyalty.eventgateway.entity.EGCompleteBiometric;
import vn.com.atomi.loyalty.eventgateway.repository.EGComBiometricRepository;
import vn.com.atomi.loyalty.eventgateway.service.EGComBiometricService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EGComBiometricServiceImpl extends BaseService implements EGComBiometricService {


    private final EGComBiometricRepository egComBiometricRepository;

    @Override
    public List<EGCBiometricOutput> getListEGComBiometrics()
    {
       List<EGCompleteBiometric> lstEGCBiometrics = egComBiometricRepository.findByDeletedFalseAndIsPlusPointFalse();

        var EGCBiometricsOutput = super.modelMapper.convertToEGCBiometricOutput(lstEGCBiometrics);
        return EGCBiometricsOutput;
    }

    public void updateEGComBioMetric(String cifBank)
    {
        EGCompleteBiometric egCompleteBiometric = egComBiometricRepository.findByCifBank(cifBank);

        if(egCompleteBiometric != null)
        {
            egCompleteBiometric.setIsPlusPoint(true);
            egComBiometricRepository.save(egCompleteBiometric);
        }
    }
}
