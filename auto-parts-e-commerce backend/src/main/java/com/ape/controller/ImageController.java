package com.ape.controller;

import com.ape.business.concretes.ImageManager;
import com.ape.entity.dto.ImageFileDTO;
import com.ape.entity.dto.response.ImageResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import com.ape.entity.concrete.ImageFileEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageManager imageManager;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save image to database")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("image") MultipartFile[] image){
        Set<String> images = imageManager.saveImage(image);
        ImageResponse response=new ImageResponse(images,ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/showcase")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set showcase image for product with product and image ID")
    public ResponseEntity<Response> setShowcaseImageForProduct(@RequestParam("productId") Long productId,
                                                        @RequestParam("imageId") String imageId){
        imageManager.setShowcaseImage(productId,imageId);
        Response response = new Response(ResponseMessage.IMAGE_SHOWCASE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{imageId}")
    @Operation(summary = "Download image with image ID")
    public ResponseEntity<byte[]> downloadImage(@PathVariable("imageId") String imageId){
        ImageFileEntity imageFile = imageManager.getImageById(imageId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="+imageFile.getName()).body(imageFile.getImageData().getData());
    }

    @GetMapping("/display/{imageId}")
    @Operation(summary = "Display image with image ID")
    public ResponseEntity<byte[]>displayImage(@PathVariable("imageId") String imageId){
        ImageFileEntity imageFile= imageManager.getImageById(imageId);
        HttpHeaders header=new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageFile.getImageData().getData(), header, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all images")
    public ResponseEntity<List<ImageFileDTO>> getAllImages(){
        List<ImageFileDTO> response = imageManager.getAllImages();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove image with image ID")
    public ResponseEntity<Response>removeImage(@PathVariable("imageId") String imageId){
        imageManager.removeById(imageId);
        Response response =new Response(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

}
