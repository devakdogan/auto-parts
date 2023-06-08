package com.ape.business.concretes;

import com.ape.business.abstracts.ImageService;
import com.ape.dao.ImageDao;
import com.ape.dao.ProductDao;
import com.ape.dto.ImageFileDTO;
import com.ape.entity.ImageDataEntity;
import com.ape.entity.ImageFileEntity;
import com.ape.entity.ProductEntity;
import com.ape.exception.ImageFileException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageManager implements ImageService {
    private final ImageDao imageDao;
    private final ProductDao productDao;


    @Override
    @Transactional
    public Set<String> saveImage(MultipartFile[] image) {
        ImageFileEntity imageFile=null;
        Set<ImageFileEntity> images = new HashSet<>();

        for (MultipartFile each:image) {
            String fileName = null;
            try
            {
                imageFile = new ImageFileEntity();
                fileName = StringUtils.cleanPath(Objects.requireNonNull(each.getOriginalFilename()));
                ImageDataEntity imageData = new ImageDataEntity(each.getBytes());
                imageFile.setName(fileName);
                imageFile.setType(each.getContentType());
                imageFile.setImageData(imageData);
                imageFile.setShowcase(false);

            }catch (IOException e){
                throw new ImageFileException(e.getMessage());
            }
            imageDao.save(imageFile);
            images.add(imageFile);
        }
        return images.stream().map(ImageFileEntity::getId).collect(Collectors.toSet());
    }

    @Override
    public void setShowcaseImage(Long productId, String imageId) {
        ProductEntity product = productDao.findProductById(productId).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_MESSAGE));
        ImageFileEntity imageFile = getImageById(imageId);
        for (ImageFileEntity each:product.getImages()) {
            each.setShowcase(false);
        }
        imageFile.setShowcase(true);
        imageDao.save(imageFile);
    }

    @Override
    public ImageFileEntity getImageById(String id) {
        return imageDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public List<ImageFileDTO> getAllImages() {
        List<ImageFileEntity> imageList = imageDao.findAll();
        return imageList.stream().map(imgFile ->{
            String imageUri= ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/image/download/")
                    .path(imgFile.getId())
                    .toUriString();
            return new ImageFileDTO(imgFile.getName(),imageUri,imgFile.getType());
        } ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeById(String id) {
        ImageFileEntity imageFile = getImageById(id);
        imageDao.delete(imageFile);
    }
}
