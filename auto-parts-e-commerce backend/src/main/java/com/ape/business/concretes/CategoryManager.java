package com.ape.business.concretes;

import com.ape.business.abstracts.CategoryService;
import com.ape.dao.CategoryDao;
import com.ape.dao.ProductDao;
import com.ape.dto.CategoryDTO;
import com.ape.dto.request.CategoryRequest;
import com.ape.dto.request.CategoryUpdateRequest;
import com.ape.entity.CategoryEntity;
import com.ape.entity.RoleEntity;
import com.ape.entity.enums.CategoryStatus;
import com.ape.entity.enums.RoleType;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.CategoryMapper;
import com.ape.exception.ErrorMessage;
import com.ape.utility.NameFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryManager implements CategoryService {

    private final EntityManager entityManager;
    private final CategoryMapper categoryMapper;
    private final CategoryDao categoryDao;
    private final UserManager userManager;
    private final RoleManager roleManager;
    private final NameFilter nameFilter;
    private final ProductDao productDao;

    @Override
    public Page<CategoryDTO> getAllCategoriesWithFilterAndPage(String query, CategoryStatus status, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> criteriaQuery = cb.createQuery(CategoryEntity.class);
        Root<CategoryEntity> root = criteriaQuery.from(CategoryEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        Predicate finalPredicate = null;

        if (query != null && !query.isEmpty()) {
            String likeSearchText = "%" + query.toLowerCase(Locale.US) + "%";
            System.err.println(likeSearchText);
            predicates.add(cb.like(cb.lower(root.get("title")), likeSearchText));
        }

        try{
            RoleEntity role = roleManager.findByRoleName(RoleType.ROLE_ADMIN);
            boolean isAdmin = userManager.getCurrentUser().getRoles().stream().anyMatch(r->r.equals(role));
            if (isAdmin) {
                if (status != null){
                    predicates.add(cb.equal(root.get("status"),status));
                }
            }else throw  new ResourceNotFoundException(ErrorMessage.BRAND_NOT_FOUND_MESSAGE);
        }catch(ResourceNotFoundException e){
            predicates.add(cb.equal(root.get("status"), CategoryStatus.PUBLISHED));
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

        TypedQuery<CategoryEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(CategoryEntity.class)));
        countQuery.where(finalPredicate);
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        List<CategoryDTO> categoryDTOList = categoryMapper.categoryListToCategoryDTOList(typedQuery.getResultList());

        return new PageImpl<>(categoryDTOList, pageable, totalRecords);
    }

    @Override
    public List<CategoryDTO> getAllCategoryList() {
        List<CategoryEntity> categoryList = categoryDao.findAll();
        return categoryMapper.categoryListToCategoryDTOList(categoryList);
    }

    @Override
    @Transactional
    public CategoryDTO saveCategory(CategoryRequest categoryRequest) {
        CategoryEntity category=new CategoryEntity();
        boolean existTitle= categoryDao.existsByTitle(nameFilter.getNamesWithFilter(categoryRequest.getTitle()));
        if (existTitle) {
            throw new ConflictException(String.format(ErrorMessage.CATEGORY_USED_EXCEPTION,nameFilter.getNamesWithFilter(categoryRequest.getTitle())));
        }
        category.setTitle(nameFilter.getNamesWithFilter(categoryRequest.getTitle()));
        category.setStatus(CategoryStatus.NOT_PUBLISHED);
        categoryDao.save(category);
        return categoryMapper.entityToDTO(category);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        CategoryEntity category=getCategoryById(id);
        boolean existsTitle= categoryDao.existsByTitle(nameFilter.getNamesWithFilter(categoryUpdateRequest.getTitle()));

        if(existsTitle && ! categoryUpdateRequest.getTitle().equalsIgnoreCase(category.getTitle())) {
            throw new ConflictException(String.format(ErrorMessage.CATEGORY_USED_EXCEPTION,nameFilter.getNamesWithFilter(categoryUpdateRequest.getTitle())));}

        category.setTitle(nameFilter.getNamesWithFilter(categoryUpdateRequest.getTitle()));
        category.setStatus(categoryUpdateRequest.getStatus());
        category.setUpdateAt(LocalDateTime.now());
        categoryDao.save(category);
        return categoryMapper.entityToDTO(category);
    }

    @Override
    @Transactional
    public CategoryDTO removeById(Long id) {
        CategoryEntity category=getCategoryById(id);

        Boolean existsProduct= productDao.existsByCategoryId(id);

        if(existsProduct){
            throw new BadRequestException(ErrorMessage.CATEGORY_CAN_NOT_DELETE_EXCEPTION);
        }
        CategoryDTO categoryDTO=categoryMapper.entityToDTO(category);
        categoryDao.delete(category);
        return categoryDTO;
    }

    @Override
    public CategoryEntity getCategoryById(Long id) {
        return categoryDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
    }
}
