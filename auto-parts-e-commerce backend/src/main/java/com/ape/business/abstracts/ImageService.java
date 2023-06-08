package com.ape.business.abstracts;

import com.ape.dto.ImageFileDTO;
import com.ape.entity.ImageFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ImageService {
    Set<String> saveImage(MultipartFile[] image);
    void setShowcaseImage(Long productId, String imageId);
    ImageFileEntity getImageById(String id);
    List<ImageFileDTO> getAllImages();
    void removeById(String id);
}
