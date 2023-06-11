package com.ape.business.concretes;

import com.ape.business.abstracts.AddressService;
import com.ape.business.abstracts.OrderService;
import com.ape.business.abstracts.UserService;
import com.ape.entity.concrete.AddressEntity;
import com.ape.entity.concrete.UserEntity;
import com.ape.entity.dao.AddressDao;
import com.ape.entity.dao.OrderDao;
import com.ape.entity.dto.AddressDTO;
import com.ape.entity.dto.request.AddressRequest;
import com.ape.exception.BadRequestException;
import com.ape.exception.ErrorMessage;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AddressManager implements AddressService {
    private final AddressDao addressDao;
    private final AddressMapper addressMapper;
    private final UserService userService;
    private final OrderDao orderDao;

    @Override
    public List<AddressDTO> getAuthUserAllAddresses() {
        UserEntity user= userService.getCurrentUser();
        List<AddressEntity> userAddresses = addressDao.findAllByUserId(user.getId());
        return addressMapper.entityListToDTOList(userAddresses);
    }

    @Override
    public AddressDTO getAuthUserAddressesById(Long id) {
        AddressEntity userAddress = addressDao.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
        return addressMapper.entityToDTO(userAddress);
    }

    @Override
    public AddressDTO saveAddress(AddressRequest addressRequest) {
        UserEntity user = userService.getCurrentUser();
        AddressEntity address= addressMapper.addressRequestToAddressEntity(addressRequest);
        address.setUser(user);
        addressDao.save(address);
        return addressMapper.entityToDTO(address);
    }

    @Override
    public AddressDTO updateAuthUserAddresses(Long id, AddressRequest addressRequest) {
        AddressEntity address = getAddressById(id);
        long tempId = address.getId();
        UserEntity user = userService.getCurrentUser();
        if (!user.getAddresses().contains(address)){
            throw new BadRequestException(ErrorMessage.ADDRESS_NOT_FOUND_MESSAGE);
        }
        address = addressMapper.addressRequestToAddressEntity(addressRequest);
        address.setId(tempId);
        address.setUpdateAt(LocalDateTime.now());
        address.setUser(user);
        addressDao.save(address);
        return addressMapper.entityToDTO(address);
    }

    @Override
    public void removeAddressById(Long id) {
    AddressEntity address = getAddressById(id);
    UserEntity user = userService.getCurrentUser();
    if (!user.getAddresses().contains(address)){
        throw new BadRequestException(ErrorMessage.ADDRESS_NOT_FOUND_MESSAGE);
    }
        boolean existInvoiceAddress= orderDao.existsByInvoiceAddress(address);
        boolean existShippingAddress= orderDao
                .existsByShippingAddress(address);
        if (existInvoiceAddress || existShippingAddress){
            throw new BadRequestException(String.format(ErrorMessage.ADDRESS_USED_MESSAGE));
        }
        addressDao.delete(address);
    }

    @Override
    public List<AddressDTO> getAllAddressesByUserId(Long userId) {
        UserEntity user = userService.getUserById(userId);
        List<AddressEntity> addresses = addressDao.findAllByUserId(user.getId());
        return addressMapper.entityListToDTOList(addresses);
    }

    @Override
    public AddressDTO adminGetAddressById(Long id) {
        AddressEntity address = getAddressById(id);
        return addressMapper.entityToDTO(address);
    }

    @Override
    public AddressDTO adminUpdateAddress(Long id, AddressRequest addressRequest) {
        AddressEntity address = getAddressById(id);
        address = addressMapper.addressRequestToAddressEntity(addressRequest);
        addressDao.save(address);
        return addressMapper.entityToDTO(address);
    }

    @Override
    public void removeAdminUserAddressById(Long id) {
        AddressEntity userAddress = getAddressById(id);
        boolean existInvoiceAddress= orderDao.existsByInvoiceAddress(userAddress);
        boolean existShippingAddress= orderDao.existsByShippingAddress(userAddress);
        if (existInvoiceAddress && existShippingAddress) {
           throw new BadRequestException(String.format(ErrorMessage.ADDRESS_USED_MESSAGE));
        }
        addressDao.delete(userAddress);
    }
    public AddressEntity getAddressById(Long id) {
        return addressDao.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
    }
}
