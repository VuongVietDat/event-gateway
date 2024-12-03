package vn.com.atomi.loyalty.eventgateway.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.eventgateway.dto.output.EGLoginOutput;
import vn.com.atomi.loyalty.eventgateway.entity.EGLogin;
import vn.com.atomi.loyalty.eventgateway.repository.EGLoginRepository;
import vn.com.atomi.loyalty.eventgateway.service.EGLoginService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EGLoginServiceImpl extends BaseService implements EGLoginService {

    private final EGLoginRepository egLoginRepository;

    @Override
    public List<EGLoginOutput> getListEGLogin() {
        List<EGLogin> lstEGLogins = egLoginRepository.findByDeletedFalse();

        var EGLoginOutput = super.modelMapper.convertToEGLoginOutput(lstEGLogins);
        return EGLoginOutput;
    }
}
