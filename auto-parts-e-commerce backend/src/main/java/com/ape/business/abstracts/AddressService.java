package com.ape.business.abstracts;

import com.ape.entity.concrete.AddressEntity;
import com.ape.entity.dto.AddressDTO;
import com.ape.entity.dto.request.AddressRequest;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAuthUserAllAddresses();
    AddressDTO getAuthUserAddressesById(Long id);
    AddressDTO saveAddress(AddressRequest addressRequest);
    AddressDTO updateAuthUserAddresses(Long id, AddressRequest addressRequest);
    void removeAddressById(Long id);
    List<AddressDTO> getAllAddressesByUserId(Long userId);
    AddressDTO adminGetAddressById(Long id);
    AddressDTO adminUpdateAddress(Long id, AddressRequest addressRequest);
    void removeAdminUserAddressById(Long id);
    AddressEntity getAddressById(Long id);
}
