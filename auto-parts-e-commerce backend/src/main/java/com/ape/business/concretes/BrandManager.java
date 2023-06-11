package com.ape.business.concretes;

import com.ape.business.abstracts.BrandService;
import com.ape.business.abstracts.ImageService;
import com.ape.business.abstracts.RoleService;
import com.ape.business.abstracts.UserService;
import com.ape.entity.dao.BrandDao;
import com.ape.entity.dao.ImageDao;
import com.ape.entity.dao.ProductDao;
import com.ape.entity.dto.BrandDTO;
import com.ape.entity.dto.request.BrandRequest;
import com.ape.entity.dto.request.BrandUpdateRequest;
import com.ape.entity.concrete.BrandEntity;
import com.ape.entity.concrete.ImageFileEntity;
import com.ape.entity.concrete.RoleEntity;
import com.ape.entity.enums.BrandStatus;
import com.ape.entity.enums.RoleType;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.BrandMapper;
import com.ape.exception.ErrorMessage;
import com.ape.utility.NameFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandManager implements BrandService {

    private final EntityManager entityManager;
    private final RoleService roleService;
    private final UserService userService;
    private final ImageService imageService;
    private final BrandMapper brandMapper;
    private final BrandDao brandDao;
    private final NameFilter nameFilter;
    private final ImageDao imageDao;
    private final ProductDao productDao;

    @Override
    public PageImpl<BrandDTO> getAllBrandsWithFilterAndPage(String query, BrandStatus status, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BrandEntity> criteriaQuery = cb.createQuery(BrandEntity.class);
        Root<BrandEntity> root = criteriaQuery.from(BrandEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        Predicate finalPredicate = null;

        if (query != null && !query.isEmpty()) {
            String likeSearchText = "%" + query.toLowerCase(Locale.US) + "%";
            predicates.add(cb.like(cb.lower(root.get("name")), likeSearchText));
        }

        try{
            RoleEntity role = roleService.findByRoleName(RoleType.ROLE_ADMIN);
            boolean isAdmin = userService.getCurrentUser().getRoles().stream().anyMatch(r->r.equals(role));
            if (isAdmin) {
                if (status != null){
                    predicates.add(cb.equal(root.get("status"),status));
                }
            }else throw  new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_MESSAGE);
        }catch(ResourceNotFoundException e){
            predicates.add(cb.equal(root.get("status"), BrandStatus.PUBLISHED));
        }

        finalPredicate = cb.and(predicates.toArray(new Predicate[0]));

        criteriaQuery.orderBy(pageable.getSort().stream()
                .map(order -> {
                    if (order.isAscending()) {
                        return cb.asc(root.get(order.getProperty()));
                    } else {
                        return cb.desc(root.get(order.getProperty()));
                    }
                })
                .collect(Collectors.toList()));

        criteriaQuery.where(finalPredicate);

        TypedQuery<BrandEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(BrandEntity.class)));
        countQuery.where(finalPredicate);
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        List<BrandDTO> brandDTOList = brandMapper.entityListToDTOList(typedQuery.getResultList());

        return new PageImpl<>(brandDTOList, pageable, totalRecords);
    }

    @Override
    public BrandDTO getBrandById(Long id) {
        BrandEntity brand=null;
        try{
            RoleEntity role = roleService.findByRoleName(RoleType.ROLE_ADMIN);
            boolean isAdmin = userService.getCurrentUser().getRoles().stream().anyMatch(r->r.equals(role));
            if (isAdmin) {
                brand = findBrandById(id);
            }else{
                throw  new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_MESSAGE);
            }
        }catch(ResourceNotFoundException e){
            BrandStatus status=BrandStatus.PUBLISHED;
            brand = brandDao.getBrandByStatusPublishedAndId(status,id).orElseThrow(()->
                    new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_MESSAGE)) ;
        }

        return brandMapper.brandToBrandDTO(brand);
    }

    @Override
    public BrandDTO createBrand(BrandRequest brandRequest) {
        ImageFileEntity imageFile= imageService.getImageById(brandRequest.getImageId());

        Integer usedBrandCount = brandDao.findBrandByImageId(brandRequest.getImageId());
        if(usedBrandCount>0){
            throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
        }

        boolean existBrand = brandDao.existsByName(nameFilter.getNamesWithFilter(brandRequest.getName()));
        if (existBrand) {
            throw new ConflictException(String.format(ErrorMessage.BRAND_CONFLICT_EXCEPTION,nameFilter.getNamesWithFilter(brandRequest.getName())));
        }


        BrandEntity brand = brandMapper.brandRequestToBrand(brandRequest);
        brand.setStatus(BrandStatus.NOT_PUBLISHED);
        brand.setName(nameFilter.getNamesWithFilter(brandRequest.getName()));
        brand.setImage(imageFile);
        brandDao.save(brand);
        return brandMapper.brandToBrandDTO(brand);
    }

    @Override
    public BrandDTO updateBrand(Long id, BrandUpdateRequest brandUpdateRequest) {
        BrandEntity brand=findBrandById(id);
        if (!brand.getImage().getId().equals(brandUpdateRequest.getImage())){
            ImageFileEntity tempImageFile = brand.getImage();
            ImageFileEntity imageFile = imageService.getImageById(brandUpdateRequest.getImage());
            brand.setImage(imageFile);
            imageDao.delete(tempImageFile);
        }

        boolean brandNameExist  = brandDao.existsByName(nameFilter.getNamesWithFilter(brandUpdateRequest.getName()));

        if(brandNameExist && ! brandUpdateRequest.getName().equalsIgnoreCase(brand.getName())) {
            throw new ConflictException(String.format(ErrorMessage.BRAND_CONFLICT_EXCEPTION,nameFilter.getNamesWithFilter(brandUpdateRequest.getName())));}

        boolean imageExists = brandDao.existsByImageId(brandUpdateRequest.getImage());
        if(imageExists && ! brandUpdateRequest.getImage().equalsIgnoreCase(brand.getImage().getId())) {
            throw new ConflictException(String.format(ErrorMessage.IMAGE_ALREADY_EXIST_MESSAGE,brandUpdateRequest.getImage()));}

        brand.setName(nameFilter.getNamesWithFilter(brandUpdateRequest.getName()));
        brand.setStatus(brandUpdateRequest.getStatus());
        brand.setUpdateAt(LocalDateTime.now());
        BrandDTO brandDTO=brandMapper.brandToBrandDTO(brand);
        brandDao.save(brand);

        return brandDTO;
    }

    @Override
    public BrandDTO deleteBrandById(Long id) {
        BrandEntity brand=findBrandById(id);
        Boolean existsProduct = productDao.existsByBrandId(id);

        if(existsProduct){
            throw  new BadRequestException(ErrorMessage.BRAND_CAN_NOT_DELETE_EXCEPTION);
        }
        BrandDTO brandDTO=getBrandById(id);
        brandDao.delete(brand);
        return brandDTO;
    }

    public BrandEntity findBrandById(Long id){
        return brandDao.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.BRAND_NOT_FOUND_MESSAGE,id)));
    }

    @Override
    public List<BrandDTO> getAllBrandList() {
        List<BrandEntity> brandList = brandDao.findAll();
        return brandMapper.entityListToDTOList(brandList);
    }

    @Override
    public long countBrandRecords() {
        return brandDao.count();
    }
}
