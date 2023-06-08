package com.ape.business.concretes;

import com.ape.business.abstracts.ProductService;
import com.ape.dto.ProductDTO;
import com.ape.dto.request.ProductRequest;
import com.ape.dto.request.ProductUpdateRequest;
import com.ape.entity.ProductEntity;
import com.ape.entity.RoleEntity;
import com.ape.entity.enums.BrandStatus;
import com.ape.entity.enums.CategoryStatus;
import com.ape.entity.enums.ProductStatus;
import com.ape.entity.enums.RoleType;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.ProductMapper;
import com.ape.exception.ErrorMessage;
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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductManager implements ProductService {

    private final EntityManager entityManager;
    private final CategoryManager categoryManager;
    private final ImageManager imageManager;
    private final ProductMapper productMapper;
    private final UserManager userManager;
    private final RoleManager roleManager;

    @Override
    public PageImpl<ProductDTO> findAllWithQueryAndPage(String query, List<Long> categoryId, List<Long> brandId, Integer minPrice, Integer maxPrice, ProductStatus status, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            String likeSearchText = "%" + query.toLowerCase(Locale.US) + "%";
            Predicate searchByTitle = cb.like(cb.lower(root.get("title")), likeSearchText);
            Predicate searchByShortDesc = cb.like(cb.lower(root.get("shortDesc")), likeSearchText);
            predicates.add(cb.or(searchByTitle, searchByShortDesc));
        }

        if (categoryId != null && !categoryId.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categoryId));
        }

        if (brandId != null && !brandId.isEmpty()) {
            predicates.add(root.get("brand").get("id").in(brandId));
        }

        if (minPrice != null && maxPrice != null){
            double doubleMin = (double) minPrice;
            double doubleMax = (double) maxPrice;
            predicates.add(cb.between(root.get("price"),doubleMin,doubleMax));
        }else {
            if (minPrice != null){
                double doubleMin = (double) minPrice;
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"),doubleMin));
            }
            if (maxPrice != null){
                double doubleMax = (double) maxPrice;
                predicates.add(cb.lessThanOrEqualTo(root.get("price"),doubleMax));
            }
        }
        try {
            RoleEntity role = roleManager.findByRoleName(RoleType.ROLE_ADMIN);
            boolean isAdmin = userManager.getCurrentUser().getRoles().stream().anyMatch(r->r.equals(role));
            if (isAdmin) {
                if (status != null){
                    predicates.add(cb.equal(root.get("status"),status));
                }
            }else throw new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE);
        }catch(ResourceNotFoundException e){
            CategoryStatus cStatus = CategoryStatus.PUBLISHED;
            BrandStatus bStatus = BrandStatus.PUBLISHED;
            ProductStatus pStatus = ProductStatus.PUBLISHED;
            predicates.add(cb.and(
                    cb.equal(root.get("status"), pStatus),
                    cb.equal(root.get("brand").get("status"), bStatus),
                    cb.equal(root.get("category").get("status"), cStatus)
            ));
        }
        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));

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

        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(ProductEntity.class)));
        countQuery.where(finalPredicate);
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        List<ProductDTO> productDTOList = productMapper.entityListToDTOList(typedQuery.getResultList());

        return new PageImpl<>(productDTOList, pageable, totalRecords);
    }

    @Override
    public ProductDTO getProductDTOById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public ProductDTO saveProduct(ProductRequest productRequest) {
        return null;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        return null;
    }

    @Override
    @Transactional
    public ProductDTO removeById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public void removeImageById(String id) {

    }
}
